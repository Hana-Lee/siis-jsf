package kr.co.leehana.siis.service;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.type.SearchType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee Hana on 2015-04-16 오후 1:35.
 *
 * @author Lee Hana
 */
@Component
public class BookSearchServiceImpl implements BookSearchService {

    @Override
    public List<Book> searchBookByWord(String searchWord, SearchType searchType) {

        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book newBook = new Book();
            if (searchType == SearchType.NAME) {
                newBook.setAuthor("이하나 : " + i);
                newBook.setPublisher(searchWord);
            } else if (searchType == SearchType.AUTHOR) {
                newBook.setAuthor(searchWord + " : " + i);
                newBook.setPublisher("북드리망");
            }
            newBook.setIsbnNumber("781026-800127");
            newBook.setName("사랑해 공주님~♥ " + i);

            books.add(newBook);
        }

        return books;
    }
}
