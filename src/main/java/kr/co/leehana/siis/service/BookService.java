package kr.co.leehana.siis.service;

import kr.co.leehana.siis.exception.BookNotFound;
import kr.co.leehana.siis.model.Book;

import java.util.List;

/**
 * Created by Lee Hana on 2015-04-16 오전 11:06.
 *
 * @author Lee Hana
 */
public interface BookService {

	Book create(Book book);

	Book delete(int id) throws BookNotFound;

	List<Book> findAll();

	Book update(Book book) throws BookNotFound;

	List<Book> findByName(String bookName);

	List<Book> findByAuthor(String author);
}
