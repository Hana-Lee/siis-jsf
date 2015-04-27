package kr.co.leehana.siis.service;

import java.util.List;

import javax.annotation.Resource;

import kr.co.leehana.siis.model.Library;
import kr.co.leehana.siis.repository.LibraryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Lee Hana on 2015-04-21 오후 5:57.
 *
 * @author Lee Hana
 */
@Service
public class LibraryServiceImpl implements LibraryService {

	@Resource
	private LibraryRepository libraryRepository;

	@Override
	@Transactional
	public Library findById(String id) {
		return libraryRepository.findOne(id);
	}

	@Override
	@Transactional
	public List<Library> findAll() {
		return libraryRepository.findAll();
	}

	@Override
	public List<Library> findByStatusAndCategoryLike(String status,
			String category) {
		return libraryRepository.findByStatusAndCategoryLike(status, "%"
				+ category + "%");
	}

	@Override
	public List<Library> findByStatus(String status) {
		return libraryRepository.findByStatus(status);
	}
}
