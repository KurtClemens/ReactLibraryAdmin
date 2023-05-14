package world.inetumrealdolmen.backendrlibrary.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import world.inetumrealdolmen.backendrlibrary.domain.BookRequest;
import world.inetumrealdolmen.backendrlibrary.domain.User;
import world.inetumrealdolmen.backendrlibrary.repository.BookRequestRepository;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class BookRequestControllerTests {

    @MockBean
    private BookRequestRepository bookRequestRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private BookRequest bookRequest1;
    private BookRequest bookRequest2;
    private List<BookRequest> bookRequests;
    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        bookRequests = new ArrayList<>();
        bookRequest1 = BookRequest.builder()
                .id(1L)
                .title("title 1")
                .publisher("publisher 1")
                .subject("subject 1")
                .dateRequested(LocalDateTime.of(2023,3,31,6,53,10))
                .approved(false)
                .reason("")
                .mailSend(false)
                .purchased(false)
                .build();
        bookRequest2 = BookRequest.builder()
                .id(2L)
                .title("title 2")
                .publisher("publisher 2")
                .subject("subject 2")
                .dateRequested(LocalDateTime.of(2023,1,31,6,53,20))
                .approved(false)
                .reason("")
                .mailSend(false)
                .purchased(false)
                .build();

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

        user2 = User.builder()
                .id(2L)
                .firstName("TestFirstName2")
                .name("TestName2")
                .email("test2@inetum-realdolmen.world")
                .password("test2")
                .role("ROLE_ADMIN")
                .loans(new ArrayList<>())
                .bookRequests(new ArrayList<>())
                .build();

        bookRequest1.setUser(user1);
        bookRequest2.setUser(user2);
        bookRequests.add(bookRequest1);
        bookRequests.add(bookRequest2);

    }

    @Test
    public void getAllBookRequests_ShouldReturnAllBooksRequests() throws Exception {
        when(bookRequestRepository.findAll()).thenReturn(bookRequests);

        mvc.perform(get("/book-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
//                .andExpect(jsonPath("$[0].title").value(bookRequest1.getTitle()))
//                .andExpect(jsonPath("$[1].subject").value(bookRequest2.getSubject()))
                .andDo(print());
    }

    @Test
    public void getAllBookRequests_ShouldThrowNotFoundException_IfNoBookRequestFound() throws Exception {
        mvc.perform(get("/book-requests"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("There are currently no bookrequests."))
                .andDo(print());
    }

    @Test
    public void createBookRequestsForUser_ShouldAddBookRequestToUser() throws Exception {
        long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        mvc.perform(post("/book-requests/{userId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest1)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void createBookRequestsForUser_ShouldThrowNotFoundException_IfUserNotExists() throws Exception {
        long id = 1L;

        mvc.perform(post("/book-requests/{userId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest1)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No user with id 1 was found."))
                .andDo(print());
    }

    @Test
    public void deleteBookRequest_ShouldDeleteBookRequest_IfUserAndBookRequestExists() throws Exception {
        long id = 1L;
        long id2 = 1L;

        when(bookRequestRepository.findById(id)).thenReturn(Optional.of(bookRequest1));
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        mvc.perform(delete("/book-requests/{userId}/{bookRequestId}", id2, id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteBookRequest_ShouldThrowNotFoundException_IfUserNotExists() throws Exception {
        long id = 1L;
        long id2 = 1L;

        when(bookRequestRepository.findById(id)).thenReturn(Optional.of(bookRequest1));
        mvc.perform(delete("/book-requests/{userId}/{bookRequestId}", id2, id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No user with id 1 was found."))
                .andDo(print());
    }
    @Test
    public void deleteBookRequest_ShouldThrowNotFoundException_IfBookRequestNotExists() throws Exception {
        long id = 1L;
        long id2 = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        mvc.perform(delete("/book-requests/{userId}/{bookRequestId}", id2, id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No bookrequest with id 1 was found."))
                .andDo(print());
    }

    @Test
    public void shouldUpdateBookRequest_IfBookRequestExists() throws Exception {
        long id = 3L;
        BookRequest bookRequestUpdated = bookRequest2;
        bookRequestUpdated.setTitle("new title bookrequest 2");
        bookRequestUpdated.setApproved(true);

        when(bookRequestRepository.findById(id)).thenReturn(Optional.of(bookRequest1));
        when(bookRequestRepository.save(any(BookRequest.class))).thenReturn(bookRequestUpdated);

        mvc.perform(put("/book-requests/{bookRequestId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(bookRequestUpdated.getTitle()))
                .andExpect(jsonPath("$.approved").value(bookRequestUpdated.isApproved()))
                .andDo(print());
    }

    @Test
    public void updateBookRequest_ShouldThrowNotFoundException_IfBookRequestNotExists() throws Exception {
        long id = 3L;
        BookRequest bookRequestUpdated = bookRequest2;
        bookRequestUpdated.setTitle("new title bookrequest 2");
        bookRequestUpdated.setApproved(true);

        when(bookRequestRepository.save(any(BookRequest.class))).thenReturn(bookRequestUpdated);

        mvc.perform(put("/book-requests/{bookRequestId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestUpdated)))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("No bookrequest with id 3 was found."))
                        .andDo(print());
    }

}
