package view.data;

import model.Book;
import service.BookService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:19.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@ManagedBean(name = "bookHandler")
@ViewScoped
public class BookDataHandler implements Serializable {

    private List<Book> books;

    @ManagedProperty("#{bookService}")
    private BookService bookService;

    @PostConstruct
    public void init() {
        this.books = bookService.getBooks();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
