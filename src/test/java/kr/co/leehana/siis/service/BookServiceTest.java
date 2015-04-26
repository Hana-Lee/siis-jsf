package kr.co.leehana.siis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import kr.co.leehana.siis.config.WebAppConfigDevProfile;
import kr.co.leehana.siis.exception.BookNotFound;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;

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
public class BookServiceTest {

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

		assertThat(newBook.getId(), anyOf(equalTo(10L), equalTo(13L)));

		List<Book> allBooks = bookService.findAll();

		assertThat(allBooks, is(not(nullValue(List.class))));
		assertThat(allBooks.size(), is(10));
	}

	@Test
	@Transactional
	public void testCreateBooks() {
		List<Book> newBooks = makeDummyBookList();

		bookService.create(newBooks);

		for (Book newBook : newBooks) {
			assertThat(newBook.getId(), is(not(nullValue())));
		}
	}

	private List<Book> makeDummyBookList() {
		Library library = libraryService.findById("1791");
		List<Book> newBookList = new ArrayList<>();

		Book dummyBook1 = new Book();
		dummyBook1.setTitle("토비의 스프링3");
		dummyBook1.setAuthor("김일민");
		dummyBook1.setInfoUrl("http://www.google.co.kr");
		dummyBook1.setPublisher("한빛");
		dummyBook1.setYear("2013");
		dummyBook1.setLibrary(library);
		dummyBook1.setCreated(new Date(System.currentTimeMillis()));

		newBookList.add(dummyBook1);

		Book dummyBook2 = new Book();
		dummyBook2.setTitle("토비의 스프링3.1");
		dummyBook2.setAuthor("김일민");
		dummyBook2.setInfoUrl("http://www.google.co.kr");
		dummyBook2.setPublisher("한빛");
		dummyBook2.setYear("2013");
		dummyBook2.setLibrary(library);
		dummyBook2.setCreated(new Date(System.currentTimeMillis()));

		newBookList.add(dummyBook2);

		Book dummyBook3 = new Book();
		dummyBook3.setTitle("토비의 스프링 3.1 시작부터 끝까지");
		dummyBook3.setAuthor("김일민 외 1인");
		dummyBook3.setInfoUrl("http://www.google.co.kr");
		dummyBook3.setPublisher("한빛");
		dummyBook3.setYear("2013");
		dummyBook3.setLibrary(library);
		dummyBook3.setCreated(new Date(System.currentTimeMillis()));

		newBookList.add(dummyBook3);

		return newBookList;
	}

	@Test
	@Transactional
	public void testDeleteBook() throws BookNotFound {
		Book book = bookService.findById(1L);

		assertThat(book, is(not(nullValue(Book.class))));

		bookService.delete(book.getId());

		List<Book> books = bookService.findAll();

		assertThat(books, is(not(nullValue(List.class))));
		assertThat(books.size(), is(8));
	}

	@Test(expected = BookNotFound.class)
	@Transactional
	public void testDeleteBookWithBookNotFoundException() throws BookNotFound {
		bookService.delete(10L);
	}

	@Test
	@Transactional
	public void testUpdateBook() throws BookNotFound {
		String newAuthor = "이하나";
		Book book = bookService.findById(1L);
		book.setAuthor(newAuthor);
		bookService.update(book);

		assertThat(book.getAuthor(), is(newAuthor));
	}

	@Test(expected = BookNotFound.class)
	@Transactional
	public void testUpdateBookWithBookNotFoundException() throws BookNotFound {
		Book book = bookService.findById(1L);
		book.setId(10L);

		bookService.update(book);
	}

	@Test
	@Transactional(readOnly = true)
	public void testGetLibraryFromBook() {
		Book book = bookService.findById(1L);
		Library library = book.getLibrary();

		assertThat(library, is(not(nullValue(Library.class))));
		assertThat(library.getCode(), is("1081"));
	}

	@Test
	@Transactional
	public void testGetAllBooks() {
		List<Book> books = bookService.findAll();
		assertThat(books, is(not(nullValue(List.class))));
		assertThat(books.size(), is(9));
	}
}
