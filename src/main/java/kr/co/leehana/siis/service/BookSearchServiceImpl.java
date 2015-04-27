package kr.co.leehana.siis.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;

import javax.annotation.PostConstruct;

import kr.co.leehana.siis.concurrent.BookSearcher;
import kr.co.leehana.siis.helper.StringHelper;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;
import kr.co.leehana.siis.model.SearchHistory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lee Hana on 2015-04-16 오후 1:35.
 *
 * @author Lee Hana
 */
@Service
public class BookSearchServiceImpl implements BookSearchService {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private SearchHistoryService searchHistoryService;

	@PostConstruct
	public void init() {
	}

	@Override
	public List<Book> searchBookByWord(String searchWord, String searchType,
			List<Library> libraries) throws UnsupportedEncodingException,
			ExecutionException, InterruptedException {
		long startTime = System.currentTimeMillis();

		if (StringUtils.isBlank(searchWord) || searchWord.contains("이하나")) {
			return getLovelyBookList();
		} else {
			SearchHistory searchHistoryList = searchHistoryService
					.findBySearchWord(StringHelper
							.convertWhiteSpacesToUnderscores(searchWord));
			if (searchHistoryList != null) {
				return searchHistoryList.getBooks();
			} else {
				ExecutorService executor = Executors.newFixedThreadPool(10);
				List<Future<List<Book>>> searcherList = new ArrayList<>();

				String searchUrlTemplate = "http://meta.seoul.go.kr/libstepsV5_seoul/ctrl/search.lsm"
						+ "?category1=%s&category2=&category3="
						+ "&text1=%s&text2=&text3="
						+ "&as1=&as2=&as3=&year1=&year2="
						+ "&dbnum=%s&recstart=%s&display=%s"
						+ "&target=&op=&op2=&sort=&id=%s&skey=798"
						+ "&ckey=0&host=115.84.165.14&_=1428567948467";

				String startIndex = "1";
				String pageCount = "10";

				for (Library library : libraries) {
					String searchUrls = String.format(searchUrlTemplate,
							searchType, URLEncoder.encode(searchWord, "UTF-8"),
							library.getCode(), startIndex, pageCount,
							makeSearchId());

					Callable<List<Book>> bookSearcher = new BookSearcher(
							library, searchUrls);
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

	private String makeSearchId() {
		int randomNumber = (int) ((Math.random() * 100) + 1);
		long currentTime = System.currentTimeMillis();
		return String.valueOf(currentTime + randomNumber);
	}
}
