package kr.co.leehana.siis.service;

import java.util.List;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;

public interface HistoryService {

	void writeSelectedLibrary(Library library);
	void writeSearchResult(String searchWord, List<Book> searchResult);
	List<Book> getSearchResultHistory(String searchWord);
}
