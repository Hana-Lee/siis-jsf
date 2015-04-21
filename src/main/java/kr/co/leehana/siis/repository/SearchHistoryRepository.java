package kr.co.leehana.siis.repository;

import kr.co.leehana.siis.model.SearchHistory;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Lee Hana on 2015-04-21 오후 5:17.
 *
 * @author Lee Hana
 */
public interface SearchHistoryRepository extends
		JpaRepository<SearchHistory, String> {
}
