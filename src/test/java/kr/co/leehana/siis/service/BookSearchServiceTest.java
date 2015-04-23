package kr.co.leehana.siis.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import kr.co.leehana.siis.concurrent.BookSearcher;
import kr.co.leehana.siis.config.WebAppConfigDevProfile;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;
import kr.co.leehana.siis.model.SearchHistory;
import kr.co.leehana.siis.type.SearchType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lee Hana on 2015-04-21 오후 4:02.
 *
 * @author Lee Hana
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebAppConfigDevProfile.class)
@ActiveProfiles(value = "dev")
public class BookSearchServiceTest {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private JdbcOperations jdbcOperations;

	@Autowired
	private SearchHistoryService searchHistoryService;

	@Test
	public void testSearchBookByWord() throws InterruptedException,
			SQLException, ExecutionException, UnsupportedEncodingException {
		String searchWord = "토비의 스프링3";
		String searchType = SearchType.NAME.getCode();

		List<Book> searchResult = searchBookByWord(searchWord, searchType);

		Assert.assertTrue(searchResult.size() > 0);
	}

	private List<Book> searchBookByWord(String searchWord, String searchType)
			throws SQLException, UnsupportedEncodingException,
			ExecutionException, InterruptedException {
		long startTime = System.currentTimeMillis();

		if (StringUtils.isBlank(searchWord) || searchWord.contains("이하나")) {
			return getLovelyBookList();
		} else {
			SearchHistory searchHistoryList = searchHistoryService
					.findBySearchWord(searchWord);
			if (searchHistoryList != null) {
				return searchHistoryList.getBooks();
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
							searchUrls, searchWord, searchHistoryService);
					searcherList.add(executor.submit(bookSearcher));
				}

				executor.shutdown();

				while (!executor.isTerminated()) {
				}

				List<Book> searchBookList = new ArrayList<>();

				for (Future<List<Book>> bookSearcher : searcherList) {
					List<Book> searchResult = bookSearcher.get();
					if (searchResult != null) {
						searchBookList.addAll(searchResult);
					}
				}

				log.info("Total Search Result Count : " + searchBookList.size());

				SearchHistory searchHistory = new SearchHistory();
				searchHistory.setSearchWord(searchWord);
				searchHistory.setBooks(searchBookList);

				searchHistoryService.create(searchHistory);

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
		String query = "SELECT CODE, NAME FROM LIBRARY "
				+ "WHERE STATUS = 'enable' "
				+ "AND CODE NOT IN ("
				+ "'1081', '45511', '45311', '42921', '45081', '45061', '45511'"
				+ "'2141','22761','42341','42371','42401','42411','42881','42901',"
				+ "'43551','43591','43611','43621','43651','44011','44271','44331',"
				+ "'44341','44681','44731','44871','44971','44991','45021','45061',"
				+ "'45231','45241','45461','45571','45581','53781','38271','44901'"
				+ ") AND CATEGORY LIKE '%1%' AND CATEGORY LIKE '%4%'";

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
