package world.inetumrealdolmen.backendrlibrary.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import world.inetumrealdolmen.backendrlibrary.domain.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookOnShelveDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.LoanDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.LoanDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;
import world.inetumrealdolmen.backendrlibrary.exception.NotAvailableException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.BookOnShelveRepository;
import world.inetumrealdolmen.backendrlibrary.repository.LoanRepository;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;
import world.inetumrealdolmen.backendrlibrary.service.impl.LoanServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTests {
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookOnShelveRepository bookOnShelveRepository;
    @InjectMocks
    private LoanServiceImpl loanService;
    private List<Loan> loans;
    private User user1;
    private User user3;
    private LoanDao loanDao;
    private Loan loan1;
    private BookOnShelve bookOnShelve;
    private BookOnShelveDao bookOnShelveDao;

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

        BookRequest bookRequest = BookRequest.builder()
                .id(1L)
                .title("bookrequest title")
                .subject("bookrequest subject")
                .publisher("bookrequest publisher")
                .dateRequested(LocalDateTime.of(2020, 5, 6, 8, 9, 6))
                .approved(false)
                .reason("")
                .mailSend(false)
                .purchased(false)
                .build();
        user3.addBookRequest(bookRequest);

        loan1 = Loan.builder()
                .id(1L)
                .dateBorrowed(LocalDateTime.of(2023, 4, 1, 12, 52, 13))
                .dateReturned(null)
                .dateToReturn(LocalDateTime.of(2023, 4, 1, 12, 52, 13).plusMonths(1))
                .extended(false)
                .bookOnShelve(bookOnShelve)
                .build();
        Loan loan2 = Loan.builder()
                .id(2L)
                .dateBorrowed(LocalDateTime.of(2023, 4, 1, 12, 52, 13))
                .dateReturned(LocalDateTime.of(2023, 4, 16, 14, 32, 45))
                .dateToReturn(LocalDateTime.of(2023, 4, 1, 12, 52, 13).plusMonths(1))
                .extended(false)
                .bookOnShelve(bookOnShelve)
                .build();
        Loan loan3 = Loan.builder()
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

        bookOnShelveDao = BookOnShelveDao.builder()
                .id(1L)
                .available(true)
                .dateAdded(LocalDateTime.of(1999,1,1,1,1,1))
                .build();
    }

    @Test
    public void getAllLoans_IfLoansAreRegistered(){
        when(loanRepository.findAll()).thenReturn(loans);

        List<LoanDto> loanDtos = loanService.getAllLoans();

        assertNotNull(loanDtos);
        assertEquals(3, loanDtos.size());
    }

    @Test
    public void getLoansOverdue(){
        when(loanRepository.findAll()).thenReturn(loans);

        List<LoanDto> loanDtos = loanService.getLoansOverdue();

        assertNotNull(loanDtos);
        assertEquals(2, loanDtos.size());
    }

    @Test
    public void getAllLoans_ShouldThrowNotFoundException_IfNoLoansAreRegistered(){
        when(loanRepository.findAll()).thenReturn(new ArrayList<>());
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> loanService.getAllLoans());

        String expectedMessage = "No loans are registered in the application.";
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> loanService.getAllLoans());
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void getLoansByUser_ShouldReturnLoansFromUser(){
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));

        List<LoanDto> loanDtos = loanService.getLoansByUser(id);

        assertNotNull(loanDtos);
        assertEquals(1, loanDtos.size());
    }

    @Test
    public void getLoansByUser_ShouldThrowNotFoundException_IfUserNotExists(){
        Long id = 4L;

        NotFoundException exception = assertThrows(NotFoundException.class, ()->loanService.getLoansByUser(id));

        String expectedMessage = String.format("No user with id %s was found.", id);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> loanService.getLoansByUser(id));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenUserIdAndBookOnShelveDto_ShouldCreateLoanForUser_AndReturnUserDto(){
        when(userRepository.findById(user3.getId())).thenReturn(Optional.of(user3));
        when(bookOnShelveRepository.findById(bookOnShelve.getId())).thenReturn(Optional.of(bookOnShelve));

        UserDto userDto = loanService.createLoanForUser(user3.getId(), bookOnShelveDao);

        assertNotNull(userDto.getLoanDtos());
        assertEquals(userDto.getLoanDtos().size(), 1);
        assertEquals(userDto.getEmail(), user3.getEmail());
    }

    @Test
    public void givenUserIdAndBookOnShelveDto_ShouldThrowNotAvailableException_IfBookIsNotAvailable(){
        when(userRepository.findById(user3.getId())).thenReturn(Optional.of(user3));
        when(bookOnShelveRepository.findById(bookOnShelve.getId())).thenReturn(Optional.of(bookOnShelve));
        bookOnShelveDao.setAvailable(false);
        NotAvailableException exception = assertThrows(NotAvailableException.class, ()-> loanService.createLoanForUser(user3.getId(), bookOnShelveDao));

        String expectedMessage = String.format("Book with id %s is currently borrowed.", 1L);
        String actualMessage = exception.getMessage();

        assertThrows(NotAvailableException.class, ()-> loanService.createLoanForUser(user3.getId(), bookOnShelveDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenUserIdAndLoanId_ShouldExtendLoanForUser_AndReturnUserDto(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        UserDto userDto = loanService.extendLoanForUser(user1.getId(), 1L);

        assertNotNull(userDto.getLoanDtos());
        assertEquals(userDto.getLoanDtos().size(), 1);
        assertTrue(userDto.getLoanDtos().get(0).isExtended());
        assertEquals(userDto.getLoanDtos().get(0).getDateToReturn(), user1.getLoans().get(0).getDateBorrowed().plusMonths(2));
        assertEquals(userDto.getEmail(), user1.getEmail());
    }

    @Test
    public void givenUserIdAndLoanId_ShouldThrowNotFoundException_IfNoLoans(){
        when(userRepository.findById(user3.getId())).thenReturn(Optional.of(user3));
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> loanService.extendLoanForUser(user3.getId(), 1L));

        String expectedMessage = String.format("No loan with id %s was found for user with id %s.", 1L, user3.getId());
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> loanService.extendLoanForUser(user3.getId(), 1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenUserIdAndLoanDao_ShouldUpdateLoanExtended_AndReturnUserDto(){
        bookOnShelve.setAvailable(false);
        loanDao.setExtended(true);

        when(bookOnShelveRepository.findById(1L)).thenReturn(Optional.of(bookOnShelve));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan1));

        Loan loanToReturn = user1.getLoans().stream().filter((loan -> Objects.equals(loan.getBookOnShelve().getId(), bookOnShelveDao.getId()))).findFirst().orElse(null);

        loanService.updateLoan(user1.getId(), loanDao);

        assert loanToReturn != null;
        assertTrue(loanToReturn.isExtended());
        assertNull(loanToReturn.getDateReturned());
    }

    @Test
    public void givenUserIdAndLoanDao_ShouldUpdateLoanDateReturned_AndReturnUserDto(){
        bookOnShelve.setAvailable(false);
        loanDao.setDateReturned(LocalDateTime.now());

        when(bookOnShelveRepository.findById(1L)).thenReturn(Optional.of(bookOnShelve));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan1));

        Loan loanToReturn = user1.getLoans().stream().filter((loan -> Objects.equals(loan.getBookOnShelve().getId(), bookOnShelveDao.getId()))).findFirst().orElse(null);

        loanService.updateLoan(user1.getId(), loanDao);

        assert loanToReturn != null;
        assertNotNull(loanToReturn.getDateReturned());
    }

    @Test
    public void givenUserIdAndLoanDao_ShouldThrowNotFoundException_IfNoLoanAvailable(){
        when(bookOnShelveRepository.findById(1L)).thenReturn(Optional.of(bookOnShelve));

        NotFoundException exception = assertThrows(NotFoundException.class, ()-> loanService.updateLoan(user3.getId(), loanDao));

        String expectedMessage = String.format("No %s was found.", "loan");
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> loanService.updateLoan(user3.getId(), loanDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
