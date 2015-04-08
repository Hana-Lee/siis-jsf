package model;

import lombok.Data;

/**
 * Created by Lee Hana on 2015-04-03 오후 5:19.
 *
 * @author Lee Hana
 * @since 2015-04-03 오후 5:19.
 */
@Data
public class Book {
    private String title;
    private String author;
    private String publisher;
    private String isbnNumber;
}