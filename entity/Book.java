package cn.llz.chapter05.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data //get、set
@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
@Entity(name = "t_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="book_name",nullable = false)
    private String name;
    private String author;
    private Float price;
    @Transient
    private String descrition;
}
