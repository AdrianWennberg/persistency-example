package com.example.persistency.controller;

import com.example.persistency.exception.BookNotFoundException;
import com.example.persistency.model.Book;
import com.example.persistency.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class BookController {
    @Autowired
    BookRepository bookRepository;


    @GetMapping("/books")
    @ResponseBody
    public List<Book> getAllNotes() {
     return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    @ResponseBody
    public Book getNoteById(@PathVariable(value = "id") Long bookId) throws BookNotFoundException {
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @PutMapping("/books/{id}")
    @ResponseBody
    public Book updateNote(@PathVariable(value = "id") Long bookId
            , @Valid @RequestBody Book book_details){
        Book book = bookRepository.findById(bookId).orElse(new Book());

        book.setBook_name(book_details.getBook_name());
        book.setAuthor_name(book_details.getAuthor_name());
        book.setIsbn(book_details.getIsbn());

        return bookRepository.save(book);
    }

    @DeleteMapping("/books/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteBook(@PathVariable(value = "id") Long bookId) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        bookRepository.delete(book);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/book")
    public String bookForm(Model model) {
        Book b = new Book();;
        model.addAttribute("book", b);
        return "book";
    }

    @PostMapping("/book")
    public RedirectView redirectWithUsingRedirectView(@ModelAttribute Book book,
            RedirectAttributes attributes) {
        book = updateNote(book.getId(), book);
        return new RedirectView("/books/" + book.getId());
    }

}
