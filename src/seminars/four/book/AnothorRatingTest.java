package com.book.unittest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;

import static org.hamcrest.CoreMatchers.*;

import com.book.model.AuthorRating;
import com.book.repository.BookLibraryRepository;
import com.book.service.IBookLibraryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
@ComponentScan(basePackageClasses = {
        IBookLibraryService.class,
        BookLibraryRepository.class
})
public class AuthorRatingTest {

    @Test
    public void givenFieldIsIgnoredByName_whenAuthorRatingIsSerialized_thenCorrect()
            throws JsonParseException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<Double> ratings = new ArrayList<Double>();
        ratings.add(3.0);
        AuthorRating ar = new AuthorRating("author", ratings);

        String arAsString = mapper.writeValueAsString(ar);

        assertThat(arAsString, not(containsString("ratings")));
    }

}