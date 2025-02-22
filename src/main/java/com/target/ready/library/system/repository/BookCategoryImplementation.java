package com.target.ready.library.system.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.BookCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class BookCategoryImplementation implements BookCategoryRepository{

    private final WebClient webClient;

    @Value("${library.baseUrl2}")
    private String libraryBaseUrl2;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public BookCategoryImplementation(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void addBookCategory(BookCategory bookCategory){
        try {
            webClient.post().uri(libraryBaseUrl2 + "inventory/book/category")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(bookCategory))
                    .retrieve()
                    .bodyToMono(BookCategory.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add", e);
        }
    }

    @Override
    public String deleteBookCategory(int bookId) {
        return webClient.delete()
                .uri(libraryBaseUrl2 + "inventory/delete/bookCategory/" + bookId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class).block();
    }

    @Override
    public List<BookCategory> findAllCategoriesByBookId(int bookId){
        List<BookCategory> categories= webClient.get().uri(libraryBaseUrl2 + "categories/" + bookId).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(BookCategory.class)
                .block().getBody();
        return  categories;
    }
}
