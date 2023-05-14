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
import world.inetumrealdolmen.backendrlibrary.domain.dao.*;
import world.inetumrealdolmen.backendrlibrary.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class BookControllerTests {
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private PublisherRepository publisherRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private SubjectRepository subjectRepository;
    @MockBean
    private OfficeRepository officeRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private Subject subject;
    private Author author;
    private Publisher publisher;
    private Office office;
    private List<Book> books;
    private BookDao bookDao;

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
                .dateOfBirth(LocalDateTime.of(1986,3,31,15,15,58))
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

        BookOnShelve bookOnShelve = BookOnShelve.builder()
                .id(1L)
                .available(true)
                .dateAdded(LocalDateTime.now())
                .books(new ArrayList<>())
                .build();

        bookOnShelve.setOffice(office);

        book = Book.builder()
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

        publisher.addBook(book);
        Loan loan1 = Loan.builder()
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

        user2.addLoan(loan2);
        loan2.setUser(user2);
        loan1.setUser(user1);
        user1.addLoan(loan1);

        books.add(book);

        SubjectDao subjectDao = SubjectDao.builder()
                .id(1L)
                .technologyName("subjectDto technologyName")
                .build();
        subjectDaos.add(subjectDao);

        PublisherDao publisherDao = PublisherDao.builder()
                .id(1L)
                .name("publisherDao name")
                .build();

        AuthorDao authorDao = AuthorDao.builder()
                .id(1L)
                .firstName("authorDto firstName")
                .name("authorDto name")
                .dateOfBirth(LocalDateTime.of(1999, 5, 5, 12, 13, 16))
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
    public void getAllBooks_ShouldReturnAllBooks() throws Exception {
        when(bookRepository.findAll()).thenReturn(books);

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$[0].description").value(book.getDescription()))
                .andDo(print());
    }
    @Test
    public void getBookById_ShouldReturnBook() throws Exception {
        long id = 1L;
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book));

        mvc.perform(get("/books/{bookId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(12)))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.description").value(book.getDescription()))
                .andDo(print());
    }
    @Test
    public void getAllBooks_ShouldThrowNotFoundException_IfNoBooksAreRegistered() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No books present in the library yet."))
                .andDo(print());
    }

    @Test
    public void addBook_ShouldCreateBookWithNewSubjectAuthorAndPublisher_AndReturnBookDto() throws Exception {
        Long id = 1L;

        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(officeRepository.findById(id)).thenReturn(Optional.of(office));

        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(12)))
                .andExpect(jsonPath("$.title").value(bookDao.getTitle()))
                .andExpect(jsonPath("$.publisherDto.name").value(publisher.getName()))
                .andDo(print());
    }

    @Test
    public void deleteBook_ShouldDelete_IfBookExists() throws Exception {
        long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        mvc.perform(delete("/books/{bookId}", id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteBook_ShouldThrowNotFountException_IfBookNotExisting() throws Exception {
        long id = 1L;

        mvc.perform(delete("/books/{bookId}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No book with id 1 was found."))

                .andDo(print());
    }

    @Test
    public void updateBook_ShouldUpdateBook_AndReturnBookDto() throws Exception {
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(officeRepository.findById(id)).thenReturn(Optional.of(office));
        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisher));
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));

        mvc.perform(put("/books/{bookId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(12)))
                .andExpect(jsonPath("$.title").value(bookDao.getTitle()))
                .andExpect(jsonPath("$.publisherDto.name").value(publisher.getName()))
                .andDo(print());
    }
}