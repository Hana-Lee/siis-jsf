package kr.co.leehana.siis.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.service.BookSearchService;
import kr.co.leehana.siis.service.HistoryService;
import kr.co.leehana.siis.type.SearchType;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.component.datalist.DataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:19.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@Component
@Scope(value = "session")
public class BookHandler implements Serializable {
	private static final long serialVersionUID = -2701377876970306880L;
	private final Log log = LogFactory.getLog(getClass());

	@Getter
	@Setter
	private String searchWord;

	@Getter
	@Setter
	private List<Book> books;

	@Autowired
	private BookSearchService bookSearchService;

	@Autowired
	private HistoryService historyService;

	@Getter
	@Setter
	private String selectedSearchType;

	@Getter
	private SearchType[] searchTypes = SearchType.values();

	@Getter
	@Setter
	private DataList searchResultList;

	@PostConstruct
	public void init() {
	}

	public String searchBookAction() throws ClassNotFoundException,
			SQLException, InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		books = bookSearchService.searchBookByWord(searchWord,
				selectedSearchType);

		return "pm:search-result";
	}

	public void bookSelectActionListener(Object obj) {
		log.info(obj);
	}

	public void initializeActionListener(ActionEvent event) {
		books = null;
		searchWord = null;
	}
}
