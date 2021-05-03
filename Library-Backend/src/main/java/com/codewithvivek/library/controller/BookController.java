package com.codewithvivek.library.controller;

import com.codewithvivek.library.dao.BookDao;
import com.codewithvivek.library.model.*;
import java.lang.reflect.*;
import java.security.*;
import java.util.*;
import java.util.stream.*;
import javax.annotation.security.*;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RolesAllowed("Admin")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @PostMapping("/add")
    public ResponseEntity addBook(@RequestBody Book book) {
        try {
            //book.setIssuedTo(-1);
            bookDao.save(book);
        } catch (Exception e) {
            return ResponseEntity.status(201).body("Error: Something went wrong" + e.getMessage());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "Successfully added book");
        return ResponseEntity.status(201).body(map);
    }

    @GetMapping("/{id}")
    public ResponseEntity getBookById(@PathVariable int id) {
        return ResponseEntity.status(201).body(bookDao.findById(id));
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
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "Successfully Updated");
        return ResponseEntity.status(201).body(map);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        try {
            bookDao.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to delete "+ e.getMessage());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "Successfully deleted");
        return ResponseEntity.status(201).body(map);
    }


    @GetMapping("/availableBooks")
    @RolesAllowed("User")
    public ResponseEntity getAvailableBooks() {
        try {
            List<Book> availableBooks = bookDao.findAll().stream().filter(book -> book.getIssuedTo() == 0).collect(Collectors.toList());
            return ResponseEntity.status(200).body(availableBooks);
        } catch (Exception e) {
            return ResponseEntity.status(200).body("No record found" + e.getMessage());
        }
    }

    @GetMapping("/borrowed")
    @RolesAllowed("User")
    public ResponseEntity getBorrowedBooks(@AuthenticationPrincipal User user) {
        try {
            System.out.println(user.getId());
            List<Book> borrowedBooks = bookDao.getBorrowedBooks(user.getId());
            return ResponseEntity.status(200).body(borrowedBooks);
        } catch (Exception e) {
            return ResponseEntity.status(200).body("No record found" + e.getMessage());
        }
    }

    @GetMapping("/issued")
    public ResponseEntity getIssuedBooks() {

        try {
            List<Book> availableBooks = bookDao.findAll().stream().filter(book -> book.getIssuedTo() != 0).collect(Collectors.toList());
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
    @RolesAllowed("User")
    public ResponseEntity borrowBook(@PathVariable(name = "bookId") int bookId, @AuthenticationPrincipal User user) {
        Book book = bookDao.findById((bookId));
        book.setIssuedTo(user.getId());
        bookDao.save(book);
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "Successfully Borrowed the book");
        return ResponseEntity.status(201).body(map);
    }

    @PostMapping("/return/{bookId}")
    @RolesAllowed("User")
    public ResponseEntity returnBook(@PathVariable(name = "bookId") int bookId, @AuthenticationPrincipal User user) {
        Book book = bookDao.findById((bookId));
        book.setIssuedTo(0);
        bookDao.save(book);
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "Successfully Returned the book");
        return ResponseEntity.status(201).body(map);
    }

    @GetMapping(value= "/columns/name")
    @RolesAllowed({"User", "Admin"})
    public List<String> tableColumnsName()
    {
        List<String> Columns = new ArrayList<String>();
        Field[] fields = Book.class.getDeclaredFields();

        for (Field field : fields) {
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                Columns.add(col.name());
                System.out.println("Columns: "+col);
            }
        }
        return Columns;
    }
}
