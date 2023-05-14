package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.AuthorDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.PublisherDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.SubjectDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.*;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.*;
import world.inetumrealdolmen.backendrlibrary.service.BookService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final SubjectRepository subjectRepository;
    private final OfficeRepository officeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()){
            throw new NotFoundException("No books present in the library yet.");
        }
        return createBookDtos(books);
    }

    @Override
    public BookDto createBook(BookDao bookDao) {
        Publisher publisher = getPublisher(bookDao);

        List<Author> authors = getAuthors(bookDao);

        List<Subject> subjects = getSubjects(bookDao);

        Office office = getObjectIfObjectIsPresent(officeRepository.findById(bookDao.getOfficeDao().getId()), "Office");

        BookOnShelve newBookOnShelve = BookOnShelve.builder()
                .dateAdded(LocalDateTime.now())
                .available(true)
                .books(new ArrayList<>())
                .loans(new ArrayList<>())
                .office(office).build();

        Book newBook = Book.builder()
                .isbn(bookDao.getIsbn())
                .title(bookDao.getTitle())
                .pages(bookDao.getPages())
                .language(bookDao.getLanguage())
                .description(bookDao.getDescription())
                .publishedYear(bookDao.getPublishedYear())
                .authors(authors)
                .subjects(subjects)
                .build();

        newBookOnShelve.addBook(newBook);
        publisher.addBook(newBook);
        bookRepository.save(newBook);

        return createBookDto(newBook);
    }

    @Override
    public void deleteBook(Long bookId) {
        Book book = findBookById(bookId);
        bookRepository.delete(book);
    }

    @Override
    public BookDto updateBook(Long bookId, BookDao bookDao) {
        Book bookUpdate = findBookById(bookId);

        Publisher publisherUpdate = getPublisher(bookDao);

        List<Author> authorsUpdate = getAuthors(bookDao);

        List<Subject> subjectsUpdate = getSubjects(bookDao);

        Office officeUpdate = getObjectIfObjectIsPresent(officeRepository.findById(bookDao.getOfficeDao().getId()), "Office");

        BookOnShelve bookOnShelveUpdate = bookUpdate.getBookOnShelve();
        bookOnShelveUpdate.setOffice(officeUpdate);

        bookUpdate.setIsbn(bookDao.getIsbn());
        bookUpdate.setTitle(bookDao.getTitle());
        bookUpdate.setPages(bookDao.getPages());
        bookUpdate.setLanguage(bookDao.getLanguage());
        bookUpdate.setDescription(bookDao.getDescription());
        bookUpdate.setPublishedYear(bookDao.getPublishedYear());
        bookUpdate.setAuthors(authorsUpdate);
        bookUpdate.setSubjects(subjectsUpdate);
        bookUpdate.setPublisher(publisherUpdate);

        bookRepository.save(bookUpdate);
        return createBookDto(bookUpdate);
    }

    public BookDto getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()->
                new NotFoundException(String.format("No book with id %s was found.", bookId)));
        return createBookDto(book);
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(()->new NotFoundException(String.format("No book with id %s was found.", bookId)));
    }

    private List<BookDto> createBookDtos(List<Book> books) {
        List<BookDto> bookDtos = new ArrayList<>();
        for(var book: books){
            bookDtos.add(createBookDto(book));
        }
        return bookDtos;
    }

    private BookDto createBookDto(Book book) {
        List<AuthorDto> authorDtos = new ArrayList<>();
        for (var author: book.getAuthors()) {
            authorDtos.add(AuthorDto.builder()
                            .id(author.getId())
                            .firstName(author.getFirstName())
                            .name(author.getName())
                            .dateOfBirth(author.getDateOfBirth())
                            .build());
        }

        BookOnShelveDto bookOnShelveDto = BookOnShelveDto.builder()
                .id(book.getBookOnShelve().getId())
                .available(book.getBookOnShelve().isAvailable())
                .dateAdded(book.getBookOnShelve().getDateAdded())
                .build();

        List<SubjectDto> subjectDtos = new ArrayList<>();
        for (var subject:book.getSubjects()) {
            subjectDtos.add(SubjectDto.builder()
                            .id(subject.getId())
                            .technologyName(subject.getTechnologyName())
                            .build());
        }

        PublisherDto publisherDto = PublisherDto.builder()
                .id(book.getPublisher().getId())
                .name(book.getPublisher().getName())
                .build();

        OfficeDto officeDto = OfficeDto.builder()
                .id(book.getBookOnShelve().getOffice().getId())
                .name(book.getBookOnShelve().getOffice().getName())
                .street(book.getBookOnShelve().getOffice().getStreet())
                .number(book.getBookOnShelve().getOffice().getNumber())
                .postalCode(book.getBookOnShelve().getOffice().getPostalCode())
                .city(book.getBookOnShelve().getOffice().getCity())
                .build();

        return BookDto.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .pages(book.getPages())
                .language(book.getLanguage())
                .description(book.getDescription())
                .publishedYear(book.getPublishedYear())
                .officeDto(officeDto)
                .authorDtos(authorDtos)
                .bookOnShelveDto(bookOnShelveDto)
                .subjectDtos(subjectDtos)
                .publisherDto(publisherDto)
                .url(book.getUrl())
                .build();
    }
    private static <T> T getObjectIfObjectIsPresent(Optional<T> optional, String objectName) {
        if(optional.isPresent()){
            return optional.get();
        }else{
            throw new NotFoundException(String.format("No %s was found.", objectName));
        }
    }
    

    private List<Subject> getSubjects(BookDao bookDao) {
        List<Subject> subjects = new ArrayList<>();
        for(var subjectDao: bookDao.getSubjectDaos()){
            if(subjectDao.getId() != 0){
                Subject subject = getObjectIfObjectIsPresent(subjectRepository.findById(subjectDao.getId()), "Subject");
                subjects.add(subject);
            }else{
                Subject newSubject = createSubject(subjectDao);
                subjectRepository.save(newSubject);
                subjects.add(newSubject);
            }
        }
        return subjects;
    }

    private static Subject createSubject(SubjectDao subjectDao) {
        return Subject.builder()
                .technologyName(subjectDao.getTechnologyName())
                .build();
    }

    private List<Author> getAuthors(BookDao bookDao) {
        List<Author> authors = new ArrayList<>();
        for (var authorDao: bookDao.getAuthorDaos()) {
            if(authorDao.getId() != 0){
                Author author = getObjectIfObjectIsPresent(authorRepository.findById(authorDao.getId()), "Author");
                authors.add(author);
            }else{
                Author newAuthor = createAuthor(authorDao);
                authorRepository.save(newAuthor);
                authors.add(newAuthor);
            }
        }
        return authors;
    }

    private static Author createAuthor(AuthorDao authorDao) {
        return Author.builder()
                .firstName(authorDao.getFirstName())
                .name(authorDao.getName())
                .dateOfBirth(authorDao.getDateOfBirth())
                .books(new ArrayList<>())
                .build();
    }

    private Publisher getPublisher(BookDao bookDao) {
        Publisher publisher;

        if(bookDao.getPublisherDao().getId() != 0){
            publisher = getObjectIfObjectIsPresent(publisherRepository.findById(bookDao.getPublisherDao().getId()), "Publisher");
        }else{
            Publisher newPublisher = createPublisher(bookDao.getPublisherDao());
            publisherRepository.save(newPublisher);
            publisher = newPublisher;
        }
        return publisher;
    }

    private static Publisher createPublisher(PublisherDao publisherDao) {
        return Publisher.builder()
                .name(publisherDao.getName())
                .books(new ArrayList<>())
                .build();
    }
}
