package kr.co.leehana.siis.model;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:19.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@Data
@Entity
@Table(name = "BOOK")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private long id;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "AUTHOR", nullable = false)
	private String author;

	@Column(name = "PUBLISHER")
	private String publisher;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LIBRARY_CODE", nullable = false)
	private Library library;

	@Column(name = "INFO_URL", length = 1000)
	private String infoUrl;

	@Column(name = "CALL_NO")
	private String callNo;

	@Column(name = "ISBN")
	private String isbnNumber;

	@Column(name = "YEAR")
	private String year;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", columnDefinition = "DATETIME", nullable = false)
	private Date created;
}