package kr.co.leehana.siis.model;

import kr.co.leehana.siis.concurrent.BookSearcher;
import kr.co.leehana.siis.type.SearchType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Lee Hana on 2015-04-04 오전 7:42.
 *
 * @author Lee Hana
 * @since 2015-04-04 오전 7:42
 */
public class BookTest {

    private static Connection connection;

    @BeforeClass
    public static void ready() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://mysql56.cx5fj3gwirpq.ap-northeast-1.rds.amazonaws.com/siis", "siis", "b3e12731050d85cb36c7d54b5fa538fabecdf076");
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

    private Log logger = LogFactory.getLog(getClass());

    private final String searchText1 = "반납기한";
    private final String searchText2 = "반납예정";
    private final String searchText3 = "도서상태";
    private final String searchText4 = "도서<br>상태";

    private final SearchType SEARCH_TYPE = SearchType.AUTHOR;

    @Test
    public void testGetJsonData() throws Exception {
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<String>> searcherList = new ArrayList<>();

//        String searchWord = "공지영";
        String searchWord = "추상적 사유의 위대한 힘";
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
                bookList.stream().filter(book -> book.getLibrary().contains("정독")).forEach(book -> logger.info(book.toString()));
            }
        }

        logger.info("Total Search Result Count : " + searchBookList.size());

        long endTime = System.currentTimeMillis();

        Date timeDiff = new Date(endTime - startTime - 3600000); // compensate for 1h in millis
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss.SSS");
        logger.info("Duration millisecond : " + (endTime - startTime));
        logger.info("Duration: " + timeFormat.format(timeDiff));
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

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> librariesCode = null;
        while (rs.next()) {
            if (librariesCode == null) {
                librariesCode = new ArrayList<>();
            }
            librariesCode.add(rs.getString("code"));
        }

        rs.close();
        preparedStatement.close();

        return librariesCode;
    }

    private String getLibraryName(String libraryCode) throws ClassNotFoundException, SQLException {
        String query = "SELECT name FROM library WHERE code = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, libraryCode);

        ResultSet rs = preparedStatement.executeQuery();
        boolean next = rs.next();
        String libraryName = null;
        if (next) {
            libraryName = rs.getString("name");
        }

        rs.close();
        preparedStatement.close();

        return libraryName;
    }

    private List<Book> makeBookList(String xmlSearchResult) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
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
