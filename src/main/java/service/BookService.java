package service;

import model.Book;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:18.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@ManagedBean(name = "bookService")
@ApplicationScoped
public class BookService {

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book newBook = new Book();
            newBook.setAuthor("고미숙 : " + i);
            newBook.setPublisher("북드리망");
            newBook.setIsbnNumber("9788997969319");
            newBook.setName("연애의 시대 : 근대적 여성성과 사랑의 탄생 . " + i);

            books.add(newBook);
        }

        return books;
    }
}
