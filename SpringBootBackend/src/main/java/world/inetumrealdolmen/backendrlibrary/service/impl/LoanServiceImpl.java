package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.Book;
import world.inetumrealdolmen.backendrlibrary.domain.BookOnShelve;
import world.inetumrealdolmen.backendrlibrary.domain.Loan;
import world.inetumrealdolmen.backendrlibrary.domain.User;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookOnShelveDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.LoanDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookRequestDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.LoanDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;
import world.inetumrealdolmen.backendrlibrary.exception.NotAvailableException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.BookOnShelveRepository;
import world.inetumrealdolmen.backendrlibrary.repository.LoanRepository;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;
import world.inetumrealdolmen.backendrlibrary.service.LoanService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
@Transactional
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookOnShelveRepository bookOnShelveRepository;

    @Override
    public List<LoanDto> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        if(loans.isEmpty()){
            throw new NotFoundException("No loans are registered in the application.");
        }
        return createLoanDtos(loans);
    }

    @Override
    public List<LoanDto> getLoansByUser(Long userId) {
        User user = getUserById(userId);
        List<Loan> userLoans = user.getLoans();
        return createLoanDtos(userLoans);
    }

    @Override
    public List<LoanDto> getLoansOverdue() {
        List<Loan> loans = loanRepository.findAll().stream().filter(loan -> loan.getDateReturned() == null && loan.getDateToReturn().isBefore(LocalDateTime.now())).toList();
        return createLoanDtos(loans);
    }

    @Override
    public LoanDto getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(()->
                new NotFoundException(String.format("No loan with id %s was found.", loanId)));
        return createLoanDto(loan);
    }

    @Override
    public void sendEmailToUser(String userEmail, String message) {
        User user = getObjectIfObjectIsPresent(userRepository.findByEmail(userEmail), "user");
        // send email to user
    }

    @Override
    public UserDto createLoanForUser(Long userId, BookOnShelveDao bookOnShelveDao) {
        User user = getUserById(userId);
        BookOnShelve bookOnShelve = getBookOnShelveById(bookOnShelveDao.getId());
        if(!bookOnShelveDao.isAvailable()){
            throw new NotAvailableException(String.format("Book with id %s is currently borrowed.", bookOnShelve.getId()));
        }else{
            bookOnShelve.setAvailable(false);
            Loan newLoan = Loan.builder()
                    .bookOnShelve(bookOnShelve)
                    .dateBorrowed(LocalDateTime.now())
                    .build();
            newLoan.setDateToReturn(newLoan.getDateBorrowed().plusMonths(1));

            user.addLoan(newLoan);

            return createUserDto(user);
        }
    }

    @Override
    public UserDto extendLoanForUser(Long userId, Long loanId) {
        User user = getUserById(userId);
        Loan loanToExtend = user.getLoans().stream().filter(loan -> loanId.equals(loan.getId())).findAny().orElse(null);
        if(loanToExtend == null) {
            throw new NotFoundException(String.format("No loan with id %s was found for user with id %s.", loanId, userId));
        }else{
            loanToExtend.setDateToReturn(loanToExtend.getDateBorrowed().plusMonths(2));
            loanToExtend.setExtended(true);
            loanRepository.save(loanToExtend);
            userRepository.save(user);
        }
        return createUserDto(user);
    }

    @Override
    public LoanDto updateLoan(Long loanId, LoanDao loanDao) {
        BookOnShelve bookOnShelve = getBookOnShelveById(loanDao.getBookOnShelveId());

        Loan loanToReturn = getObjectIfObjectIsPresent(loanRepository.findById(loanId), "loan");
            loanToReturn.setDateBorrowed(loanDao.getDateBorrowed());
            loanToReturn.setDateReturned(loanDao.getDateReturned());
            loanToReturn.setExtended(loanDao.isExtended());
            if(loanDao.getDateReturned() != null){
                bookOnShelve.setAvailable(true);
            }
            if(loanDao.isExtended()){
                loanToReturn.setExtended(true);
                loanToReturn.setDateToReturn(loanDao.getDateToReturn().plusMonths(1));
            }
            loanRepository.save(loanToReturn);
            return createLoanDto(loanToReturn);
    }

    private static <T> T getObjectIfObjectIsPresent(Optional<T> optional, String objectName) {
        if(optional.isPresent()){
            return optional.get();
        }else{
            throw new NotFoundException(String.format("No %s was found.", objectName));
        }
    }

    private BookOnShelve getBookOnShelveById(Long bookOnShelveId) {
        return bookOnShelveRepository.findById(bookOnShelveId).orElseThrow(()->
                new NotFoundException(String.format("No bookOnShelve with id %s was found.", bookOnShelveId)));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->
                new NotFoundException(String.format("No user with id %s was found.", userId)));
    }

    private List<LoanDto> createLoanDtos(List<Loan> loans) {
        List<LoanDto> loanDtos = new ArrayList<>();
        for(var loan:loans){
            loanDtos.add(createLoanDto(loan));
        }
        return loanDtos;
    }

    private LoanDto createLoanDto(Loan loan) {
        BookOnShelve bookOnShelve = loan.getBookOnShelve();
        Book book = bookOnShelve.getBooks().get(0);
        return LoanDto.builder()
                .id(loan.getId())
                .dateBorrowed(loan.getDateBorrowed())
                .dateToReturn(loan.getDateToReturn())
                .dateReturned(loan.getDateReturned())
                .extended(loan.isExtended())
                .userEmail(loan.getUser().getEmail())
                .bookIsbn(book.getIsbn())
                .bookTitle(book.getTitle())
                .bookOnShelveId(bookOnShelve.getId())
                .bookOnShelveOffice(bookOnShelve.getOffice().getName())
                .url(book.getUrl())
                .build();
    }

    private UserDto createUserDto(User user) {
        List<BookRequestDto> bookRequestDtos = new ArrayList<>();
        for (var bookRequest:user.getBookRequests()) {
            bookRequestDtos.add(BookRequestDto.builder()
                    .id(bookRequest.getId())
                    .title(bookRequest.getTitle())
                    .subject(bookRequest.getSubject())
                    .publisher(bookRequest.getPublisher())
                    .dateRequested(bookRequest.getDateRequested())
                    .approved(bookRequest.isApproved())
                    .reason(bookRequest.getReason())
                    .build());
        }

        List<LoanDto> loanDtos = new ArrayList<>();
        for (var loan:user.getLoans()) {
            Book book = loan.getBookOnShelve().getBooks().get(0);
            loanDtos.add(LoanDto.builder()
                    .id(loan.getId())
                    .dateBorrowed(loan.getDateBorrowed())
                    .dateToReturn(loan.getDateToReturn())
                    .dateReturned(loan.getDateReturned())
                    .extended(loan.isExtended())
                    .userEmail(loan.getUser().getEmail())
                    .bookIsbn(book.getIsbn())
                    .bookTitle(book.getTitle())
                    .bookOnShelveOffice(loan.getBookOnShelve().getOffice().getName())
                    .bookOnShelveId(loan.getBookOnShelve().getId())
                    .bookId(book.getId())
                    .url(book.getUrl())
                    .build());
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .bookRequestDtos(bookRequestDtos)
                .loanDtos(loanDtos).build();
    }
}
