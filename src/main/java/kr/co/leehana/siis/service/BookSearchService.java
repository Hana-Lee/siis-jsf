package kr.co.leehana.siis.service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kr.co.leehana.siis.model.Book;

/**
 * Created by Lee Hana on 2015-04-16 오후 1:34.
 *
 * @author Lee Hana
 */
public interface BookSearchService {

	List<Book> searchBookByWord(String searchWord, String searchType)
			throws UnsupportedEncodingException, SQLException,
			ClassNotFoundException, ExecutionException, InterruptedException;
}
