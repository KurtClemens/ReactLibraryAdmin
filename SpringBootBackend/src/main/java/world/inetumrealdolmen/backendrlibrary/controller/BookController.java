package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookDto;
import world.inetumrealdolmen.backendrlibrary.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@CrossOrigin
public class BookController {

    @Autowired
    private final BookService bookService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "",produces = "application/json")
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{bookId}",produces = "application/json")
    public BookDto getBookById(@PathVariable Long bookId){
        return bookService.getBookById(bookId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "",produces = "application/json")
    public BookDto addBook(@RequestBody BookDao bookDao){
        return bookService.createBook(bookDao);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{bookId}", produces = "application/json")
    public void deleteBook(@PathVariable Long bookId){
        bookService.deleteBook(bookId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{bookId}", produces = "application/json")
    public BookDto updateBook(@PathVariable Long bookId, @RequestBody BookDao bookDao){
        return bookService.updateBook(bookId, bookDao);
    }
}
