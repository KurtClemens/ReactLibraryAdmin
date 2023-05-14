package world.inetumrealdolmen.backendrlibrary.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import world.inetumrealdolmen.backendrlibrary.domain.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.*;
import world.inetumrealdolmen.backendrlibrary.domain.dto.*;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.*;
import world.inetumrealdolmen.backendrlibrary.service.impl.BookServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTests {
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private OfficeRepository officeRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    private Subject subject;
    private Author author;
    private Publisher publisher;
    private Office office;
    private List<Book> books;
    private Book book1;
    private BookDao bookDao;
    private SubjectDao subjectDao;
    private PublisherDao publisherDao;
    private AuthorDao authorDao;

    @BeforeEach
    public void setup() {
        List<Subject> subjects = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        books = new ArrayList<>();
        List<SubjectDao> subjectDaos = new ArrayList<>();
        List<AuthorDao> authorDaos = new ArrayList<>();

        User user1 = User.builder()
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

        User user3 = User.builder()
                .id(3L)
                .firstName("TestFirstName3")
                .name("TestName3")
                .email("test3@inetum-realdolmen.world")
                .password("test")
                .role("ROLE_ADMIN")
                .loans(new ArrayList<>())
                .bookRequests(new ArrayList<>())
                .build();

        publisher = Publisher.builder()
                .id(1L)
                .name("test publisher")
                .books(new ArrayList<>())
                .build();

        subject = Subject.builder()
                .id(1L)
                .technologyName("test name")
                .build();
        subjects.add(subject);

        author = Author.builder()
                .id(1L)
                .name("test name")
                .firstName("test firstname")
                .dateOfBirth(LocalDateTime.of(1986, 3, 31, 15, 15, 58))
                .build();
        authors.add(author);

        office = Office.builder()
                .id(1L)
                .name("office name")
                .city("test city")
                .postalCode("test postalcode")
                .street("test street")
                .number("test number")
                .booksOnShelve(new ArrayList<>())
                .build();

        BookOnShelve bookOnShelve1 = BookOnShelve.builder()
                .id(1L)
                .available(true)
                .dateAdded(LocalDateTime.now())
                .books(new ArrayList<>())
                .build();

        bookOnShelve1.setOffice(office);

        BookOnShelve bookOnShelve2 = BookOnShelve.builder()
                .id(2L)
                .available(true)
                .dateAdded(LocalDateTime.now())
                .books(new ArrayList<>())
                .build();
        bookOnShelve2.setOffice(office);

        book1 = Book.builder()
                .id(1L)
                .isbn("123-4567987")
                .title("test book1")
                .language("Enlish")
                .pages(123)
                .description("test book1 description")
                .publishedYear(2023)
                .subjects(subjects)
                .authors(authors)
                .publisher(publisher)
                .build();

        Book book2 = Book.builder()
                .id(1L)
                .isbn("abc-lkdfhmkljsf")
                .title("test book2")
                .language("Nederlands")
                .pages(653)
                .description("test book2 description")
                .publishedYear(1998)
                .subjects(subjects)
                .authors(authors)
                .publisher(publisher)
                .build();

        bookOnShelve1.addBook(book1);
        book1.setBookOnShelve(bookOnShelve1);

        bookOnShelve2.addBook(book2);
        book2.setBookOnShelve(bookOnShelve2);

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
        publisher.addBook(book1);
        Loan loan1 = Loan.builder()
                .id(1L)
                .dateBorrowed(LocalDateTime.of(2023, 4, 1, 12, 52, 13))
                .dateReturned(null)
                .dateToReturn(LocalDateTime.of(2023, 4, 1, 12, 52, 13).plusMonths(1))
                .extended(false)
                .bookOnShelve(bookOnShelve1)
                .build();
        Loan loan2 = Loan.builder()
                .id(2L)
                .dateBorrowed(LocalDateTime.of(2023, 4, 1, 12, 52, 13))
                .dateReturned(LocalDateTime.of(2023, 4, 16, 14, 32, 45))
                .dateToReturn(LocalDateTime.of(2023, 4, 1, 12, 52, 13).plusMonths(1))
                .extended(false)
                .bookOnShelve(bookOnShelve1)
                .build();

        user2.addLoan(loan2);
        loan2.setUser(user2);
        loan1.setUser(user1);
        user1.addLoan(loan1);

        books.add(book1);
        books.add(book2);

        subjectDao = SubjectDao.builder()
                .id(1L)
                .technologyName("subjectDto technologyName")
                .build();
        subjectDaos.add(subjectDao);

        publisherDao = PublisherDao.builder()
                .id(1L)
                .name("publisherDto name")
                .build();

        authorDao = AuthorDao.builder()
                .id(1L)
                .firstName("authorDto firstName")
                .name("authorDto name")
                .dateOfBirth(LocalDateTime.of(1999,5,5,12,13,16))
                .build();
        authorDaos.add(authorDao);

        OfficeDao officeDao = OfficeDao.builder()
                .id(1L)
                .name("officeDto name")
                .city("officeDto city")
                .street("officeDto street")
                .postalCode("officeDto postalcode")
                .number("officeDto number")
                .build();

        bookDao = BookDao.builder()
                .isbn("159-7532852")
                .title("bookDao title")
                .language("bookDao language")
                .pages(555)
                .description("bookDao description")
                .publishedYear(1999)
                .subjectDaos(subjectDaos)
                .publisherDao(publisherDao)
                .authorDaos(authorDaos)
                .officeDao(officeDao)
                .build();
    }

    @Test
    public void getAllBooks_IfBooksAreRegistered(){
        when(bookRepository.findAll()).thenReturn(books);

        List<BookDto> bookDtos = bookService.getAllBooks();

        assertNotNull(bookDtos);
        assertEquals(2, bookDtos.size());
    }

    @Test
    public void getBookById_IfBookExists(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));

        BookDto bookDto = bookService.getBookById(book1.getId());

        assertNotNull(bookDto);
        assertEquals(bookDto.getIsbn(), book1.getIsbn());
    }

    @Test
    public void getBookById_ShouldThrowException_IfBookNotExists(){
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> bookService.getBookById(book1.getId()));

        String expectedMessage = String.format("No book with id %s was found.", 1L);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> bookService.getBookById(1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void getAllBooks_ShouldThrowNotFoundException_WhenNoBooks(){
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> bookService.getAllBooks());

        String expectedMessage = "No books present in the library yet.";
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> bookService.getAllBooks());
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void givenBookDao_ShouldCreateBookWithExistingAuthorSubjectAndPublisher_AndReturnBookDto(){
        Long id = 1L;
        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(officeRepository.findById(id)).thenReturn(Optional.of(office));

        BookDto bookDto = bookService.createBook(bookDao);

        assertEquals(bookDto.getAuthorDtos().get(0).getId(), author.getId());
        assertNotNull(bookDto.getBookOnShelveDto());
        assertEquals(bookDto.getSubjectDtos().get(0).getId(), subject.getId());
        assertEquals(bookDto.getPublisherDto().getId(), publisher.getId());
        assertEquals(bookDto.getIsbn(), bookDao.getIsbn());
    }

    @Test
    public void givenBookDao_ShouldCreateBookWitNewAuthorSubjectAndPublisher_AndReturnBookDto(){
        Long id = 1L;
        publisherDao.setId(0L);
        subjectDao.setId(0L);
        authorDao.setId(0L);

        when(officeRepository.findById(id)).thenReturn(Optional.of(office));

        BookDto bookDto = bookService.createBook(bookDao);

        assertEquals(1, author.getId());
        assertNotNull(bookDto.getBookOnShelveDto());
        assertEquals(1, subject.getId());
        assertEquals(1, publisher.getId());
        assertEquals(bookDto.getIsbn(), bookDao.getIsbn());
    }

    @Test
    public void givenBookDao_ShouldThrowNotFoundException_IfPublisherHasNonExistingId(){
        publisherDao.setId(20L);
        subjectDao.setId(0L);
        authorDao.setId(0L);

        NotFoundException exception = assertThrows(NotFoundException.class, ()-> bookService.createBook(bookDao));

        String expectedMessage = "No Publisher was found.";
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> bookService.createBook(bookDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void givenBookId_DeleteShouldDeleteBook_IfExists(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));

        bookService.deleteBook(book1.getId());
        verify(bookRepository, times(1)).delete(book1);
    }
    @Test
    public void givenBookId_DeleteShouldThrowNotFoundException_IfBookNotExists(){
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> bookService.deleteBook(book1.getId()));

        String expectedMessage = String.format("No book with id %s was found.", 1L);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> bookService.deleteBook(1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenBookIdAndBookDao_ShouldUpdateBook_AndReturnBookDto(){
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book1));
        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(officeRepository.findById(id)).thenReturn(Optional.of(office));

        BookDto bookDto = bookService.updateBook(id, bookDao);

        assertEquals(bookDto.getAuthorDtos().get(0).getId(), bookDao.getAuthorDaos().get(0).getId());
        assertEquals(bookDto.getSubjectDtos().get(0).getId(), bookDao.getSubjectDaos().get(0).getId());
        assertEquals(bookDto.getPublisherDto().getId(), bookDao.getPublisherDao().getId());
        assertEquals(bookDto.getIsbn(), bookDao.getIsbn());
        verify(officeRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}
