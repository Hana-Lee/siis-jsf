package kr.co.leehana.siis.model;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Lee Hana on 2015-04-21 오전 10:10.
 *
 * @author Lee Hana
 */
@Data
@ToString(exclude = { "password" })
@Entity
@Table(name = "USER")
public class User {

	@Id
	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "ENABLED", nullable = false)
	private Boolean enabled;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", columnDefinition = "DATETIME")
	private Date created;

}
