package kr.co.leehana.siis.service;


import kr.co.leehana.siis.exception.BookNotFound;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:18.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@Service
public class BookServiceImpl implements BookService {

    @Resource
    private BookRepository bookRepository;

    @Override
    @Transactional
    public Book create(Book book) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = BookNotFound.class)
    public Book delete(int id) throws BookNotFound {
        return null;
    }

    @Override
    @Transactional
    public List<Book> findAll() {
        return null;
    }

    @Override
    @Transactional(rollbackFor = BookNotFound.class)
    public Book update(Book book) throws BookNotFound {
        return null;
    }

    @Override
    @Transactional
    public List<Book> findByName(String bookName) {
        return null;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book newBook = new Book();
            newBook.setAuthor(author + " : " + i);
            newBook.setPublisher("북드리망");
            newBook.setIsbnNumber("9788997969319");
            newBook.setName("연애의 시대 : 근대적 여성성과 사랑의 탄생 . " + i);

            books.add(newBook);
        }

        return books;
    }
}
