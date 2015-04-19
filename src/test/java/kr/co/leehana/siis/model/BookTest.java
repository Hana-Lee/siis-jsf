package kr.co.leehana.siis.model;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kr.co.leehana.siis.concurrent.BookSearcher;
import kr.co.leehana.siis.config.WebAppConfig;
import kr.co.leehana.siis.service.HistoryService;
import kr.co.leehana.siis.type.SearchType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by Lee Hana on 2015-04-04 오전 7:42.
 *
 * @author Lee Hana
 * @since 2015-04-04 오전 7:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebAppConfig.class, loader = AnnotationConfigContextLoader.class)
public class BookTest {

	@Autowired
	private JdbcOperations jdbcOperations;
	@Autowired
	private HistoryService historyService;
	private final Log log = LogFactory.getLog(getClass());

	private static Connection connection;

	@BeforeClass
	public static void ready() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager
				.getConnection(
						"jdbc:mysql://mysql56.cx5fj3gwirpq.ap-northeast-1.rds.amazonaws.com/siis",
						"siis", "b3e12731050d85cb36c7d54b5fa538fabecdf076");
	}

	@AfterClass
	public static void close() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	@Test
	public void testBookModel() {
		System.out.println("Book Model Test");
	}

	private final String searchText1 = "반납기한";
	private final String searchText2 = "반납예정";
	private final String searchText3 = "도서상태";
	private final String searchText4 = "도서<br>상태";

	private final SearchType SEARCH_TYPE = SearchType.AUTHOR;

	@Test
	public void testGetJsonData() throws Exception {
		long startTime = System.currentTimeMillis();

		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<List<Book>>> searcherList = new ArrayList<>();

		String searchUrlTemplate = "http://meta.seoul.go.kr/libstepsV5_seoul/ctrl/search.lsm"
				+ "?category1=%s&category2=&category3=&text1=%s&text2=&text3=&as1=&as2=&as3=&year1=&year2="
				+ "&dbnum=%s&recstart=%s&display=%s&target=&op=&op2=&sort=&id=%s&skey=798"
				+ "&ckey=0&host=115.84.165.14&_=1428567948467";

		String startIndex = "1";
		String pageCount = "10";
		String categoryNumber = "1";

		String searchWord = "괴델,에셔,바흐";

		Map<String, String> librariesCodeName = getAllLibraryCode();
		for (String libraryCode : librariesCodeName.keySet()) {
			String searchUrls = String.format(searchUrlTemplate,
					categoryNumber, URLEncoder.encode(searchWord, "UTF-8"),
					libraryCode, startIndex, pageCount, makeSearchId());

			Callable<List<Book>> bookSearcher = new BookSearcher(libraryCode,
					librariesCodeName.get(libraryCode), searchUrls, searchWord,
					historyService);
			searcherList.add(executor.submit(bookSearcher));
		}

		executor.shutdown();

		while (!executor.isTerminated()) {
		}

		List<Book> searchBookList = new ArrayList<>();

		for (Future<List<Book>> bookSearcher : searcherList) {
			List<Book> searchResult = bookSearcher.get();
			if (searchResult != null) {
				searchBookList.addAll(searchResult);
			}
		}

		log.info("Total Search Result Count : " + searchBookList.size());

		long endTime = System.currentTimeMillis();

		Date timeDiff = new Date(endTime - startTime);
		SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss.SSS",
				Locale.KOREA);
		log.info("Duration millisecond : " + (endTime - startTime));
		log.info("Duration: " + timeFormat.format(timeDiff));
	}

	private Map<String, String> getAllLibraryCode() throws SQLException {
		String query = "SELECT code, name FROM library "
				+ "WHERE status = 'enable' "
				+ "AND code NOT IN ("
				+ "'1081', '45511', '45311', '42921', '45081', '45061', '45511'"
				+ "'2141','22761','42341','42371','42401','42411','42881','42901',"
				+ "'43551','43591','43611','43621','43651','44011','44271','44331',"
				+ "'44341','44681','44731','44871','44971','44991','45021','45061',"
				+ "'45231','45241','45461','45571','45581','53781','38271','44901'"
				+ ") AND category LIKE '%1%' AND category LIKE '%4%'";

		List<Map<String, Object>> result = jdbcOperations.queryForList(query);

		Map<String, String> librariesCodeName = null;

		for (Map<String, Object> dataRow : result) {
			if (librariesCodeName == null) {
				librariesCodeName = new HashMap<>();
			}
			librariesCodeName.put(Objects.toString(dataRow.get("code")),
					Objects.toString(dataRow.get("name")));
		}

		return librariesCodeName;
	}

	private String makeSearchId() {
		int randomNumber = (int) ((Math.random() * 100) + 1);
		long currentTime = System.currentTimeMillis();
		return String.valueOf(currentTime + randomNumber);
	}
}
