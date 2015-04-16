package kr.co.leehana.siis.concurrent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Lee Hana on 2015-04-16 오후 5:07.
 *
 * @author Lee Hana
 */
public class BookSearcher implements Runnable {
    private final String searchUrl;
    private final String libraryCode;

    private Log log = LogFactory.getLog(getClass());

    public BookSearcher(String searchUrl, String libraryCode) {
        this.searchUrl = searchUrl;
        this.libraryCode = libraryCode;
    }

    @Override
    public void run() {
        log.info("Start Book Searching.. : " + libraryCode);
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36";
        StringBuilder cookie = new StringBuilder();
        cookie.append("sess_lang=ko;");
        cookie.append("sess_skey=798;");
        cookie.append("sess_ckey=0;");
        cookie.append("sess_host=115.84.165.14;");
        cookie.append("PHPSESSID=63e1b3a95771d61725d09b2b73ec5285");

        try {
            URLConnection urlConnection = new URL(searchUrl).openConnection();
            urlConnection.setRequestProperty("Cookie", cookie.toString());
            urlConnection.setRequestProperty("User-Agent", userAgent);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder resultBuffer = new StringBuilder();
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

            log.info("Total Search Count : " + total + " code : " + libraryCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
