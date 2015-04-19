package kr.co.leehana.siis.concurrent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import kr.co.leehana.siis.helper.UserAgent;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;
import kr.co.leehana.siis.service.HistoryService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

/**
 * Created by Lee Hana on 2015-04-16 오후 5:07.
 *
 * @author Lee Hana
 */
public class BookSearcher implements Callable<List<Book>> {
	private Log log = LogFactory.getLog(getClass());

	private final String libraryCode;
	private final String libraryName;
	private final String searchWord;
	private final String searchUrl;
	private final HistoryService historyService;

	public BookSearcher(String libraryCode, String libraryName,
			String searchUrl, String searchWord, HistoryService historyService) {
		this.libraryCode = libraryCode;
		this.searchUrl = searchUrl;
		this.libraryName = libraryName;
		this.searchWord = searchWord;
		this.historyService = historyService;
	}

	@Override
	public List<Book> call() {
		String xmlSearchResult = null;
		List<Book> result = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("Search url :" + searchUrl);
				log.debug("Book Searching... : " + libraryCode);
			}

			URLConnection urlConnection = new URL(searchUrl).openConnection();
			urlConnection.setRequestProperty("Cookie", getCookieValue());
			urlConnection.setRequestProperty("User-Agent",
					UserAgent.SAFARI.getLabel());

			xmlSearchResult = getXmlSearchResult(urlConnection.getInputStream());
			result = makeBookList(xmlSearchResult);
		} catch (IOException e) {
			log.error("Book Search Error.", e);
		}

		return result;
	}

	private String getCookieValue() {
		return "sess_lang=ko;sess_skey=798;sess_ckey=0;sess_host=115.84.165.14;PHPSESSID=63e1b3a95771d61725d09b2b73ec5285";
	}

	private String getXmlSearchResult(InputStream resultStream)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				resultStream, Charset.forName("UTF-8")));
		String inputLine;
		StringBuilder resultBuffer = new StringBuilder();
		while ((inputLine = br.readLine()) != null) {
			resultBuffer.append(inputLine);
		}

		br.close();

		return resultBuffer.toString();
	}

	private List<Book> makeBookList(String xmlSearchResult)
			throws UnsupportedEncodingException {
		Document doc = Jsoup.parse(xmlSearchResult, "", Parser.xmlParser());
		Elements elems = doc.select("resultinfo");

		String resultTotal = null;

		if (elems.first() != null) {
			resultTotal = elems.first().attr("total");
		}

		if (StringUtils.isBlank(resultTotal)
				|| !StringUtils.isNumeric(resultTotal)) {
			resultTotal = "0";
		}

		if (Integer.parseInt(resultTotal.trim()) > 0) {

			Elements recordElems = doc.select("record");

			Iterator<Element> recordElemIterator = recordElems.iterator();

			List<Book> newBookList = null;

			Library library = new Library();
			library.setCode(libraryCode);
			library.setName(libraryName);

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
						newBook.setInfoUrl(infoUrl);
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
					default:
						break;
					}
				}

				newBook.setLibrary(library);

				if (StringUtils.isNotBlank(newBook.getTitle())
						&& StringUtils.isNotBlank(newBook.getAuthor())) {
					if (newBookList == null) {
						newBookList = new ArrayList<>();
					}

					newBookList.add(newBook);
				}
			}

			if (newBookList != null && newBookList.size() > 0) {
				writeBookListToDatabase(newBookList);
			}

			return newBookList;
		} else {
			return null;
		}
	}

	private void writeBookListToDatabase(List<Book> bookList) {
		historyService.writeSearchResult(searchWord, bookList);
	}
}
