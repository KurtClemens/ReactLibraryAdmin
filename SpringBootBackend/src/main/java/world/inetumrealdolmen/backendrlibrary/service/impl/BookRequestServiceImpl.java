package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import world.inetumrealdolmen.backendrlibrary.domain.Book;
import world.inetumrealdolmen.backendrlibrary.domain.BookRequest;
import world.inetumrealdolmen.backendrlibrary.domain.User;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookRequestDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookRequestDto;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.BookRequestRepository;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;
import world.inetumrealdolmen.backendrlibrary.service.BookRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class BookRequestServiceImpl implements BookRequestService {
    private final BookRequestRepository bookRequestRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookRequestDto> getAllBookRequests() {
        List<BookRequest> bookRequests = bookRequestRepository.findAll();
        if (bookRequests.isEmpty()){
            throw new NotFoundException("There are currently no bookrequests.");
        }
        return createBookRequestDtos(bookRequests);
    }

    @Override
    public BookRequestDto createBookRequestForUser(Long userId, BookRequestDao bookRequestDao) {
        User user = getUserById(userId);
        BookRequest newBookRequest = BookRequest.builder()
                .title(bookRequestDao.getTitle())
                .author(bookRequestDao.getAuthor())
                .publisher(bookRequestDao.getPublisher())
                .subject(bookRequestDao.getSubject())
                .reason(bookRequestDao.getReason())
                .dateRequested(LocalDateTime.now())
                .build();
        user.addBookRequest(newBookRequest);
        return createBookRequestDto(newBookRequest);
    }

    private BookRequestDto createBookRequestDto(BookRequest bookRequest) {
        return BookRequestDto.builder()
                .id(bookRequest.getId())
                .requester(bookRequest.getUser().getFirstName() + " "+bookRequest.getUser().getName())
                .title(bookRequest.getTitle())
                .publisher(bookRequest.getPublisher())
                .author(bookRequest.getAuthor())
                .subject(bookRequest.getSubject())
                .dateRequested(bookRequest.getDateRequested())
                .approved(bookRequest.isApproved())
                .mailSend(bookRequest.isMailSend())
                .purchased(bookRequest.isPurchased())
                .reason(bookRequest.getReason())
                .build();
    }

    @Override
    public void deleteBookRequest(Long userId, Long bookRequestId) {
        User user = getUserById(userId);
        BookRequest bookRequest = bookRequestById(bookRequestId);
        user.removeBookRequest(bookRequest);
    }

    @Override
    public BookRequestDto updateBookRequest(Long bookRequestId, BookRequestDao bookRequestDao) {
        BookRequest bookRequest = bookRequestById(bookRequestId);
        bookRequest.setTitle(bookRequestDao.getTitle());
        bookRequest.setPublisher(bookRequestDao.getPublisher());
        bookRequest.setAuthor(bookRequestDao.getAuthor());
        bookRequest.setSubject(bookRequestDao.getSubject());
        bookRequest.setApproved(bookRequestDao.isApproved());
        bookRequest.setReason(bookRequestDao.getReason());
        bookRequest.setPurchased(bookRequestDao.isPurchased());
        if(bookRequest.isApproved()){
            //send email to
            //bookRequest.getUser().getEmail();
            bookRequest.setMailSend(true);
        }
        if(bookRequest.isPurchased()){
            //create book and bookonShelve
            bookRequest.setPurchased(true);
        }
        return createBookRequestDto(bookRequest);
    }

    @Override
    public BookRequestDto getBookRequestById(Long bookRequestId) {
        BookRequest bookRequest = bookRequestRepository.findById(bookRequestId).orElseThrow(()->
                new NotFoundException(String.format("No bookrequest with id %s was found.", bookRequestId)));
        return createBookRequestDto(bookRequest);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->
                new NotFoundException(String.format("No user with id %s was found.", userId)));
    }

    private BookRequest bookRequestById(Long bookRequestId) {
        return bookRequestRepository.findById(bookRequestId).orElseThrow(()->
                new NotFoundException(String.format("No bookrequest with id %s was found.", bookRequestId)));
    }


    private List<BookRequestDto> createBookRequestDtos(List<BookRequest> bookRequests) {
        List<BookRequestDto> bookRequestDtos = new ArrayList<>();
        for(var bookRequest: bookRequests){
            bookRequestDtos.add(createBookRequestDto(bookRequest));
        }
        return bookRequestDtos;
    }
}