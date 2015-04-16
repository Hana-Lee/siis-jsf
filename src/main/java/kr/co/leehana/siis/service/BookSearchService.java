package kr.co.leehana.siis.service;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.type.SearchType;

import java.util.List;

/**
 * Created by Lee Hana on 2015-04-16 오후 1:34.
 *
 * @author Lee Hana
 */
public interface BookSearchService {

    public List<Book> searchBookByWord(String searchWord, SearchType searchType);
}
