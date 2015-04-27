package kr.co.leehana.siis.service;

import kr.co.leehana.siis.model.SearchHistory;
import kr.co.leehana.siis.repository.SearchHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

	@Autowired
	private SearchHistoryRepository searchHistoryRepository;

	@Override
	@Transactional
	public SearchHistory create(SearchHistory searchHistory) {
		return searchHistoryRepository.save(searchHistory);
	}

	@Override
	@Transactional
	public SearchHistory findBySearchWord(String searchWord) {
		return searchHistoryRepository.findBySearchWord(searchWord);
	}
}
