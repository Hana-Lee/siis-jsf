package kr.co.leehana.siis.service;

import kr.co.leehana.siis.model.SearchHistory;

public interface SearchHistoryService {

	SearchHistory create(SearchHistory searchHistory);

	SearchHistory findBySearchWord(String searchWord);
}
