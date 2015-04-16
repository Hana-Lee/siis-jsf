package kr.co.leehana.siis.model;

import kr.co.leehana.siis.concurrent.BookSearcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URLEncoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lee Hana on 2015-04-04 오전 7:42.
 * @author Lee Hana
 * @since 2015-04-04 오전 7:42
 */
public class BookTest {

    @Test
    public void testBookModel() {
        System.out.println("Book Model Test") ;
    }

    private Log logger = LogFactory.getLog(getClass());

    private final String searchText1 = "반납기한";
    private final String searchText2 = "반납예정";
    private final String searchText3 = "도서상태";
    private final String searchText4 = "도서<br>상태";

    @Test
    public void testGetJsonData() throws Exception {
        String searchWord = "마이크로코스모스";
        long startTime = System.currentTimeMillis();
        String searchUrlTemplate = "http://meta.seoul.go.kr/libstepsV5_seoul/ctrl/search.lsm?category1=%s&category2=&category3=&text1=%s&text2=&text3=&as1=&as2=&as3=&year1=&year2=&dbnum=%s&recstart=%s&display=%s&target=&op=&op2=&sort=&id=%s&skey=798&ckey=0&host=115.84.165.14&_=1428567948467";

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://mysql56.cx5fj3gwirpq.ap-northeast-1.rds.amazonaws.com/siis", "siis", "b3e12731050d85cb36c7d54b5fa538fabecdf076");
        String query = "SELECT code, name, url FROM library WHERE status = 'enable' AND name NOT LIKE '%불가%' AND name NOT LIKE '%어린이%' AND code NOT IN ('1081', '45511', '45311') AND category LIKE '%1%' AND category LIKE '%4%'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        String startIndex = "1";
        String pageCount = "10";
        String categoryNumber = "1";

        int totalCount = 0;
        ExecutorService excutor = Executors.newFixedThreadPool(5);
        int count = 0;
        while (rs.next()) {
            int randomNumber = (int) ((Math.random() * 100) + 1);
            long currentTime = System.currentTimeMillis();
            String newId = String.valueOf(currentTime + randomNumber);
            String libraryCode = rs.getString("code");

            String searchUrl = String.format(searchUrlTemplate, categoryNumber, URLEncoder.encode(searchWord, "UTF-8"), libraryCode, startIndex, pageCount, newId);

            Runnable worker = new BookSearcher(searchUrl, libraryCode);

            excutor.execute(worker);
        }

        rs.close();
        preparedStatement.close();
        connection.close();

        excutor.shutdown();

        while (!excutor.isTerminated()) {
        }

        long endTime = System.currentTimeMillis();

        Date timeDiff = new Date(endTime - startTime - 3600000); // compensate for 1h in millis
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss.SSS");
        logger.info("Duration millisecond : " + (endTime - startTime));
        logger.info("Duration: " + timeFormat.format(timeDiff));
    }
}
