import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
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
                boolean hasContent = tableElem.toString().contains("도서상태");
                if (hasContent) {
                    Elements tdElems = tableElem.select("tbody td");
                    for (Element td : tdElems) {
                        logger.info(td.toString());
                    }
                }
            }
        }
    }

    @Test
    public void testHtmlParsing2() {
//        String testUrl = "http://gnlib.sen.go.kr/Book-Info.do?rec_key=5179499254&st_code=KEY_5179499252&lib_code=111003&searchType=search-simple&boardId=GN_D01";
        String testUrl = "http://www.gangbuklib.seoul.kr/youth/01.search/?m=0102&mode=v&RK=302871402&bGubn=M&LibCD=MB";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> f2 = asyncHttpClient.prepareGet(testUrl).execute();

        String htmlDocument = "";
        try {
            Response r = f2.get();
            htmlDocument = r.getResponseBody();
            htmlDocument = StringUtils.toEncodedString(htmlDocument.getBytes("8859_1"), Charset.forName("UTF-8"));
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
                    logger.info("No data found");
                }
            }
        }
    }
}
