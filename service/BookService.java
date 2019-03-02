package cn.llz.chapter05.service;

import cn.llz.chapter05.entity.Book;
import cn.llz.chapter05.mapper.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    BookDao bookDao;

    public void addBook(Book book){
        bookDao.save(book);
    }

    public Page<Book> getBooByPage(Pageable pageable){
        return bookDao.findAll(pageable);
    }

    public List<Book> getBooksByAuthorStartingWith(String author){
        return bookDao.getBooksByAuthorStratingWith(author);
    }

    public List<Book> getBooksByPriceGreaterThan(Float price){
        return bookDao.getBooksByPriceGreaterThan(price);
    }

    public Book getMaxIdBook(){
        return bookDao.getMaxIdBook();
    }

    public List<Book>getBookByIdAndAuthor(String author,Integer id){
        return bookDao.getBooksByIdAndAuthor(author,id);
    }

    public List<Book>getBooksByIdAndName(String name,Integer id){
        return bookDao.getBooksByIdAndName(name,id);
    }
}
