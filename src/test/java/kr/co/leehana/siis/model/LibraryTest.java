package kr.co.leehana.siis.model;

import kr.co.leehana.siis.config.WebAppConfig;

import kr.co.leehana.siis.service.LibraryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lee Hana on 2015-04-21 오후 3:11.
 *
 * @author Lee Hana
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebAppConfig.class)
public class LibraryTest {

	@Autowired
	private LibraryService libraryService;

	@Test
	public void testGetLibrary() {
		Library library = libraryService.findById("1791");
	}
}