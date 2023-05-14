package world.inetumrealdolmen.backendrlibrary.service;

import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookDto;

import java.util.List;

public interface BookService {
    @Transactional(readOnly = true)
    List<BookDto> getAllBooks();

    BookDto createBook(BookDao bookDao);

    void deleteBook(Long bookId);

    BookDto updateBook(Long bookId, BookDao bookDao);

    BookDto getBookById(Long bookId);
}
