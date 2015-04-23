package kr.co.leehana.siis.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import kr.co.leehana.siis.config.WebAppConfigDevProfile;
import kr.co.leehana.siis.service.LibraryService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class LibraryTest {

	private final Log log = LogFactory.getLog(getClass());

	private final List<String> libraryCodeList = Arrays.asList("2161", "1081",
			"1851");

	@Autowired
	private LibraryService libraryService;

	@Test
	public void testGetAllLibrary() {
		List<Library> libraries = libraryService.findAll();

		assertThat(libraries, is(not(nullValue(List.class))));
		assertThat(libraries.size(), is(352));
	}

	@Test
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
}