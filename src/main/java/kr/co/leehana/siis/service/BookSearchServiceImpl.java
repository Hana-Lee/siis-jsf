package kr.co.leehana.siis.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import javax.annotation.PostConstruct;

import kr.co.leehana.siis.concurrent.BookSearcher;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Controller;

/**
 * Created by Lee Hana on 2015-04-16 오후 1:35.
 *
 * @author Lee Hana
 */
@Controller
public class BookSearchServiceImpl implements BookSearchService {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private JdbcOperations jdbcOperations;

	@Autowired
	private HistoryService historyService;

	@PostConstruct
	public void init() {
	}

	@Override
	public List<Book> searchBookByWord(String searchWord, String searchType)
			throws SQLException, UnsupportedEncodingException,
			ExecutionException, InterruptedException {
		long startTime = System.currentTimeMillis();

		if (StringUtils.isBlank(searchWord) || searchWord.contains("이하나")) {
			return getLovelyBookList();
		} else {
			List<Book> searchBookList = historyService
					.getSearchResultHistory(searchWord);
			if (searchBookList != null && searchBookList.size() > 0) {
				return searchBookList;
			} else {
				ExecutorService executor = Executors.newFixedThreadPool(10);
				List<Future<List<Book>>> searcherList = new ArrayList<>();

				String searchUrlTemplate = "http://meta.seoul.go.kr/libstepsV5_seoul/ctrl/search.lsm"
						+ "?category1=%s&category2=&category3=&text1=%s&text2=&text3=&as1=&as2=&as3=&year1=&year2="
						+ "&dbnum=%s&recstart=%s&display=%s&target=&op=&op2=&sort=&id=%s&skey=798"
						+ "&ckey=0&host=115.84.165.14&_=1428567948467";

				String startIndex = "1";
				String pageCount = "10";

				Map<String, String> librariesCodeName = getAllLibraryCode();
				for (String libraryCode : librariesCodeName.keySet()) {
					String searchUrls = String.format(searchUrlTemplate,
							searchType, URLEncoder.encode(searchWord, "UTF-8"),
							libraryCode, startIndex, pageCount, makeSearchId());

					Callable<List<Book>> bookSearcher = new BookSearcher(
							libraryCode, librariesCodeName.get(libraryCode),
							searchUrls, searchWord, historyService);
					searcherList.add(executor.submit(bookSearcher));
				}

				executor.shutdown();

				while (!executor.isTerminated()) {
				}

				if (searchBookList == null) {
					searchBookList = new ArrayList<>();
				}

				for (Future<List<Book>> bookSearcher : searcherList) {
					List<Book> searchResult = bookSearcher.get();
					if (searchResult != null) {
						searchBookList.addAll(searchResult);
					}
				}

				log.info("Total Search Result Count : " + searchBookList.size());

				long endTime = System.currentTimeMillis();

				Date timeDiff = new Date(endTime - startTime);
				SimpleDateFormat timeFormat = new SimpleDateFormat(
						"H:mm:ss.SSS", Locale.KOREA);
				log.info("Duration millisecond : " + (endTime - startTime));
				log.info("Duration: " + timeFormat.format(timeDiff));

				return searchBookList;
			}
		}
	}

	private List<Book> getLovelyBookList() {
		List<Book> books = new ArrayList<>();
		Library library = new Library();
		for (int i = 0; i < 10; i++) {
			Book newBook = new Book();
			newBook.setAuthor("이하나 : " + i);
			newBook.setPublisher("하나출판사");
			newBook.setIsbnNumber("781026-800127");
			newBook.setTitle("사랑해 공주님~♥ " + i);

			library.setCode("777");
			library.setName("하나도서관");

			newBook.setLibrary(library);

			books.add(newBook);
		}

		return books;
	}

	private Map<String, String> getAllLibraryCode() throws SQLException {
		String query = "SELECT code, name FROM library "
				+ "WHERE status = 'enable' "
				+ "AND code NOT IN ("
				+ "'1081', '45511', '45311', '42921', '45081', '45061', '45511'"
				+ "'2141','22761','42341','42371','42401','42411','42881','42901',"
				+ "'43551','43591','43611','43621','43651','44011','44271','44331',"
				+ "'44341','44681','44731','44871','44971','44991','45021','45061',"
				+ "'45231','45241','45461','45571','45581','53781','38271','44901'"
				+ ") AND category LIKE '%1%' AND category LIKE '%4%'";

		List<Map<String, Object>> result = jdbcOperations.queryForList(query);

		Map<String, String> librariesCodeName = null;

		for (Map<String, Object> dataRow : result) {
			if (librariesCodeName == null) {
				librariesCodeName = new HashMap<>();
			}
			librariesCodeName.put(Objects.toString(dataRow.get("code")),
					Objects.toString(dataRow.get("name")));
		}

		return librariesCodeName;
	}

	private String makeSearchId() {
		int randomNumber = (int) ((Math.random() * 100) + 1);
		long currentTime = System.currentTimeMillis();
		return String.valueOf(currentTime + randomNumber);
	}
}
