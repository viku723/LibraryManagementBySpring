package com.codewithvivek.library.controller;

import com.codewithvivek.library.dao.BookDao;
import com.codewithvivek.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @PostMapping("/add")
    public String addBook(@RequestBody Book book) {
        bookDao.save(book);
        return "Successfully added book";
    }

    @GetMapping("/all")
    public ResponseEntity getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
//            for (Book book : bookDao.findAll()) {
//                books.add(book);
//            }
            bookDao.findAll().forEach(books::add);
        }
        catch (Exception e) {
            return ResponseEntity.status(201).body("Error: No Record found" + e.getMessage());
        }
        if (books == null) {
            return ResponseEntity.status(201).body("No Record found");
        }
        return ResponseEntity.status(201).body(books);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity updateBook(@PathVariable int id, @RequestBody Book bookToUpdate) {
        try {
           Book book = bookDao.getOne(id);
           book.setName(bookToUpdate.getName());
           book.setAuthor(bookToUpdate.getAuthor());
           book.setPrice(bookToUpdate.getPrice());
           bookDao.save(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to update "+ e.getMessage());
        }
        return ResponseEntity.status(201).body("Successfully Updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        try {
            bookDao.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to delete "+ e.getMessage());
        }
        return ResponseEntity.status(201).body("Successfully deleted");
    }


    @GetMapping("/availableBooks")
    public ResponseEntity getAvailableBooks() {
        try {
            List<Book> availableBooks = bookDao.findByIssuedToIsNull();
            return ResponseEntity.status(200).body(availableBooks);
        } catch (Exception e) {
            return ResponseEntity.status(200).body("No record found" + e.getMessage());
        }
    }

    @GetMapping("/mybooks")
    public ResponseEntity getMyBooks() {
        return ResponseEntity.ok("needs implementation");
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity borrowBook(@PathVariable(name = "bookId") int bookId) {
        return ResponseEntity.ok("needs implementation");
    }
}
