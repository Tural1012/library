package com.example.demo2.service;


import com.example.demo2.dto.BookDto;
import com.example.demo2.dto.BookDtoRegistr;
import com.example.demo2.mapper.Mapper;
import com.example.demo2.entity.Book;
import com.example.demo2.repo.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@org.springframework.stereotype.Service
public class Service {

    private final BookRepository repository;

    private final Mapper<BookDtoRegistr , Book> registrBookMapper;

    private final Mapper<BookDto, Book> dtoBookMapper;

    @Autowired
    public Service(BookRepository repository,
                   @Qualifier("reg") Mapper<BookDtoRegistr, Book> registrBookMapper,
                   @Qualifier("dto") Mapper<BookDto, Book> dtoBookMapper) {
        this.repository = repository;
        this.registrBookMapper = registrBookMapper;
          this.dtoBookMapper = dtoBookMapper;
    }
    @Transactional
    public void addBook(BookDtoRegistr book) {
       Book book1 = registrBookMapper.toEntity(book);
       repository.save(book1);
    }
    @Transactional(readOnly = true)
    public List<BookDto> GetAllBooks() {
        return dtoBookMapper.toDtoList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public BookDto findById(Long id) {
       var optional = repository.findById(id);
       if (optional.isPresent()){
           Book book = optional.get();
           return dtoBookMapper.toDto(book);
       }
       throw new EntityNotFoundException("Book with id " + id +" not found");
    }
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void update(Long id, BookDtoRegistr dto) {
        var book = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        book.setName(dto.getName());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        repository.save(book);
        }
    @Transactional(noRollbackFor = EntityNotFoundException.class)
    public void delete(Long id) {
       var book = repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Book not found"));
       repository.delete(book);
    }


}
