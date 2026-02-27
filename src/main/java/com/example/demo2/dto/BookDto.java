package com.example.demo2.dto;


import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("dto")
public class BookDto  {
    private String name;
    private String author;


    public BookDto(){}

    public String getName() {
        return name;
    }



    public String getAuthor() {
        return author;
    }



    public void setName(String name) {
        this.name = name;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

}
