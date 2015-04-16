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

    public Book create(Book book);
    public Book delete(int id) throws BookNotFound;
    public List<Book> findAll();
    public Book update(Book book) throws BookNotFound;
    public List<Book> findByName(String bookName);
    public List<Book> findByAuthor(String author);
}
