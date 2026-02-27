package com.example.demo2.controller;
import com.example.demo2.dto.BookDto;
import com.example.demo2.dto.BookDtoRegistr;
import com.example.demo2.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/api")
@RestController
public class Controller {

    @Autowired
    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }


    @PostMapping
    public Response addBook(@RequestBody BookDtoRegistr book) {
        service.addBook(book);
        return new Response("Add");
    }
    @GetMapping
    public List<BookDto> getAllBooks() {
        return service.GetAllBooks();
    }

    @GetMapping("/{id}")
    public BookDto getById(@PathVariable Long id) {
        return service.findById(id);
    }
    @PutMapping("/{id}")
    public Response update(@PathVariable Long id, @RequestBody BookDtoRegistr dto) {
        service.update(id, dto);
        return new Response("Update");
    }
    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        service.delete(id);
        return new Response("Delete");
    }

    public record Response(String name) {}
}
