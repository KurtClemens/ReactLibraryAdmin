package world.inetumrealdolmen.backendrlibrary.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import world.inetumrealdolmen.backendrlibrary.domain.BookRequest;
import world.inetumrealdolmen.backendrlibrary.domain.User;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookRequestDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookRequestDto;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.BookRequestRepository;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;
import world.inetumrealdolmen.backendrlibrary.service.impl.BookRequestServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookRequestServiceImplTests {

    @Mock
    private BookRequestRepository bookRequestRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookRequestServiceImpl bookRequestServiceImpl;

    private BookRequestDao bookRequestDao;
    private BookRequest bookRequest1;
    private List<BookRequest> bookRequests;
    private User user1;

    @BeforeEach
    public void setup() {
        bookRequests = new ArrayList<>();
        bookRequest1 = BookRequest.builder()
                .id(1L)
                .title("title 1")
                .publisher("publisher 1")
                .subject("subject 1")
                .dateRequested(LocalDateTime.of(2023,3,31,6,53,1))
                .approved(false)
                .reason("")
                .mailSend(false)
                .purchased(false)
                .build();
        BookRequest bookRequest2 = BookRequest.builder()
                .id(2L)
                .title("title 2")
                .publisher("publisher 2")
                .subject("subject 2")
                .dateRequested(LocalDateTime.of(2023, 1, 31, 6, 53, 2))
                .approved(false)
                .reason("")
                .mailSend(false)
                .purchased(false)
                .build();
        bookRequests.add(bookRequest1);
        bookRequests.add(bookRequest2);

        user1 = User.builder()
                .id(1L)
                .firstName("TestFirstName")
                .name("TestName")
                .email("test@inetum-realdolmen.world")
                .password("test")
                .role("ROLE_ADMIN")
                .loans(new ArrayList<>())
                .bookRequests(new ArrayList<>())
                .build();
        user1.addBookRequest(bookRequest1);
        user1.addBookRequest(bookRequest2);

        bookRequestDao = BookRequestDao.builder()
                .title("requested title")
                .subject("requested subject")
                .publisher("requested publisher")
                .reason("")
                .approved(false)
                .build();
    }

    @Test
    public void getAllBookRequests_IfBookRequestsAreRegistered(){

        when(bookRequestRepository.findAll()).thenReturn(bookRequests);

        List<BookRequestDto> bookRequestDtos = bookRequestServiceImpl.getAllBookRequests();

        assertNotNull(bookRequestDtos);
        assertEquals("title 1", bookRequestDtos.get(0).getTitle());
        assertEquals(2, bookRequestDtos.size());
    }

    @Test
    public void getAllBookRequests_ShouldThrowNotFoundException_IfNoBookRequestsAreRegistered(){

        when(bookRequestRepository.findAll()).thenReturn(new ArrayList<>());
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> bookRequestServiceImpl.getAllBookRequests());

        String expectedMessage = "There are currently no bookrequests.";
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> bookRequestServiceImpl.getAllBookRequests());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenBookRequestDao_ShouldSaveBookRequest_ThenReturnBookRequestDto(){

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        BookRequestDto bookRequestDto = bookRequestServiceImpl.createBookRequestForUser(user1.getId(), bookRequestDao);

        assertNotNull(bookRequestDto);
        assertEquals(bookRequestDao.getTitle(), bookRequestDto.getTitle());
    }

    @Test
    public void givenUserIdAndBookRequestId_DeleteShouldThrowNotFoundException_IfUserNotExists(){
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> bookRequestServiceImpl.deleteBookRequest(1L,1L));

        String expectedMessage = String.format("No user with id %s was found.", 1L);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> bookRequestServiceImpl.deleteBookRequest(1L, 1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenUserIdAndBookRequestId_DeleteShouldThrowNotFoundException_IfBookRequestNotExists(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));

        NotFoundException exception = assertThrows(NotFoundException.class, ()-> bookRequestServiceImpl.deleteBookRequest(user1.getId(), 1L));

        String expectedMessage = String.format("No bookrequest with id %s was found.", 1L);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> bookRequestServiceImpl.deleteBookRequest(1L, 1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenUserIdAndBookRequestId_DeleteShouldDeleteBookRequest_IfExists(){
        when(bookRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookRequest1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));

        bookRequestServiceImpl.deleteBookRequest(user1.getId(), bookRequest1.getId());
        assertEquals(1, user1.getBookRequests().size());
    }

    @Test
    public void givenUserIdAndBookRequestDao_ShouldUpdateBookRequest_ThenReturnBookRequestDto(){
        when(bookRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(bookRequest1));

        BookRequestDto bookRequestDto = bookRequestServiceImpl.updateBookRequest(1L, bookRequestDao);

        assertEquals(bookRequestDto.getTitle(), bookRequestDao.getTitle());
    }

    @Test
    public void givenUserIdAndBookRequestDao_ShouldUpdateBookRequestAndSendMailIfApproved_ThenReturnBookRequestDto(){
        when(bookRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(bookRequest1));

        bookRequestDao.setApproved(true);
        BookRequestDto bookRequestDto = bookRequestServiceImpl.updateBookRequest(1L, bookRequestDao);

        assertEquals(bookRequestDto.getTitle(), bookRequestDao.getTitle());
        assertTrue(bookRequestDto.isMailSend());
    }

    @Test
    public void givenUserIdAndBookRequestDao_ShouldUpdateBookRequestAndCreateBookIfPurchased_ThenReturnBookRequestDto(){
        when(bookRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(bookRequest1));

        bookRequestDao.setPurchased(true);
        BookRequestDto bookRequestDto = bookRequestServiceImpl.updateBookRequest(1L, bookRequestDao);

        assertEquals(bookRequestDto.getTitle(), bookRequestDao.getTitle());
        assertTrue(bookRequestDto.isPurchased());
    }

}
