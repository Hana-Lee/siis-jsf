package kr.co.leehana.siis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "library")
public class Library {

	@Id
	private String code;
	private String name;
	private String category;
	private String status;
	private String checked;
	private String tooltip;
	private String url;
	private String supply;
}
