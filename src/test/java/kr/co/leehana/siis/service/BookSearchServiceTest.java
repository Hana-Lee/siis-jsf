package kr.co.leehana.siis.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kr.co.leehana.siis.config.WebAppConfigDevProfile;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;
import kr.co.leehana.siis.type.SearchType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
	private LibraryService libraryService;

	@Autowired
	private BookSearchService bookSearchService;

	@Test
	@Transactional(readOnly = true)
	public void testSearchBookByNonExistSearchWord()
			throws ClassNotFoundException, SQLException, InterruptedException,
			ExecutionException, UnsupportedEncodingException {
		String searchWord = "토비의 스프링 3.1";
		String searchType = SearchType.NAME.getCode();

		List<Library> libraries = getEnableLibrariesByCategory(searchType);

		List<Book> searchResult = bookSearchService.searchBookByWord(
				searchWord, searchType, libraries);

		assertThat(searchResult, is(not(nullValue(List.class))));
		assertThat(searchResult.size(), is(not(0)));

		for (Book book : searchResult) {
			assertThat(book.getTitle(), is(not(nullValue(String.class))));
			assertThat(book.getTitle(), is(not(StringUtils.EMPTY)));

			assertThat(book.getAuthor(), is(not(nullValue(String.class))));
			assertThat(book.getAuthor(), is(not(StringUtils.EMPTY)));
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void testSearchBookByExistSearchWord()
			throws ClassNotFoundException, SQLException, InterruptedException,
			ExecutionException, UnsupportedEncodingException {
		String searchWord = "토비의 스프링3";
		String searchType = SearchType.NAME.getCode();

		List<Library> libraries = getEnableLibrariesByCategory(searchType);

		List<Book> searchResult = bookSearchService.searchBookByWord(
				searchWord, searchType, libraries);

		assertThat(searchResult, is(not(nullValue(List.class))));
		assertThat(searchResult.size(), is(not(0)));

		for (Book book : searchResult) {
			assertThat(book.getId(), is(not(nullValue())));
			assertThat(book.getId(), is(not(0L)));
		}
	}

	@Test
	public void testSearchBookByEmptySearchWord()
			throws ClassNotFoundException, SQLException, InterruptedException,
			ExecutionException, UnsupportedEncodingException {
		String searchWord = StringUtils.EMPTY;
		String searchType = SearchType.NAME.getCode();

		List<Library> libraries = getEnableLibrariesByCategory(searchType);

		List<Book> searchResult = bookSearchService.searchBookByWord(
				searchWord, searchType, libraries);

		assertThat(searchResult, is(not(nullValue(List.class))));
		assertThat(searchResult.size(), is(10));

		for (Book book : searchResult) {
			assertThat(book.getId(), is(0L));

			assertThat(book.getTitle(), is(not(nullValue(String.class))));
			assertThat(book.getTitle(), is(not(StringUtils.EMPTY)));

			assertThat(book.getAuthor(), is(not(nullValue(String.class))));
			assertThat(book.getAuthor(), is(not(StringUtils.EMPTY)));
		}
	}

	private List<Library> getEnableLibrariesByCategory(String category) {
		Library library = libraryService.findById("46891");

		List<Library> libraries = new ArrayList<>(1);
		libraries.add(library);

		return libraries;
	}
}
