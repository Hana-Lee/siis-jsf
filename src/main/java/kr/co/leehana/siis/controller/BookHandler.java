package kr.co.leehana.siis.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;
import kr.co.leehana.siis.model.SearchHistory;
import kr.co.leehana.siis.service.BookSearchService;
import kr.co.leehana.siis.service.LibraryService;
import kr.co.leehana.siis.service.SearchHistoryService;
import kr.co.leehana.siis.type.SearchType;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:19.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@ManagedBean
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
	private LibraryService libraryService;

	@Autowired
	private SearchHistoryService searchHistoryService;

	@Getter
	@Setter
	private String selectedSearchType;

	@Getter
	private SearchType[] searchTypes = SearchType.values();

	@Getter
	@Setter
	private Book selectedBook;

	@PostConstruct
	public void init() {
	}

	public String searchBookAction() throws ClassNotFoundException,
			SQLException, InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		List<Library> libraries = libraryService
				.findEnableLibraryByCategoryLike(selectedSearchType);

		books = bookSearchService.searchBookByWord(searchWord,
				selectedSearchType, libraries);

		if (books != null && books.size() > 0) {
			saveSearchHistory();
		}

		return "pm:search-result";
	}

	private void saveSearchHistory() {
		SearchHistory searchHistory = searchHistoryService.findBySearchWord(searchWord);
		if (searchHistory == null) {
			searchHistory = new SearchHistory();
			searchHistory.setSearchWord(searchWord);
			searchHistory.setBooks(books);

			searchHistoryService.create(searchHistory);
		}
	}

	public void bookSelectActionListener(Book book) {
		log.info("selected Boook : " + book);
		if (book != null) {
			log.info("Selected book info write");
		}
	}
}
