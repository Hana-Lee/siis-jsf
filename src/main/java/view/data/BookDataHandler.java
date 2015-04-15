package view.data;

import lombok.Getter;
import lombok.Setter;
import model.Book;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.component.button.Button;
import org.primefaces.component.commandbutton.CommandButton;
import service.BookService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
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
    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(getClass());

    @Setter
    private String searchWord;

    @Setter
    private List<Book> books;

    @ManagedProperty(value = "#{bookService}")
    @Setter
    private BookService bookService;

    @PostConstruct
    public void init() {
    }

    public String getSearchWord() {
        return searchWord;
    }

    public List<Book> getBooks() throws InterruptedException {
        this.books = bookService.getBooks(this.searchWord);
        return books;
    }

    public String searchBookAction() {
        return "pm:search-result";
    }
}
