package kr.co.leehana.siis.service;

import kr.co.leehana.siis.concurrent.BookSearcher;
import kr.co.leehana.siis.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Lee Hana on 2015-04-16 오후 1:35.
 *
 * @author Lee Hana
 */
@Component
public class BookSearchServiceImpl implements BookSearchService {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public List<Book> searchBookByWord(String searchWord, String searchType) throws SQLException, UnsupportedEncodingException, ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        if (StringUtils.isBlank(searchWord) || searchWord.contains("이하나")) {
            return getLovelyBookList();
        } else {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            List<Future<String>> searcherList = new ArrayList<>();

            String searchUrlTemplate = "http://meta.seoul.go.kr/libstepsV5_seoul/ctrl/search.lsm" +
                    "?category1=%s&category2=&category3=&text1=%s&text2=&text3=&as1=&as2=&as3=&year1=&year2=" +
                    "&dbnum=%s&recstart=%s&display=%s&target=&op=&op2=&sort=&id=%s&skey=798" +
                    "&ckey=0&host=115.84.165.14&_=1428567948467";

            String startIndex = "1";
            String pageCount = "10";
            String categoryNumber = "0";

            List<String> librariesCode = getAllLibraryCode();
            for (String libraryCode : librariesCode) {
                String searchUrls = String.format(searchUrlTemplate, categoryNumber, URLEncoder.encode(searchWord, "UTF-8"),
                        libraryCode, startIndex, pageCount, makeSearchId());

                Callable<String> bookSearcher = new BookSearcher(libraryCode, searchUrls);
                searcherList.add(executor.submit(bookSearcher));
            }

            executor.shutdown();

            while (!executor.isTerminated()) {
            }

            List<Book> searchBookList = new ArrayList<>();
            for (Future<String> bookSearcher : searcherList) {
                String searchResult = bookSearcher.get();
                if (StringUtils.isNotBlank(searchResult)) {
                    List<Book> bookList = makeBookList(searchResult);
                    searchBookList.addAll(bookList);
                }
            }

            log.info("Total Search Result Count : " + searchBookList.size());

            long endTime = System.currentTimeMillis();

            Date timeDiff = new Date(endTime - startTime - 3600000); // compensate for 1h in millis
            SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss.SSS");
            log.info("Duration millisecond : " + (endTime - startTime));
            log.info("Duration: " + timeFormat.format(timeDiff));

            return searchBookList;
        }
    }

    private List<Book> getLovelyBookList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book newBook = new Book();
            newBook.setAuthor("이하나 : " + i);
            newBook.setPublisher("지구별");
            newBook.setIsbnNumber("781026-800127");
            newBook.setTitle("사랑해 공주님~♥ " + i);

            books.add(newBook);
        }

        return books;
    }

    private List<String> getAllLibraryCode() throws SQLException {
        String query = "SELECT code FROM library "
                + "WHERE status = 'enable' "
                + "AND code NOT IN ("
                + "'1081', '45511', '45311', '42921', '45081', '45061', '45511'"
                + "'2141','22761','42341','42371','42401','42411','42881','42901',"
                + "'43551','43591','43611','43621','43651','44011','44271','44331',"
                + "'44341','44681','44731','44871','44971','44991','45021','45061',"
                + "'45231','45241','45461','45571','45581','53781','38271','44901'"
                + ") AND category LIKE '%1%' AND category LIKE '%4%'";

        List<Map<String, Object>> result = jdbcOperations.queryForList(query);

        List<String> librariesCode = null;

        for (Map<String, Object> dataRow : result) {
            if (librariesCode == null) {
                librariesCode = new ArrayList<>();
            }
            librariesCode.add((String) dataRow.get("code"));
        }

        return librariesCode;
    }

    private String getLibraryName(String libraryCode) {
        String query = "SELECT name FROM library WHERE code = '" + libraryCode + "'";
        List<Map<String, Object>> result = jdbcOperations.queryForList(query);

        String libraryName = null;
        if (result != null && result.size() > 0) {
            libraryName = (String) result.get(0).get("name");
        }

        return libraryName;
    }

    private List<Book> makeBookList(String xmlSearchResult) throws UnsupportedEncodingException {
        Document doc = Jsoup.parse(xmlSearchResult, "", Parser.xmlParser());
        Elements recordElems = doc.select("record");

        Iterator<Element> recordElemIterator = recordElems.iterator();

        List<Book> newBookList = null;
        String libraryCode = recordElems.attr("dbnum");
        while (recordElemIterator.hasNext()) {
            Book newBook = new Book();
            Elements fieldElems = recordElemIterator.next().select("field");

            for (Element fieldElem : fieldElems) {
                String fieldNameAttr = fieldElem.attr("name").toLowerCase();
                String content = fieldElem.select("content").text();
                switch (fieldNameAttr) {
                    case "title":
                        newBook.setTitle(content);
                        String infoUrl = fieldElem.select("url").text();
                        newBook.setInfoUrl(URLEncoder.encode(infoUrl, "UTF-8"));
                        break;
                    case "author":
                        newBook.setAuthor(content);
                        break;
                    case "callno":
                        newBook.setCallNo(content);
                        break;
                    case "date":
                    case "year":
                        if (StringUtils.isBlank(newBook.getYear()))
                            newBook.setYear(content);
                        break;
                    case "publication":
                    case "publisher":
                        if (StringUtils.isBlank(newBook.getPublisher()))
                            newBook.setPublisher(content);
                        break;
                    case "location":
                        newBook.setLibrary(content);
                        break;
                }

                if (StringUtils.isNotBlank(newBook.getTitle()) && StringUtils.isBlank(newBook.getLibrary())) {
                    newBook.setLibrary(getLibraryName(libraryCode));
                }
            }

            if (StringUtils.isNotBlank(newBook.getTitle())) {
                if (newBookList == null) {
                    newBookList = new ArrayList<>();
                }

                newBookList.add(newBook);
            }
        }

        return newBookList;
    }

    private String makeSearchId() {
        int randomNumber = (int) ((Math.random() * 100) + 1);
        long currentTime = System.currentTimeMillis();
        return String.valueOf(currentTime + randomNumber);
    }
}
