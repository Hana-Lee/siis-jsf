package kr.co.leehana.siis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.model.Library;

@Service
public class HistoryServiceImpl implements HistoryService {

	@Autowired
	private JdbcOperations jdbcOperations;

	@Transactional
	@Override
	public void writeSelectedLibrary(Library library) {
		if (library != null) {
			String query = "INSERT INTO fav_library (libraryCode) VALUES (?)";
			jdbcOperations.update(query, new Object[] { library.getCode() });
		}

	}

	@Transactional
	@Override
	public void writeSearchResult(String searchWord, List<Book> searchResult) {
		if (searchResult != null) {
			String query = "INSERT INTO history (searchWord, title, author, callNo, infoUrl, libraryCode, publisher, year) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			for (Book book : searchResult) {
				jdbcOperations.update(
						query,
						new Object[] {
								searchWord.trim().replaceAll("\\s+", ""),
								book.getTitle(), book.getAuthor(),
								book.getCallNo(), book.getInfoUrl(),
								book.getLibrary().getCode(),
								book.getPublisher(), book.getYear() });
			}
		}
	}

	@Override
	public List<Book> getSearchResultHistory(String searchWord) {
		List<Book> books = null;

		if (StringUtils.isNotBlank(searchWord)) {
			String query = "SELECT title, author, callNo, infoUrl, libraryCode, publisher, year FROM history WHERE searchWord = ?";
			List<Map<String, Object>> result = jdbcOperations.queryForList(
					query, new Object[] { searchWord });

			if (result != null && result.size() > 0) {
				books = new ArrayList<>();
				Book newBook = null;
				Library library = null;
				for (Map<String, Object> dataRow : result) {
					newBook = new Book();
					newBook.setTitle(String.valueOf(dataRow.get("title")));
					newBook.setAuthor(String.valueOf(dataRow.get("author")));
					newBook.setCallNo(String.valueOf(dataRow.get("callNo")));
					newBook.setInfoUrl(String.valueOf(dataRow.get("infoUrl")));
					newBook.setPublisher(String.valueOf(dataRow
							.get("publisher")));
					newBook.setYear(String.valueOf(dataRow.get("year")));

					if (library == null) {
						library = getLibrary(dataRow.get("libraryCode"));
					}

					newBook.setLibrary(library);

					books.add(newBook);
				}
			}
		}
		return books;
	}

	private Library getLibrary(Object libraryCode) {
		if (Objects.nonNull(libraryCode)) {
			String query = "SELECT name FROM library WHERE code = ?";
			String result = jdbcOperations.queryForObject(query,
					new Object[] { libraryCode }, String.class);

			Library library = null;
			if (StringUtils.isNotBlank(result)) {
				library = new Library();
				library.setCode(String.valueOf(libraryCode));
				library.setName(result);
			}

			return library;
		} else {
			return null;
		}
	}

}
