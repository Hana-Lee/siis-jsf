package kr.co.leehana.siis.service;

import java.util.List;

import kr.co.leehana.siis.exception.UserNotFound;
import kr.co.leehana.siis.model.User;
import kr.co.leehana.siis.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lee Hana on 2015-04-22 오후 3:30.
 *
 * @author Lee Hana
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public User create(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional(rollbackFor = UserNotFound.class)
	public User delete(String email) throws UserNotFound {
		User deleteTargetUser = findById(email);
		if (deleteTargetUser == null) {
			throw new UserNotFound();
		}

		userRepository.delete(deleteTargetUser);

		return deleteTargetUser;
	}

	@Override
	@Transactional
	public User findById(String email) {
		return userRepository.findOne(email);
	}

	@Override
	@Transactional(rollbackFor = UserNotFound.class)
	public User update(User user) throws UserNotFound {
		User updateTargetUser = findById(user.getEmail());
		if (updateTargetUser == null) {
			throw new UserNotFound();
		}

		updateTargetUser.setFirstName(user.getFirstName());
		updateTargetUser.setLastName(user.getLastName());
		updateTargetUser.setEnabled(user.getEnabled());
		updateTargetUser.setPassword(user.getPassword());

		return updateTargetUser;
	}

	@Override
	@Transactional
	public List<User> findAll() {
		return userRepository.findAll();
	}
}
