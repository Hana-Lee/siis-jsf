package kr.co.leehana.siis.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Lee Hana on 2015-04-24 오전 7:26.
 *
 * @author Lee Hana
 * @since 2015-04-24 오전 7:26
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ BookServiceTest.class, LibraryServiceTest.class, SearchHistoryServiceTest.class })
public class ServiceTestSuite {
}
