package kr.co.leehana.siis.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Date;
import java.util.List;

import kr.co.leehana.siis.config.WebAppConfigDevProfile;
import kr.co.leehana.siis.exception.UserNotFound;
import kr.co.leehana.siis.model.User;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lee Hana on 2015-04-27 오전 11:59.
 *
 * @author Lee Hana
 * @since 2015-04-27 오전 11:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebAppConfigDevProfile.class)
@ActiveProfiles(value = "dev")
@Transactional
public class UserServiceTest {

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserService userService;

	private final String userEmail = "voyaging@naver.com";

	@Test
	public void testCreateUser() throws UserNotFound {
		deleteAllUser();

		User user = new User();
		user.setEmail("voyaging@daum.net");
		user.setFirstName("두나");
		user.setLastName("박");
		user.setPassword("gksk8192");
		user.setEnabled(true);
		user.setCreated(new Date(System.currentTimeMillis()));

		userService.create(user);

		List<User> users = userService.findAll();

		assertThat(users, is(not(nullValue(List.class))));
		assertThat(users.size(), is(1));
	}

	private void deleteAllUser() throws UserNotFound {
		List<User> users = userService.findAll();
		for (User user : users) {
			userService.delete(user.getEmail());
		}
	}

	@Test
	public void testUpdateUser() throws UserNotFound {
		User user = userService.findById(userEmail);
		assertThat(user, is(not(nullValue(User.class))));
		assertThat(user.getPassword(), is(not(StringUtils.EMPTY)));
		assertThat(user.getFirstName(), is("하나"));

		user.setFirstName("연경");

		userService.update(user);

		User user2 = userService.findById(userEmail);
		assertThat(user2.getFirstName(), is("연경"));
	}

	@Test
	public void testDeleteUser() throws UserNotFound {
		User user = userService.findById(userEmail);
		assertThat(user, is(not(nullValue(User.class))));
		assertThat(user.getPassword(), is(not(StringUtils.EMPTY)));

		userService.delete(user.getEmail());

		User user2 = userService.findById(userEmail);
		assertThat(user2, is(nullValue(User.class)));
	}

	@Test
	public void testFindAllUser() {
		List<User> users = userService.findAll();
		assertThat(users, is(not(nullValue(List.class))));
		assertThat(users.size(), is(1));
	}

	@Test(expected = UserNotFound.class)
	public void testUpdateWithUserNotFoundException() throws UserNotFound {
		User user = userService.findById("voyaging@naver.com");
        user.setEmail("voyaging@daum.net");
		userService.update(user);
	}

    @Test(expected = UserNotFound.class)
    public void testDeleteWithUserNotFoundException() throws UserNotFound {
        userService.delete("voyaging@daum.net");
    }
}
