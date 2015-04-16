package kr.co.leehana.siis.parser;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import kr.co.leehana.siis.concurrent.BookSearcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.apache.tika.parser.txt.UniversalEncodingDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Lee Hana on 2015-04-07 오후 6:14.
 *
 * @author Lee Hana
 */
public class HtmlParser {

    private Log logger = LogFactory.getLog(getClass());

    private final String searchText1 = "반납기한";
    private final String searchText2 = "반납예정";
    private final String searchText3 = "도서상태";
    private final String searchText4 = "도서<br>상태";

    @Test
    public void testGetJsonData() throws IOException, SQLException, ClassNotFoundException {
        String searchWord = "마이크로코스모스";
        long startTime = System.currentTimeMillis();
        String searchUrlTemplate = "http://meta.seoul.go.kr/libstepsV5_seoul/ctrl/search.lsm?category1=%s&category2=&category3=&text1=%s&text2=&text3=&as1=&as2=&as3=&year1=&year2=&dbnum=%s&recstart=%s&display=%s&target=&op=&op2=&sort=&id=%s&skey=798&ckey=0&host=115.84.165.14&_=1428567948467";

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://mysql56.cx5fj3gwirpq.ap-northeast-1.rds.amazonaws.com/siis", "siis", "b3e12731050d85cb36c7d54b5fa538fabecdf076");
        String query = "select code, name, url from library where status = 'enable' and name not like '%불가%' and name not like '%어린이%' and code not in ('1081', '45511', '45311') and category like '%1%' and category like '%4%'";
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

        while (!excutor.isTerminated()){
        }

        long endTime = System.currentTimeMillis();

        Date timeDiff = new Date(endTime - startTime - 3600000); // compensate for 1h in millis
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss.SSS");
        logger.info("Duration millisecond : " + (endTime - startTime));
        logger.info("Duration: " + timeFormat.format(timeDiff));
    }
}
