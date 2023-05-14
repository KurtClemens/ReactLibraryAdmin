package world.inetumrealdolmen.backendrlibrary.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import world.inetumrealdolmen.backendrlibrary.domain.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.UserDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;
import world.inetumrealdolmen.backendrlibrary.exception.AlreadyExistsException;
import world.inetumrealdolmen.backendrlibrary.exception.EmailException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;
import world.inetumrealdolmen.backendrlibrary.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private UserDao userDao;
    private List<User> userList;

    @BeforeEach
    public void setup(){
        userList = new ArrayList<>();
        List<Subject> subjects = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<BookRequest> bookRequests = new ArrayList<>();
        List<Loan> loans = new ArrayList<>();

        BookRequest bookRequest = BookRequest.builder()
                .id(0L)
                .title("bookrequestTitle")
                .subject("bookSubject")
                .publisher("publisher")
                .dateRequested(LocalDateTime.now())
                .approved(false)
                .reason("leeg")
                .build();
        bookRequests.add(bookRequest);

        Loan loan = Loan.builder()
                .id(0L)
                .dateBorrowed(LocalDateTime.now())
                .dateToReturn(LocalDateTime.now().plusMonths(1))
                .dateReturned(null)
                .extended(false)
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

        BookOnShelve bookOnShelve = BookOnShelve.builder()
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

        loan.setBookOnShelve(bookOnShelve);
        loans.add(loan);

        user = User.builder()
                .id(0L)
                .firstName("TestFirstName")
                .name("TestName")
                .email("test@inetum-realdolmen.world")
                .password("test")
                .role("ROLE_ADMIN")
                .loans(loans)
                .bookRequests(bookRequests)
                .active(true)
                .build();
        loan.setUser(user);

        userDao = UserDao.builder()
                .firstName("TestDaoFirstName")
                .name("TestDaoName")
                .email("testDao@realdolmen.com")
                .password("testDao")
                .role("ROLE_ADMIN")
                .build();

        userList.add(user);
    }

    @Test
    public void getAllUsers_IfUserAreRegistered(){

        when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> userDtos = userServiceImpl.getAllUsers();

        assertNotNull(userDtos);
        assertEquals("test@inetum-realdolmen.world", userDtos.get(0).getEmail());
        assertEquals(1, userDtos.size());
    }

    @Test
    public void getUserById_IfUserExists(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        UserDto userDto = userServiceImpl.getUserById(user.getId());

        assertNotNull(userDto);
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void getUserById_ShouldThrowException_IfUserNotExists(){
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> userServiceImpl.getUserById(user.getId()));

        String expectedMessage = String.format("No user with id %s was found.", 0L);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> userServiceImpl.getUserById(0L));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllUsers_ShouldThrowNotFoundException_IfNoUserAreRegistered(){

        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> userServiceImpl.getAllUsers());

        String expectedMessage = "No users are registered in the application";
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> userServiceImpl.getAllUsers());
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void givenUserDao_SaveEmployeeIfEmailNotExists_ThenReturnUserDto(){

        UserDto newUserDto = userServiceImpl.createUser(userDao);

        assertNotNull(newUserDto);
        assertEquals(userDao.getEmail(), newUserDto.getEmail());
    }

    @Test
    public void givenUserDao_SaveShouldThrowAlreadyExistsException_IfUserWithEmailExists(){

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, ()-> userServiceImpl.createUser(userDao));

        String expectedMessage = String.format("User with email %s already exists", userDao.getEmail());
        String actualMessage = exception.getMessage();

        assertThrows(AlreadyExistsException.class, ()-> userServiceImpl.createUser(userDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenUserDao_SaveShouldThrowEmailException_IfEmailHasIncorrectDomain(){

        userDao.setEmail("test@test.be");

        EmailException exception = assertThrows(EmailException.class, ()-> userServiceImpl.createUser(userDao));

        String EMAIL_DOMAIN_INETUM = "@inetum-realdolmen.world";
        String EMAIL_DOMAIN_REALDOLMEN = "@realdolmen.com";
        String expectedMessage = String.format("Email domain is not correct. Use an email with domain %s or %s", EMAIL_DOMAIN_REALDOLMEN, EMAIL_DOMAIN_INETUM);
        String actualMessage = exception.getMessage();

        assertThrows(EmailException.class, ()-> userServiceImpl.createUser(userDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenUserId_DeleteShouldThrowNotFoundException_IfUserNotExists(){
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> userServiceImpl.deleteUser(1L, true));

        String expectedMessage = String.format("No user with id %s was found.", 1L);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> userServiceImpl.deleteUser(1L, true));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenUserId_ShouldDeleteUser_IfExists(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        assertTrue(user.isActive());

        userServiceImpl.deleteUser(user.getId(), true);

        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);

    }

    @Test
    public void givenUserIdAndUserDao_ShouldUpdateUser_ThenReturnUserDto(){
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        UserDto newUserDto = userServiceImpl.updateUser(1L, userDao);

        assertEquals(newUserDto.getEmail(),userDao.getEmail());
    }

    @Test
    public void givenUserIdUserDao_UpdateShouldThrowEmailException_IfEmailHasIncorrectDomain(){

        userDao.setEmail("test@test.be");

        EmailException exception = assertThrows(EmailException.class, ()-> userServiceImpl.updateUser(1L, userDao));

        String EMAIL_DOMAIN_INETUM = "@inetum-realdolmen.world";
        String EMAIL_DOMAIN_REALDOLMEN = "@realdolmen.com";
        String expectedMessage = String.format("Email domain is not correct. Use an email with domain %s or %s", EMAIL_DOMAIN_REALDOLMEN, EMAIL_DOMAIN_INETUM);
        String actualMessage = exception.getMessage();

        assertThrows(EmailException.class, ()-> userServiceImpl.updateUser(1L, userDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
