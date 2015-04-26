package kr.co.leehana.siis.service;

import java.util.List;

import javax.annotation.Resource;

import kr.co.leehana.siis.exception.BookNotFound;
import kr.co.leehana.siis.model.Book;
import kr.co.leehana.siis.repository.BookRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:18.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@Service
public class BookServiceImpl implements BookService {

	@Resource
	private BookRepository bookRepository;

	@Override
	@Transactional
	public Book create(Book book) {
		return bookRepository.save(book);
	}

	@Override
	@Transactional
	public List<Book> create(List<Book> books) {
		return bookRepository.save(books);
	}

	@Override
	@Transactional(rollbackFor = BookNotFound.class)
	public Book delete(long id) throws BookNotFound {
		Book deleteTargetBook = findById(id);
		if (deleteTargetBook == null) {
			throw new BookNotFound();
		}
		bookRepository.delete(deleteTargetBook);
		return deleteTargetBook;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	@Transactional(rollbackFor = BookNotFound.class)
	public Book update(Book book) throws BookNotFound {
		Book updateTargetBook = findById(book.getId());
		if (updateTargetBook == null) {
			throw new BookNotFound();
		}
		updateTargetBook.setTitle(book.getTitle());
		updateTargetBook.setAuthor(book.getAuthor());
		updateTargetBook.setCallNo(book.getCallNo());
		updateTargetBook.setInfoUrl(book.getInfoUrl());
		updateTargetBook.setIsbnNumber(book.getIsbnNumber());
		updateTargetBook.setLibrary(book.getLibrary());
		updateTargetBook.setPublisher(book.getPublisher());
		updateTargetBook.setYear(book.getYear());

		return updateTargetBook;
	}

	@Override
	@Transactional(readOnly = true)
	public Book findById(long id) {
		return bookRepository.findOne(id);
	}
}
