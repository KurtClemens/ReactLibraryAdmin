package world.inetumrealdolmen.backendrlibrary.service;

import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookRequestDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookRequestDto;

import java.util.List;

public interface BookRequestService {
    @Transactional(readOnly = true)
    List<BookRequestDto> getAllBookRequests();
    BookRequestDto createBookRequestForUser(Long userId, BookRequestDao bookRequestDao);
    void deleteBookRequest(Long userId, Long bookRequestId);
    BookRequestDto updateBookRequest(Long bookRequestId, BookRequestDao bookRequestDao);

    BookRequestDto getBookRequestById(Long bookRequestId);
}
