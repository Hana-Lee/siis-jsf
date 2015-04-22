package kr.co.leehana.siis.model;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import kr.co.leehana.siis.config.WebAppConfig;
import kr.co.leehana.siis.service.BookService;
import kr.co.leehana.siis.service.LibraryService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lee Hana on 2015-04-04 오전 7:42.
 *
 * @author Lee Hana
 * @since 2015-04-04 오전 7:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebAppConfig.class)
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

		List<Book> oldBooks = bookService.findAll();

		Assert.assertEquals(1L, newBook.getId());

		for (Book book : oldBooks) {
			System.out.println("new book id : " + book.getId());
		}

		List<Book> books2 = newLibrary.getBooks();
		for (Book book : books2) {
			System.out.println("old book id : " + book.getId());
		}
	}

	@Test
	public void testGetBookBySearchWord() {
	}
}
