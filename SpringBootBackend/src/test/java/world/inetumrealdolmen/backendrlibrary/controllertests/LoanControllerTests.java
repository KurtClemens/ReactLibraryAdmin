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
import world.inetumrealdolmen.backendrlibrary.domain.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.LoanDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookOnShelveDto;
import world.inetumrealdolmen.backendrlibrary.repository.BookOnShelveRepository;
import world.inetumrealdolmen.backendrlibrary.repository.LoanRepository;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class LoanControllerTests {
    @MockBean
    private LoanRepository loanRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BookOnShelveRepository bookOnShelveRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Loan loan1;
    private Loan loan2;
    private Loan loan3;
    private List<Loan> loans;
    private User user1;
    private User user3;
    private LoanDao loanDao;
    private BookOnShelve bookOnShelve;
    private BookOnShelveDto bookOnShelveDto;

    @BeforeEach
    public void setup() {
        loans = new ArrayList<>();
        List<Subject> subjects = new ArrayList<>();
        List<Author> authors = new ArrayList<>();

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

        User user2 = User.builder()
                .id(2L)
                .firstName("TestFirstName2")
                .name("TestName2")
                .email("test2@inetum-realdolmen.world")
                .password("test")
                .role("ROLE_ADMIN")
                .loans(new ArrayList<>())
                .bookRequests(new ArrayList<>())
                .build();

        user3 = User.builder()
                .id(3L)
                .firstName("TestFirstName3")
                .name("TestName3")
                .email("test3@inetum-realdolmen.world")
                .password("test")
                .role("ROLE_ADMIN")
                .loans(new ArrayList<>())
                .bookRequests(new ArrayList<>())
                .build();

        Publisher publisher = Publisher.builder()
                .id(1L)
                .name("test publisher")
                .build();

        Subject subject = Subject.builder()
                .id(1L)
                .technologyName("test name")
                .build();
        subjects.add(subject);

        Author author = Author.builder()
                .id(1L)
                .name("test name")
                .firstName("test firstname")
                .dateOfBirth(LocalDateTime.of(1986, 3, 31, 15, 15, 58))
                .build();
        authors.add(author);

        Office office = Office.builder()
                .id(1L)
                .name("office name")
                .city("test city")
                .postalCode("test postalcode")
                .street("test street")
                .number("test number")
                .booksOnShelve(new ArrayList<>())
                .build();

        bookOnShelve = BookOnShelve.builder()
                .id(1L)
                .available(true)
                .dateAdded(LocalDateTime.now())
                .books(new ArrayList<>())
                .build();

        bookOnShelve.setOffice(office);

        Book book = Book.builder()
                .id(1L)
                .isbn("123-4567987")
                .title("test book")
                .language("Enlish")
                .pages(123)
                .description("test description")
                .publishedYear(2023)
                .subjects(subjects)
                .authors(authors)
                .publisher(publisher)
                .build();

        bookOnShelve.addBook(book);
        book.setBookOnShelve(bookOnShelve);

        loan1 = Loan.builder()
                .id(1L)
                .dateBorrowed(LocalDateTime.of(2023,4,1, 12, 52, 13))
                .dateReturned(null)
                .dateToReturn(LocalDateTime.of(2023,4,1, 12, 52, 13).plusMonths(1))
                .extended(false)
                .bookOnShelve(bookOnShelve)
                .build();
        loan2 = Loan.builder()
                .id(2L)
                .dateBorrowed(LocalDateTime.of(2023,4,1, 12, 52, 13))
                .dateReturned(LocalDateTime.of(2023,4,16, 14, 32, 45))
                .dateToReturn(LocalDateTime.of(2023,4,1, 12, 52, 13).plusMonths(1))
                .extended(false)
                .bookOnShelve(bookOnShelve)
                .build();
        loan3 = Loan.builder()
                .id(3L)
                .dateBorrowed(LocalDateTime.of(2023, 2, 1, 12, 52, 13))
                .dateReturned(null)
                .dateToReturn(LocalDateTime.of(2023, 3, 1, 12, 52, 13).plusMonths(1))
                .extended(false)
                .bookOnShelve(bookOnShelve)
                .build();

        user2.addLoan(loan2);
        loan2.setUser(user2);
        loan1.setUser(user1);
        user1.addLoan(loan1);
        user2.addLoan(loan3);
        loan3.setUser(user2);

        loans.add(loan1);
        loans.add(loan2);
        loans.add(loan3);

        loanDao = LoanDao.builder()
                .id(1L)
                .dateBorrowed(LocalDateTime.of(2023, 4, 1, 12, 52, 13))
                .dateReturned(null)
                .dateToReturn(LocalDateTime.of(2023, 4, 1, 12, 52, 13).plusMonths(1))
                .extended(false)
                .bookOnShelveId(1L)
                .build();

        bookOnShelveDto = BookOnShelveDto.builder()
                .id(1L)
                .available(true)
                .dateAdded(LocalDateTime.of(1999,1,1,1,1,1))
                .build();
    }

    @Test
    public void getAllLoans_ShouldReturnAllLoans() throws Exception {
        when(loanRepository.findAll()).thenReturn(loans);

        mvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0].userEmail").value(loan1.getUser().getEmail()))
                .andExpect(jsonPath("$[1].userEmail").value(loan2.getUser().getEmail()))
                .andDo(print());
    }

    @Test
    public void getLoansOverdue_ShouldReturnAllLoansOverdue() throws Exception {
        when(loanRepository.findAll()).thenReturn(loans);

        mvc.perform(get("/loans/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[1].userEmail").value(loan3.getUser().getEmail()))
                .andDo(print());
    }
    @Test
    public void getAllLoans_ShouldThrowNotFoundException_IfNoLoansAreFound() throws Exception {
        mvc.perform(get("/loans"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No loans are registered in the application."))
                .andDo(print());
    }
    @Test
    public void getLoansForUser_ShouldReturnLoansForUser() throws Exception {
        long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        mvc.perform(get("/loans/user/{userId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].userEmail").value(loan1.getUser().getEmail()))
                .andDo(print());
    }
    @Test
    public void createLoanForUser_ShouldReturnUserDtoWithLoans() throws Exception {
        long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user3));
        when(bookOnShelveRepository.findById(id)).thenReturn(Optional.of(bookOnShelve));
        mvc.perform(post("/loans/{userId}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookOnShelveDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andExpect(jsonPath("$.loanDtos[0].userEmail").value(user3.getEmail()))
                .andExpect(jsonPath("$.loanDtos[0].bookTitle").value(bookOnShelve.getBooks().get(0).getTitle()))
                .andDo(print());
    }

    @Test
    public void createLoanForUser_ShouldThrowNotFoundExceptionIfBookOnShelveIsNotAvailable() throws Exception {
        long id = 1L;
        bookOnShelveDto.setAvailable(false);
        when(userRepository.findById(id)).thenReturn(Optional.of(user3));
        when(bookOnShelveRepository.findById(id)).thenReturn(Optional.of(bookOnShelve));
        mvc.perform(post("/loans/{userId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookOnShelveDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book with id 1 is currently borrowed."))
                .andDo(print());
    }
    @Test
    public void extendLoanForUser_ShouldExtendWithOneMonth_AndReturnUserDtoWithLoans() throws Exception {
        long id = 1L;
        Loan loanToExtend = user1.getLoans().stream().filter(loan -> id == loan1.getId()).findAny().orElse(null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));

        assert loanToExtend != null;
        mvc.perform(put("/loans/{userId}/{loanId}", id, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andExpect(jsonPath("$.loanDtos[0].dateToReturn").value(loanToExtend.getDateBorrowed().plusMonths(2).toString()))
                .andExpect(jsonPath("$.loanDtos[0].extended").value(true))
                .andDo(print());
    }
    @Test
    public void extendLoanForUser_ShouldThrowNotFoundException_IfUserHasNoLoans() throws Exception {
        long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user3));

        mvc.perform(put("/loans/{userId}/{loanId}", id, id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No loan with id 1 was found for user with id 1."))
                .andDo(print());
    }

    @Test
    public void returnLoanForUser_ShouldReturnLoan() throws Exception {
        long id = 1L;

        loanDao.setDateReturned(LocalDateTime.now());

        when(loanRepository.findById(id)).thenReturn(Optional.of(loan1));
        when(bookOnShelveRepository.findById(id)).thenReturn(Optional.of(bookOnShelve));

        mvc.perform(put("/loans/{loanId}", id).contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(loanDao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(10)))
                .andExpect(jsonPath("$.dateReturned").exists())
                .andExpect(jsonPath("$.userEmail").value(user1.getEmail()))
                .andDo(print());
    }

    @Test
    public void returnLoanForUser_ShouldThrowNotFoundException_IfNoBookOnShelve() throws Exception {
        long id = 1L;

        when(loanRepository.findById(id)).thenReturn(Optional.of(loan1));

        mvc.perform(put("/loans/{loanId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDao)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No bookOnShelve with id 1 was found."))
                .andDo(print());
    }

    @Test
    public void returnLoanForUser_ShouldThrowNotFoundException_IfNoLoanToReturn() throws Exception {
        long id = 1L;
        bookOnShelve.setAvailable(false);

        when(bookOnShelveRepository.findById(id)).thenReturn(Optional.of(bookOnShelve));

        mvc.perform(put("/loans/{loanId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDao)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No loan was found."))
                .andDo(print());
    }
}
