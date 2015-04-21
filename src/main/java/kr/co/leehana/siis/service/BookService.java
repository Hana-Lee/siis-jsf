package kr.co.leehana.siis.service;

import java.util.List;

import kr.co.leehana.siis.exception.BookNotFound;
import kr.co.leehana.siis.model.Book;

/**
 * Created by Lee Hana on 2015-04-16 오전 11:06.
 *
 * @author Lee Hana
 */
public interface BookService {

	Book create(Book book);

	Book delete(long id) throws BookNotFound;

	List<Book> findAll();

	Book update(Book book) throws BookNotFound;

	Book findById(long id);
}
