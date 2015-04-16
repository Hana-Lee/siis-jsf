package kr.co.leehana.siis.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:19.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    private String name;
    private String author;
    private String publisher;

    @Column(name = "isbn")
    private String isbnNumber;
    private String year;
}