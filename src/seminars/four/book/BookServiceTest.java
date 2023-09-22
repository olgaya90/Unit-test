package com.book.unittest;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.book.model.AuthorRating;
import com.book.model.Book;
import com.book.repository.BookLibraryRepository;
import com.book.service.IBookLibraryService;

@ComponentScan(basePackageClasses = {
        IBookLibraryService.class,
        BookLibraryRepository.class
})
@RunWith(SpringRunner.class)
@DataJpaTest
public class BookServiceTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IBookLibraryService bookService;

    @Autowired
    private BookLibraryRepository bookRepository;

    @Test
    public void whenValidAuthorRating_thenAuthorsRatingsShouldBeFound() {

        ArrayList<String> author = new ArrayList<String>(){{add("author");}};
        ArrayList<String> category = new ArrayList<String>(){{add("category");}};
        Book book = new Book("1234", "title", "subtitle", "publisher",
                (long) 8000000, "description", 300, "url", "len", "link", 4.0, author, category);

        entityManager.persist(book);
        entityManager.flush();
        // when
        Set<AuthorRating> found = bookService.findAuthorsRatings();
        // then
        assertThat(found.size()).isEqualTo(1);
    }

    @Test
    public void whenInvalidAuthorRating_thenAuthorsRatingsShouldNotBeFound() {

        // when
        Set<AuthorRating> found = bookService.findAuthorsRatings();
        // then
        assertThat(found.size()).isEqualTo(0);
    }

    @Test
    public void whenValidBookIsbn_thenBookShouldBeFound() {

        String isbn = "12345";
        ArrayList<String> author = new ArrayList<String>(){{add("author");}};
        ArrayList<String> category = new ArrayList<String>(){{add("category");}};
        Book book = new Book(isbn, "title", "subtitle", "publisher",
                (long) 8000000, "description", 300, "url", "len", "link", 4.0, author, category);

        entityManager.persist(book);
        entityManager.flush();
        // when
        Book found = bookService.findBookByIsbn(isbn);
        // then
        assertThat(found.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    public void whenInvalidBookIsbn_thenBookShouldNotBeFound() {

        String isbn = "12345";
        ArrayList<String> author = new ArrayList<String>(){{add("author");}};
        ArrayList<String> category = new ArrayList<String>(){{add("category");}};
        Book book = new Book(isbn, "title", "subtitle", "publisher",
                (long) 8000000, "description", 300, "url", "len", "link", 4.0, author, category);

        entityManager.persist(book);
        entityManager.flush();
        // when
        Book found = bookService.findBookByIsbn(isbn+"1");
        // then
        assertThat(found.isEmpty()).isTrue();
    }

    @Test
    public void whenValidCategory_thenBooksInCategoryShouldBeFound() {
        String categoryName = "category";
        ArrayList<String> author = new ArrayList<String>(){{add("author");}};
        ArrayList<String> category = new ArrayList<String>(){{add(categoryName);}};
        Book book = new Book("1234", "title", "subtitle", "publisher",
                (long) 8000000, "description", 300, "url", "len", "link", 4.0, author, category);

        entityManager.persist(book);
        entityManager.flush();

        // when
        List<Book> found = bookService.findBooksByCategory(categoryName);
        // then
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0)).isEqualTo(book);
    }

    @Test
    public void whenInvalidCategory_thenBooksInCategoryShouldNotBeFound() {
        String categoryName = "category";
        ArrayList<String> author = new ArrayList<String>(){{add("author");}};
        ArrayList<String> category = new ArrayList<String>(){{add(categoryName);}};
        Book book = new Book("1234", "title", "subtitle", "publisher",
                (long) 8000000, "description", 300, "url", "len", "link", 4.0, author, category);
        entityManager.persist(book);
        entityManager.flush();

        // when
        List<Book> found = bookService.findBooksByCategory(categoryName+"1");
        // then
        assertThat(found.size()).isEqualTo(0);
        assertThat(found.isEmpty()).isTrue();
    }

}