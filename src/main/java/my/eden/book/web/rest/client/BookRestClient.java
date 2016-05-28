package my.eden.book.web.rest.client;

import com.google.common.collect.Lists;
import my.eden.book.web.rest.client.vo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by eden90267 on 2016/5/27.
 */
@Service
public class BookRestClient {

    @Value("${book-rest.url}")
    private String baseUri;

    @Autowired
    private RestTemplate restTemplate;

    public List<Book> findAll() {
        ParameterizedTypeReference<PagedResources<Book>> responseTypeRef = new ParameterizedTypeReference<PagedResources<Book>>() {
        };
        ResponseEntity<PagedResources<Book>> responseEntity = restTemplate.exchange(baseUri, HttpMethod.GET, null, responseTypeRef);
        return Lists.newArrayList(responseEntity.getBody().getContent());
    }

    public Book findOne(Long id) {
        String resourceUri = baseUri + "/" + id;
        return restTemplate.getForObject(resourceUri, Book.class);
    }

    public boolean hasDataByBookName(String bookName) {
        String resourceUri = baseUri + "/search/findCountByBookName?bookName=" + bookName;
        return restTemplate.getForObject(resourceUri, Long.class) > 0;
    }

    public Book create(Book book) {
        HttpEntity<Book> request = new HttpEntity<>(book);
        return restTemplate.postForObject(baseUri, request, Book.class);
    }

    public void update(Book book) {
        String resourceUri = baseUri + "/" + book.getId();
        HttpEntity<Book> request = new HttpEntity<>(book);
        restTemplate.exchange(resourceUri, HttpMethod.PUT, request, Void.class);
    }

    public void delete(Book book) {
        String resourceUri = baseUri + "/" + book.getId();
        restTemplate.delete(resourceUri);
    }

}
