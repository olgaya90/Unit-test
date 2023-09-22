package com.book.unittest;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.junit.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.book.controller.BookLibraryController;
import com.book.exception.ResourceNotFoundException;
import com.book.model.AuthorRating;
import com.book.model.Book;
import com.book.repository.BookLibraryRepository;
import com.book.service.IBookLibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ComponentScan(basePackageClasses = {
        IBookLibraryService.class,
        BookLibraryRepository.class
})
@RunWith(SpringRunner.class)
@WebMvcTest(value = BookLibraryController.class, secure = false)
public class BookLibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private IBookLibraryService bookServiceMock;

    @Autowired
    ObjectMapper objectMapper;

    private JacksonTester<Book> jsonBook;
    private JacksonTester<Set<AuthorRating>> jsonAuthorRatings;
    private JacksonTester<ArrayList<Book>> jsonCategoryBooks;

    @Before
    public void setup() {
        // Initializes the JacksonTester
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void showBookByIsbnShoulReturnBookFromService() throws Exception {
        // given
        String isbn = "1234";
        ArrayList<String> author = new ArrayList<String>();
        author.add("author");
        ArrayList<String> category = new ArrayList<String>();
        category.add("category");
        Book book = new Book(isbn, "title", "subtitle", "publisher",
                (long) 8000000000.0, "description", 300, "url", "len", "link", 4.0,
                author, category);
        when(bookServiceMock.findBookByIsbn(isbn))
                .thenReturn(book);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/book/" + isbn)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().replaceAll("\\s+","")).isEqualTo(
                jsonBook.write(new Book(isbn, "title", "subtitle", "publisher",
                        (long) 8000000000.0, "description", 300, "url", "len", "link", 4.0, author, category)).getJson());
    }

    @Test
    public void showBookByIsbnShoulReturnResourceNotFoundException() throws Exception {
        // given
        String isbn = "1234";

        when(bookServiceMock.findBookByIsbn(isbn))
                .thenThrow(new ResourceNotFoundException("Book", "ISBN", isbn));

        //when
        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/book/" + isbn)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains(
                new ResourceNotFoundException("Book", "ISBN", isbn).getLocalizedMessage());
    }

    @Test
    public void showAuthorsRatingsShouldReturnAuthorsRatingsFromService() throws Exception {

        Set<AuthorRating> authorsRatings = new TreeSet<AuthorRating>();
        ArrayList<Double> rating = new ArrayList<Double>();
        rating.add(4.0);
        AuthorRating ar = new AuthorRating("author", rating);
        ar.calculateRating();
        authorsRatings.add(ar);

        given(bookServiceMock.findAuthorsRatings()).willReturn(authorsRatings);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/rating")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().replaceAll("\\s+","")).isEqualTo(
                jsonAuthorRatings.write(authorsRatings).getJson());
    }

    @Test
    public void showBooksByCategoryShouldReturnBooksFromService() throws Exception {
        String categoryName = "Computers";
        ArrayList<String> author = new ArrayList<String>();
        author.add("author");
        ArrayList<String> category = new ArrayList<String>();
        category.add("Computers");
        Book book = new Book("1234", "title", "subtitle", "publisher",
                (long) 8000000000.0, "description", 300, "url", "len", "link", 4.0,
                author, category);
        ArrayList<Book> books = new ArrayList<Book>();
        books.add(book);
        given(bookServiceMock.findBooksByCategory(categoryName)).willReturn(books);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/category/" + categoryName + "/books")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().replaceAll("\\s+","")).isEqualTo(
                jsonCategoryBooks.write(books).getJson());
    }

    @Test
    public void showBooksByCategoryShouldReturnResourceNotFoundException() throws Exception {
        String categoryName = "Computers";

        given(bookServiceMock.findBooksByCategory(categoryName)).willThrow(
                new ResourceNotFoundException("Books", "Category", categoryName));

        //when
        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/category/" + categoryName + "/books")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains(
                new ResourceNotFoundException("Books", "Category", categoryName).getLocalizedMessage());
    }

}