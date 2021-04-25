package com.codewithvivek.library.dao;

import com.codewithvivek.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Integer> {

    //to practice custom query
    //@Query(value = "update books set ")

    public List<Book> findByIssuedToIsNull();
}
