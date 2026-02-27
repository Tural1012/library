package com.example.demo2.mapper;

import com.example.demo2.dto.BookDtoRegistr;
import com.example.demo2.entity.Book;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Qualifier("reg")
public class BookRegistImpl implements Mapper<BookDtoRegistr, Book> {

    @Override
    @Transactional
    public BookDtoRegistr toDto(Book book) {
        BookDtoRegistr bookDtoRegistr = new BookDtoRegistr();
        bookDtoRegistr.setName(book.getName());
        bookDtoRegistr.setAuthor(book.getAuthor());
        bookDtoRegistr.setPrice(book.getPrice());
        return bookDtoRegistr;
    }

    @Override
    @Transactional
    public Book toEntity(BookDtoRegistr dto) {
        Book book = new Book();
        book.setName(dto.getName());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        return book;
    }

    @Override
    @Transactional
    public List<BookDtoRegistr> toDtoList(List<Book> books) {
        return books.stream().map(this::toDto).toList();
    }


}
