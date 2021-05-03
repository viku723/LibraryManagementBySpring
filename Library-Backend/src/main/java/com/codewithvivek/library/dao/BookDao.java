package com.codewithvivek.library.dao;

import com.codewithvivek.library.model.Book;
import org.hibernate.tool.hbm2ddl.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.metadata.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Integer> {

    //to practice custom query
    //@Query(value = "update books set ")

    List<Book> findByIssuedToIsNull();
    List<Book> findByIssuedToIsNotNull();
    List<Book> findAll();
    Book findById(int id);
    @Query("select b from Book b where b.issuedTo = ?1")
    List<Book> getBorrowedBooks(int userId);
}
