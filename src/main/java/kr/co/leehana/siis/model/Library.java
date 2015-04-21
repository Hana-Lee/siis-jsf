package kr.co.leehana.siis.model;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "LIBRARY")
public class Library {

	@Id
	@Column(name = "LIBRARY_CODE", unique = true, nullable = false)
	private String code;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "URL")
	private String url;

	@Column(name = "TRAIT")
	private String trait;

	@Column(name = "REGION")
	private String region;

	@OneToMany(mappedBy = "library", fetch = FetchType.LAZY)
	private List<Book> books;
}
