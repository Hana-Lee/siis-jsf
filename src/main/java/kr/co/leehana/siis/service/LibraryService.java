package kr.co.leehana.siis.service;

import java.util.List;

import kr.co.leehana.siis.model.Library;

/**
 * Created by Lee Hana on 2015-04-21 오후 5:56.
 *
 * @author Lee Hana
 */
public interface LibraryService {

	Library findById(String id);

	List<Library> findAll();

	List<Library> findByStatusAndCategoryLike(String status, String category);

	List<Library> findByStatus(String status);
}
