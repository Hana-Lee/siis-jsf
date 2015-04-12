import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
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
    @Ignore
    public void testHtmlParsing() throws UnsupportedEncodingException {
        String baseText = "detailurl";
        String testUrl1 = "http://115.84.165.14/EngineGateway/Detail.lsm?id=1428394645702&dbnum=42551&seq=9&attr=1&detailurl=http%3A%2F%2Flibrary.gangnam.go.kr%2Fsearch%2Fdetail%2FCATCAZ000000165430%3FmainLink%3D%2Fsearch%2Fcaz%26briefLink%3D%2Fsearch%2Fcaz%2Fresult%3Flmtsn%3D000000000003_A_lmtsn%3D000000000006_A_cpp%3D10_A_range%3D000000000021_A__inc%3Don_A__inc%3Don_A__inc%3Don_A__inc%3Don_A__inc%3Don_A__inc%3Don_A_lmt0%3DTOTAL_A_rf%3D_A_lmt1%3DTOTAL_A_si%3DTOTAL_A_si%3D2_A_si%3D3_A_weight2%3D0.5_A_weight0%3D0.5_A_weight1%3D0.5_A_inc%3DTOTAL_A_q%3D%25EB%258F%2584%25EA%25B0%2580%25EB%258B%2588_A_q%3D_A_q%3D_A_b0%3Dand_A_b1%3Dand_A_lmtst%3DOR_A_lmtst%3DOR_A_rt%3D_A_st%3DKWRD_A_msc%3D10000";
        int detailUrlIndex = testUrl1.toLowerCase().indexOf(baseText);

        String testUrl = StringUtils.EMPTY;
        if (detailUrlIndex > -1) {
            testUrl = URLDecoder.decode(testUrl1.substring(detailUrlIndex + baseText.length() + 1), "UTF-8");
        } else {
            testUrl = testUrl1;
        }
        logger.info(detailUrlIndex);
        logger.info(testUrl);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> f = asyncHttpClient.prepareGet(testUrl).execute();

        String htmlDocument = "";
        try {
            Response r = f.get();
            htmlDocument = r.getResponseBody();
            InputStream stream = r.getResponseBodyAsStream();
            CharsetDetector detector = new CharsetDetector();
            detector.setText(htmlDocument.getBytes());
            for (CharsetMatch match : detector.detectAll()) {
                logger.info(match.getName());
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        Document dom = null;
        if (StringUtils.isNotBlank(htmlDocument)) {
            dom = Jsoup.parse(htmlDocument);
        }

        if (dom != null) {
            Elements tableElems = dom.select("table");

            for(Element tableElem : tableElems) {
                boolean hasContent = tableElem.toString().contains(searchText3);
                if (hasContent) {
                    Elements tdElems = tableElem.select("tbody td");
                    for (Element td : tdElems) {
                        logger.info(td.toString());
                    }
                } else {
                    logger.error("No data found");
                }
            }
        }
    }

    @Test
    @Ignore
    public void testHtmlParsing2() {
// String testUrl = "http://gnlib.sen.go.kr/Book-Info.do?rec_key=5179499254&st_code=KEY_5179499252&lib_code=111003&searchType=search-simple&boardId=GN_D01";
        String testUrl = "http://www.gangbuklib.seoul.kr/youth/01.search/?m=0102&mode=v&RK=302871402&bGubn=M&LibCD=MB";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> f2 = asyncHttpClient.prepareGet(testUrl).execute();

        String htmlDocument = "";
        try {
            Response r = f2.get();
            htmlDocument = r.getResponseBody();
            InputStream stream = r.getResponseBodyAsStream();
            CharsetDetector detector = new CharsetDetector();
            detector.setText(htmlDocument.getBytes());
            for (CharsetMatch match : detector.detectAll()) {
                logger.info(match.getName());
            }
            htmlDocument = StringUtils.toEncodedString(htmlDocument.getBytes("latin1"), Charset.forName("UTF-8"));
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        Document dom = null;
        if (StringUtils.isNotBlank(htmlDocument)) {
            dom = Jsoup.parse(htmlDocument);
        }

        if (dom != null) {
            Elements tableElems = dom.select("table");

            for (Element tableElem : tableElems) {
                boolean hasContent = tableElem.toString().contains(searchText4);
                if (hasContent) {
                    Elements tdElems = tableElem.select("tbody td");
                    for (Element td : tdElems) {
                        logger.info(td.toString());
                    }
                } else {
                    logger.error("No data found");
                }
            }
        }
    }

    @Test
    @Ignore
    public void testHtmlParsing3() {
        String testUrl = "http://gnlib.sen.go.kr/Book-Info.do?rec_key=5179499254&st_code=KEY_5179499252&lib_code=111003&searchType=search-simple&boardId=GN_D01";
// String testUrl = "http://www.gangbuklib.seoul.kr/youth/01.search/?m=0102&mode=v&RK=302871402&bGubn=M&LibCD=MB";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> f2 = asyncHttpClient.prepareGet(testUrl).execute();

        String htmlDocument = "";
        try {
            Response r = f2.get();
            htmlDocument = r.getResponseBody();
            InputStream stream = r.getResponseBodyAsStream();
            CharsetDetector detector = new CharsetDetector();
            detector.setText(htmlDocument.getBytes());
            for (CharsetMatch match : detector.detectAll()) {
                logger.info(match.getName());
            }
// htmlDocument = StringUtils.toEncodedString(htmlDocument.getBytes("8859_1"), Charset.forName("UTF-8"));
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        Document dom = null;
        if (StringUtils.isNotBlank(htmlDocument)) {
            dom = Jsoup.parse(htmlDocument);
        }

        if (dom != null) {
            Elements tableElems = dom.select("table");

            for (Element tableElem : tableElems) {
                boolean hasContent = tableElem.toString().contains(searchText3);
                if (hasContent) {
                    Elements tdElems = tableElem.select("tbody td");
                    for (Element td : tdElems) {
                        logger.info(td.toString());
                    }
                } else {
                    logger.error("No data found");
                }
            }
        }
    }

    @Test
    @Ignore
    public void testGetJsonData() throws IOException, SQLException, ClassNotFoundException {
// String searchWord = "%EB%8F%84%EA%B0%80%EB%8B%88";
        String searchWord = "(映畵論述) 영화 속 논술";
        long startTime = System.currentTimeMillis();
        String searchUrlTemplate = "http://meta.seoul.go.kr/libstepsV5_seoul/ctrl/search.lsm?category1=%s&category2=&category3=&text1=%s&text2=&text3=&as1=&as2=&as3=&year1=&year2=&dbnum=%s&recstart=%s&display=%s&target=&op=&op2=&sort=&id=%s&skey=798&ckey=0&host=115.84.165.14&_=1428567948467";

        String firstUrl = "http://meta.seoul.go.kr/libstepsV5_seoul/index.php?skey=798";
        String jsonUrl = "http://meta.seoul.go.kr/libstepsV5_seoul/get_info.php?item=category&mode=main&_=1428480458156&skey=798";

        StringBuffer sb = new StringBuffer();
        sb.append("sess_lang=ko;");
        sb.append("sess_skey=798;");
        sb.append("sess_ckey=0;");
        sb.append("sess_host=115.84.165.14;");
        sb.append("PHPSESSID=63e1b3a95771d61725d09b2b73ec5285");


        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://mysql56.cx5fj3gwirpq.ap-northeast-1.rds.amazonaws.com/siis", "siis", "b3e12731050d85cb36c7d54b5fa538fabecdf076");
        String query = "select code, name, url from library where status = 'enable' and name not like '%불가%' and name not like '%어린이%' and code not in ('1081', '45511', '45311')";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        File file = new File("result.txt");
        if (!file.exists()) {
            boolean createResult = file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file.getName(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36";
        String startIndex = "1";
        String pageCount = "10";
        String categoryNumber = "1"; // 책제목
//        String categoryNumber = "4"; // 저자이름
        int totalCount = 0;
        while (rs.next()) {
            int randomNumber = (int) ((Math.random() * 100) + 1);
            long currentTime = System.currentTimeMillis();
            String newId = String.valueOf(currentTime + randomNumber);
            String libraryCode = rs.getString("code");

            String searchUrl = String.format(searchUrlTemplate, categoryNumber, URLEncoder.encode(searchWord, "UTF-8"), libraryCode, startIndex, pageCount, newId);

            URLConnection urlConnection = new URL(searchUrl).openConnection();
            urlConnection.setRequestProperty("Cookie", sb.toString());
            urlConnection.setRequestProperty("User-Agent", userAgent);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer resultBuffer = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                resultBuffer.append(inputLine);
            }
            br.close();

            String result = resultBuffer.toString();
            Document doc = Jsoup.parse(result, "", Parser.xmlParser());
            Elements elems = doc.select("resultinfo");
            String total = StringUtils.EMPTY;
            if (elems.first() != null) {
                total = elems.first().attr("total");
            }

            if (StringUtils.isBlank(total) || !StringUtils.isNumeric(total)) {
                total = "0";
            }
            total = total.trim();
            totalCount += Integer.valueOf(total);
            if (Integer.valueOf(total) > 0) {
                logger.info(rs.getString("url"));
                logger.info(rs.getString("name"));
                logger.info("Search count : " + total);
            }
            bufferedWriter.write(resultBuffer.toString());
        }
        logger.info(totalCount);
        bufferedWriter.close();
        fileWriter.close();
        rs.close();
        preparedStatement.close();
        connection.close();

        long endTime = System.currentTimeMillis();

        Date timeDiff = new Date(endTime - startTime); // compensate for 1h in millis
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss.SSS");
        logger.info("Duration millisecond : " + (endTime - startTime));
        logger.info("Duration: " + timeFormat.format(timeDiff));
    }
}
