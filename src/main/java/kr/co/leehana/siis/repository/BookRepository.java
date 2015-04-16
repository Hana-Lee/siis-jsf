package kr.co.leehana.siis.repository;

import kr.co.leehana.siis.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Lee Hana on 2015-04-16 오전 11:01.
 *
 * @author Lee Hana
 */
public interface BookRepository extends JpaRepository<Book, String> {
}
