package kr.co.leehana.siis.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.Data;

import org.hibernate.annotations.NaturalId;

/**
 * Created by Lee Hana on 2015-04-21 오전 10:01.
 *
 * @author Lee Hana
 */
@Data
@Entity
@Table(name = "SEARCH_HISTORY")
public class SearchHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;

	@NaturalId(mutable = false)
	@Column(name = "SEARCH_WORD", unique = true, nullable = false)
	private String searchWord;

	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "HISTORY_ID")
	private List<Book> books;

	@ManyToOne()
	@JoinColumn(name = "USER_EMAIL", nullable = false)
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", columnDefinition = "DATETIME")
	private Date created;
}
