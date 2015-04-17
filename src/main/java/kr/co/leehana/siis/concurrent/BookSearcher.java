package kr.co.leehana.siis.concurrent;

import kr.co.leehana.siis.helper.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

/**
 * Created by Lee Hana on 2015-04-16 오후 5:07.
 *
 * @author Lee Hana
 */
public class BookSearcher implements Callable<String> {
    private Log log = LogFactory.getLog(getClass());

    private final String libraryCode;
    private final String searchUrl;

    public BookSearcher(String libraryCode, String searchUrl) {
        this.libraryCode = libraryCode;
        this.searchUrl = searchUrl;
    }

    @Override
    public String call() {
        int totalSearchCount = 0;
        String xmlSearchResult = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Search url :" + searchUrl);
                log.debug("Book Searching... : " + libraryCode);
            }

            URLConnection urlConnection = new URL(searchUrl).openConnection();
            urlConnection.setRequestProperty("Cookie", getCookieValue());
            urlConnection.setRequestProperty("User-Agent", UserAgent.SAFARI.getLabel());

            xmlSearchResult = getXmlSearchResult(urlConnection.getInputStream());

            totalSearchCount = findSearchResultCount(xmlSearchResult);
        } catch (IOException e) {
            log.error("Book Search Error.", e);
        }

        if (totalSearchCount > 0) {
            return xmlSearchResult;
        } else {
            return null;
        }
    }

    private String getCookieValue() {
        return "sess_lang=ko;sess_skey=798;sess_ckey=0;sess_host=115.84.165.14;PHPSESSID=63e1b3a95771d61725d09b2b73ec5285";
    }

    private String getXmlSearchResult(InputStream resultStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(resultStream));
        String inputLine;
        StringBuilder resultBuffer = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            resultBuffer.append(inputLine);
        }

        br.close();

        return resultBuffer.toString();
    }

    private int findSearchResultCount(String xmlSearchResult) {
        Document doc = Jsoup.parse(xmlSearchResult, "", Parser.xmlParser());
        Elements elems = doc.select("resultinfo");

        String resultTotal = null;

        if (elems.first() != null) {
            resultTotal = elems.first().attr("total");
        }

        if (StringUtils.isBlank(resultTotal) || !StringUtils.isNumeric(resultTotal)) {
            resultTotal = "0";
        }

        return Integer.parseInt(resultTotal.trim());
    }
}
