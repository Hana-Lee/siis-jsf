package kr.co.leehana.siis.repository;

import kr.co.leehana.siis.model.SearchHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Lee Hana on 2015-04-21 오후 5:17.
 *
 * @author Lee Hana
 */
public interface SearchHistoryRepository extends
		JpaRepository<SearchHistory, Long> {

	SearchHistory findBySearchWord(String searchWord);
}
