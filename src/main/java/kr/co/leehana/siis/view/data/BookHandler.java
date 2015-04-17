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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:19.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@ManagedBean
@ViewScoped
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

    public List<Book> getBooks() throws InterruptedException, ClassNotFoundException, SQLException, ExecutionException, UnsupportedEncodingException {
        if (books == null) {
            books = bookSearchService.searchBookByWord(searchWord, SearchType.ALL.getCode());
        }
        return books;
    }

    public String searchBookAction() {
        return "pm:search-result";
    }
}
