package com.example.demo2.mapper;

import java.util.List;

public interface Mapper<T, U> {
    T toDto(U book);
    U toEntity(T dto);
    List<T> toDtoList(List<U> books);
}
