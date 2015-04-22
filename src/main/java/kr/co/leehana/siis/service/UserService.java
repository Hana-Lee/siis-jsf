package kr.co.leehana.siis.service;

import java.util.List;

import kr.co.leehana.siis.exception.UserNotFound;
import kr.co.leehana.siis.model.User;

/**
 * Created by Lee Hana on 2015-04-22 오후 3:15.
 *
 * @author Lee Hana
 */
public interface UserService {

	User create(User user);

	User delete(String email) throws UserNotFound;

	User findById(String email);

    User update(User user) throws UserNotFound;

	List<User> findAll();
}
