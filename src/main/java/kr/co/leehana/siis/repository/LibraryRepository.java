package kr.co.leehana.siis.repository;

import kr.co.leehana.siis.model.Library;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Lee Hana on 2015-04-21 오후 5:19.
 *
 * @author Lee Hana
 */
public interface LibraryRepository extends JpaRepository<Library, String> {

    List<Library> findByStatus(String status);
    List<Library> findByStatusAndCategoryLike(String status, String category);
}
