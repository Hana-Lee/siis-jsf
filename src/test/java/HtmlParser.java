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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Lee Hana on 2015-04-07 오후 6:14.
 *
 * @author Lee Hana
 */
public class HtmlParser {

    private Log logger = LogFactory.getLog(getClass());

    @Test
    public void testHtmlParsing() {
        String testUrl1 = "http://115.84.165.14/EngineGateway/Detail.lsm?id=1428394645702&dbnum=42551&seq=9&attr=1&detailurl=http%3A%2F%2Flibrary.gangnam.go.kr%2Fsearch%2Fdetail%2FCATCAZ000000165430%3FmainLink%3D%2Fsearch%2Fcaz%26briefLink%3D%2Fsearch%2Fcaz%2Fresult%3Flmtsn%3D000000000003_A_lmtsn%3D000000000006_A_cpp%3D10_A_range%3D000000000021_A__inc%3Don_A__inc%3Don_A__inc%3Don_A__inc%3Don_A__inc%3Don_A__inc%3Don_A_lmt0%3DTOTAL_A_rf%3D_A_lmt1%3DTOTAL_A_si%3DTOTAL_A_si%3D2_A_si%3D3_A_weight2%3D0.5_A_weight0%3D0.5_A_weight1%3D0.5_A_inc%3DTOTAL_A_q%3D%25EB%258F%2584%25EA%25B0%2580%25EB%258B%2588_A_q%3D_A_q%3D_A_b0%3Dand_A_b1%3Dand_A_lmtst%3DOR_A_lmtst%3DOR_A_rt%3D_A_st%3DKWRD_A_msc%3D10000";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> f = asyncHttpClient.prepareGet(testUrl1).execute();

        String htmlDocument = "";
        try {
            Response r = f.get();
            htmlDocument = r.getResponseBody();
            htmlDocument = new String(htmlDocument.getBytes("8859_1"), "UTF-8");
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

        String testUrl2 = "http://gnlib.sen.go.kr/Book-Info.do?rec_key=5179499254&st_code=KEY_5179499252&lib_code=111003&searchType=search-simple&boardId=GN_D01";
        Future<Response> f2 = asyncHttpClient.prepareGet(testUrl2).execute();

        htmlDocument = "";
        try {
            Response r = f2.get();
            htmlDocument = r.getResponseBody();
            htmlDocument = new String(htmlDocument.getBytes("8859_1"), "UTF-8");
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

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
}
