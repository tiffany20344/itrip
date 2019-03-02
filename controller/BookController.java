package cn.llz.chapter05.controller;

import cn.llz.chapter05.entity.Book;
import cn.llz.chapter05.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class BookController {

    @Resource
    BookService bookService;

    @GetMapping("findAll")
    public void findAll(){
        PageRequest pageable = PageRequest.of(2,3);
        Page<Book> page = bookService.getBookByPage(pageable);
        System.out.println("总页数："+page.getTotalPages());
        System.out.println("总记录数："+page.getTotalElements());
        System.out.println("查询结果："+page.getContent());
        System.out.println("当前页数："+(page.getNumber()+1));
        System.out.println("当前页记录数："+page.getNumberOfElements());
        System.out.println("每页记录数："+page.getSize());
    }

    @GetMapping("search")
    public void search(){
        List<Book> b1 = bookService.getBookByIdAndAuthor("鲁迅",7);
        List<Book> b2 = bookService.getBooksByAuthorStartingWith("吴");
        List<Book> b3 = bookService.getBooksByIdAndName("西",8);
        List<Book> b4 = bookService.getBooksByPriceGreaterThan(30F);
        Book b = bookService.getMaxIdBook();
        System.out.println("b1:"+b1);
        System.out.println("b2:"+b2);
        System.out.println("b3:"+b3);
        System.out.println("b4:"+b4);
        System.out.println("b:"+b);
    }

    @GetMapping("save")
    public void save(){
        Book book = new Book();
        book.setAuthor("鲁迅");
        book.setName("呐喊");
        book.setPrice(23F);
        bookService.addBook(book);
    }
}
