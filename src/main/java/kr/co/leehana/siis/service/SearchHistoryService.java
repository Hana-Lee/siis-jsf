package kr.co.leehana.siis.service;

import java.util.List;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.SearchHistory;

public interface SearchHistoryService {

	SearchHistory create(SearchHistory searchHistory);

	SearchHistory findBySearchWord(String searchWord);
}
