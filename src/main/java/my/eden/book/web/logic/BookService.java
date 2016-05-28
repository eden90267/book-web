package my.eden.book.web.logic;

import my.eden.book.web.rest.client.BookRestClient;
import my.eden.book.web.rest.client.vo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by eden90267 on 2016/5/28.
 */
@Service
public class BookService {

    @Autowired
    private BookRestClient restClient;

    public List<Book> findAll() {
        return restClient.findAll();
    }

    public Book findOne(Long id) {
        return restClient.findOne(id);
    }

    public boolean hasDataByBookName(String bookName) {
        return restClient.hasDataByBookName(bookName);
    }

    public Book save(Book book) {
        return restClient.create(book);
    }

    public void update(Book book) {
        restClient.update(book);
    }

    public void delete(Book book) {
        restClient.delete(book);
    }


}
