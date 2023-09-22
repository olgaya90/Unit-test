package com.book.unittest;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.junit.Test;
import org.springframework.boot.test.json.JacksonTester;

import com.book.model.Book;
import com.book.serialize.BookSerializer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BookSerializerTest {

    @Test
    public void bookSerializer_doesNotSerializeInvalidValues() throws JsonProcessingException, IOException {
        Long publishedDate = (long) -1;
        Integer pageCount = -1;
        Double rating = (double) -1;
        ArrayList<String> author = new ArrayList<String>();
        ArrayList<String> category = new ArrayList<String>();

        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        new BookSerializer().serialize(new Book("1234", "title", "subtitle", "publisher",
                publishedDate, "description", pageCount, "url", "len", "link", rating, author, category), jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString()).doesNotContain("publishedDate", "pageCount", "averageRating", "authors", "categories");
    }

}