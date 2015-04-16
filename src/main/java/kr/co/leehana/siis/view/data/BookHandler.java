package kr.co.leehana.siis.view.data;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.service.BookSearchService;
import kr.co.leehana.siis.type.SearchType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:19.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@ManagedBean
@RequestScoped
@Component
public class BookHandler implements Serializable {
    private static final long serialVersionUID = -2701377876970306880L;
    private Log log = LogFactory.getLog(getClass());

    @Getter
    @Setter
    private String searchWord;

    @Setter
    private List<Book> books;

    @Setter
    @Autowired
    private BookSearchService bookSearchService;

    @Getter
    @Setter
    private String selectedSearchType;

    private SearchType[] searchTypes;

    @PostConstruct
    public void init() {
    }

    public SearchType[] getSearchTypes() {
        return SearchType.values();
    }

    public List<Book> getBooks() throws InterruptedException {
        log.info("Search word : " + searchWord);
        log.info("selected search type : " + selectedSearchType);
        this.books = bookSearchService.searchBookByWord(searchWord, SearchType.NAME);
        return books;
    }

    public String searchBookAction() {
        log.info("Search Btn");
        return "pm:search-result";
    }

    public void searchActionListener(ActionEvent event) {
        log.info("Search Action Listener");
    }
}
