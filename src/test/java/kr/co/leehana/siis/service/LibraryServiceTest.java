package kr.co.leehana.siis.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import kr.co.leehana.siis.config.WebAppConfigDevProfile;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.MatcherAssert;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lee Hana on 2015-04-21 오후 3:11.
 *
 * @author Lee Hana
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebAppConfigDevProfile.class)
@ActiveProfiles(value = "dev")
public class LibraryServiceTest {

	private final Log log = LogFactory.getLog(getClass());

	private final List<String> libraryCodeList = Arrays.asList("2161", "1081",
			"1851");

	@Autowired
	private LibraryService libraryService;

	@Test
	@Transactional
	public void testGetAllLibrary() {
		List<Library> libraries = libraryService.findAll();

		assertThat(libraries, is(not(nullValue(List.class))));
		assertThat(libraries.size(), is(352));
	}

	@Test
	@Transactional
	public void testGetLibrary() {
		for (String libraryCode : libraryCodeList) {
			Library library = libraryService.findById(libraryCode);

			assertThat(library, is(not(nullValue(Library.class))));
			assertThat(library.getCode(), is(libraryCode));
		}
	}

	@Test
	@Transactional
	public void testGetBooksFromLibrary() {
		for (String libraryCode : libraryCodeList) {
			Library library = libraryService.findById(libraryCode);
			List<Book> books = library.getBooks();

			assertThat(books, is(not(nullValue(List.class))));
			assertThat(books.size(), is(3));
		}
	}

	@Test
	@Transactional
	public void testGetEmptyBooksFromLibrary() {
		final String libraryCode = "42921";
		Library library = libraryService.findById(libraryCode);
		List<Book> books = library.getBooks();

		assertThat(books, is(not(nullValue(List.class))));
		assertThat(books.size(), is(0));
	}

	@Test(expected = LazyInitializationException.class)
	public void testFetchLazyException() {
		final String libraryCode = "42921";
		Library library = libraryService.findById(libraryCode);
		List<Book> books = library.getBooks();

		assertThat(books, is(not(nullValue(List.class))));
		assertThat(books.size(), is(0));
	}

	@Test
	@Transactional
	public void testFindByStatus() {
		String enableStatus = "enable";
		List<Library> enableLibraries = libraryService
				.findByStatus(enableStatus);

		assertThat(enableLibraries, is(not(nullValue(List.class))));
		assertThat(enableLibraries.size(), is(not(0)));

		for (Library enableLibrary : enableLibraries) {
			assertThat(enableLibrary.getStatus(), is(enableStatus));
		}

		String disabledStatus = "disabled";
		List<Library> disabledLibraries = libraryService
				.findByStatus(disabledStatus);

		assertThat(disabledLibraries, is(not(nullValue(List.class))));
		assertThat(disabledLibraries.size(), is(not(0)));

		for (Library disabledLibrary : disabledLibraries) {
			assertThat(disabledLibrary.getStatus(), is(disabledStatus));
		}
	}

	@Test
	@Transactional
	public void testFindByStatusAndCategoryLike() throws SQLException {
		String status = "enable";
		String category = "4";

		List<Library> libraries = libraryService.findByStatusAndCategoryLike(
				status, category);

		MatcherAssert.assertThat(libraries, is(not(nullValue(List.class))));
		MatcherAssert.assertThat(libraries.size(), is(not(0)));

		for (Library library : libraries) {
			MatcherAssert.assertThat(library.getCategory(),
					containsString(category));
		}

		category = "1";
		libraries = libraryService
				.findByStatusAndCategoryLike(status, category);

		MatcherAssert.assertThat(libraries, is(not(nullValue(List.class))));
		MatcherAssert.assertThat(libraries.size(), is(not(0)));

		for (Library library : libraries) {
			MatcherAssert.assertThat(library.getCategory(),
					containsString(category));
		}
	}

	@Test
	public void testFindEnableLibraryByCategoryLike() {
		String category = "4";

		List<Library> libraries = libraryService
				.findEnableLibraryByCategoryLike(category);

		MatcherAssert.assertThat(libraries, is(not(nullValue(List.class))));
		MatcherAssert.assertThat(libraries.size(), is(not(0)));

		for (Library library : libraries) {
			MatcherAssert.assertThat(library.getCategory(),
					containsString(category));
		}

		category = "1";
		libraries = libraryService.findEnableLibraryByCategoryLike(category);

		MatcherAssert.assertThat(libraries, is(not(nullValue(List.class))));
		MatcherAssert.assertThat(libraries.size(), is(not(0)));

		for (Library library : libraries) {
			MatcherAssert.assertThat(library.getCategory(),
					containsString(category));
		}
	}
}