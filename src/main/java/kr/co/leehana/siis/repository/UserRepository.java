package kr.co.leehana.siis.repository;

import kr.co.leehana.siis.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Lee Hana on 2015-04-21 오후 5:19.
 *
 * @author Lee Hana
 */
public interface UserRepository extends JpaRepository<User, String> {
}
