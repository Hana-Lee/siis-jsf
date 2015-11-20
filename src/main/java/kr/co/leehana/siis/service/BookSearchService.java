package kr.co.leehana.siis.service;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lee Hana on 2015-04-16 오후 1:34.
 *
 * @author Lee Hana
 */
public interface BookSearchService {

	List<Book> searchBookByWord(String searchWord, String searchType, List<Library> libraries)
			throws UnsupportedEncodingException, SQLException, ClassNotFoundException, ExecutionException, InterruptedException;
}
