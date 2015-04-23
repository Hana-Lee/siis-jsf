package kr.co.leehana.siis.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import java.sql.Date;
import java.util.List;

import kr.co.leehana.siis.config.WebAppConfigDevProfile;
import kr.co.leehana.siis.exception.BookNotFound;
import kr.co.leehana.siis.service.BookService;
import kr.co.leehana.siis.service.LibraryService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lee Hana on 2015-04-04 오전 7:42.
 *
 * @author Lee Hana
 * @since 2015-04-04 오전 7:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebAppConfigDevProfile.class)
@ActiveProfiles(value = "dev")
public class BookTest {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private BookService bookService;

	@Test
	@Transactional
	public void testCreateBook() {
		Library newLibrary = libraryService.findById("1381");

		Book newBook = new Book();
		newBook.setAuthor("이하나");
		newBook.setTitle("토비의 스프링3");
		newBook.setInfoUrl("http://www.google.co.kr");
		newBook.setPublisher("한빛도서");
		newBook.setYear("2010");
		newBook.setLibrary(newLibrary);
		newBook.setCreated(new Date(System.currentTimeMillis()));

		bookService.create(newBook);

		assertThat(newBook.getId(), is(10L));

		List<Book> allBooks = bookService.findAll();

		assertThat(allBooks, is(not(nullValue(List.class))));
		assertThat(allBooks.size(), is(10));
	}

	@Test
	public void testDeleteBook() throws BookNotFound {
		Book book = bookService.findById(1L);

		assertThat(book, is(not(nullValue(Book.class))));

		bookService.delete(book.getId());

		List<Book> books = bookService.findAll();

		assertThat(books, is(not(nullValue(List.class))));
		assertThat(books.size(), is(8));
	}

	@Test(expected = BookNotFound.class)
	public void testDeleteBookWithBookNotFoundException() throws BookNotFound {
		bookService.delete(10L);
	}

	@Test
	public void testUpdateBook() throws BookNotFound {
		String newAuthor = "이하나";
		Book book = bookService.findById(1L);
		book.setAuthor(newAuthor);
		bookService.update(book);

		assertThat(book.getAuthor(), is(newAuthor));
	}

	@Test(expected = BookNotFound.class)
	public void testUpdateBookWithBookNotFoundException() throws BookNotFound {
		Book book = bookService.findById(1L);
		book.setId(10L);

		bookService.update(book);
	}

	@Test
	@Transactional
	public void testGetLibraryFromBook() {
		Book book = bookService.findById(1L);
		Library library = book.getLibrary();

		assertThat(library, is(not(nullValue(Library.class))));
		assertThat(library.getCode(), is("1081"));
	}
}
