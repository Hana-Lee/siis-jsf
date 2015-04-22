package kr.co.leehana.siis.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.siis.config.WebAppConfig;
import kr.co.leehana.siis.service.LibraryService;
import kr.co.leehana.siis.service.SearchHistoryService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lee Hana on 2015-04-22 오후 3:04.
 *
 * @author Lee Hana
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebAppConfig.class)
public class SearchHistoryTest {

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private SearchHistoryService searchHistoryService;

	@Autowired
	private LibraryService libraryService;

	private List<Book> bookList = new ArrayList<>();

	@Before
	public void setUp() throws Exception {

		Library library = libraryService.findById("1791");

		Book dummyBook1 = new Book();
		dummyBook1.setTitle("토비의 스프링3");
		dummyBook1.setAuthor("김일민");
		dummyBook1.setInfoUrl("http://www.google.co.kr");
		dummyBook1.setPublisher("한빛");
		dummyBook1.setYear("2013");
		dummyBook1.setLibrary(library);
		dummyBook1.setCreated(new Date(System.currentTimeMillis()));

		bookList.add(dummyBook1);

		Book dummyBook2 = new Book();
		dummyBook2.setTitle("토비의 스프링3.1");
		dummyBook2.setAuthor("김일민");
		dummyBook2.setInfoUrl("http://www.google.co.kr");
		dummyBook2.setPublisher("한빛");
		dummyBook2.setYear("2013");
		dummyBook2.setLibrary(library);
		dummyBook2.setCreated(new Date(System.currentTimeMillis()));

		bookList.add(dummyBook2);

		Book dummyBook3 = new Book();
		dummyBook3.setTitle("토비의 스프링 3.1 시작부터 끝까지");
		dummyBook3.setAuthor("김일민 외 1인");
		dummyBook3.setInfoUrl("http://www.google.co.kr");
		dummyBook3.setPublisher("한빛");
		dummyBook3.setYear("2013");
		dummyBook3.setLibrary(library);
		dummyBook3.setCreated(new Date(System.currentTimeMillis()));

		bookList.add(dummyBook3);

		SearchHistory history = new SearchHistory();
		history.setSearchWord(convertWhiteSpacesToUnderscores("클린 코드"));
		history.setBooks(bookList);

		searchHistoryService.create(history);
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void testCreateSearchHistoryWithNewBookList() {
		SearchHistory history = new SearchHistory();
		history.setSearchWord(convertWhiteSpacesToUnderscores("토비의 스프링3"));
		history.setBooks(bookList);

		searchHistoryService.create(history);

		assertThat(history.getId(), is(3L));
	}

	@Test
	@Transactional
	public void testGetSearchHistoryWithSearchWord() {
		String searchWord = "클린_코드";
		SearchHistory oldHistory = searchHistoryService
				.findBySearchWord(convertWhiteSpacesToUnderscores(searchWord));
		assertThat(oldHistory, is(not(nullValue(SearchHistory.class))));
		assertThat(oldHistory.getSearchWord(), is(searchWord));
	}

	private String convertWhiteSpacesToUnderscores(String searchWord) {
		return searchWord.trim().replaceAll("\\s+", "_");
	}
}
