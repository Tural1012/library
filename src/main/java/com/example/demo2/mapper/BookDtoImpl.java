package com.example.demo2.mapper;

import com.example.demo2.dto.BookDto;
import com.example.demo2.entity.Book;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Qualifier("dto")
public class BookDtoImpl implements Mapper<BookDto, Book> {

    @Override
    @Transactional
    public BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setName(book.getName());
        dto.setAuthor(book.getAuthor());
        return dto;
    }

    @Override
    @Transactional
    public Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setAuthor(dto.getAuthor());
        book.setName(dto.getName());
        return book;
    }

    @Override
    @Transactional
    public List<BookDto> toDtoList(List<Book> books) {
        return books.stream().map(this::toDto).toList();
    }


}
