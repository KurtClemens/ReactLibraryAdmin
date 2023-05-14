package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.Book;
import world.inetumrealdolmen.backendrlibrary.domain.User;
import world.inetumrealdolmen.backendrlibrary.domain.dao.UserDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookRequestDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.LoanDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.BookRepository;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;
import world.inetumrealdolmen.backendrlibrary.service.LoginService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {
    private UserRepository userRepository;
    private BookRepository bookRepository;
    @Override
    public UserDto login(UserDao userDao) {
        User user = userRepository.findByEmail(userDao.getEmail()).orElseThrow(()->
                new NotFoundException(String.format("No user with email %s was found.", userDao.getEmail())));
        if(user.getPassword().equals(userDao.getPassword()) && user.isActive()){
            return createUserDto(user);
        }
        throw new NotFoundException("Wrong credentials");
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
            Book book = getObjectIfObjectIsPresent(bookRepository.findById(loan.getBookOnShelve().getBooks().get(0).getId()), "Book");
            loanDtos.add(LoanDto.builder()
                    .id(loan.getId())
                    .bookTitle(loan.getBookOnShelve().getBooks().get(0).getTitle())
                    .bookOnShelveOffice(loan.getBookOnShelve().getOffice().getName())
                    .bookOnShelveId(loan.getBookOnShelve().getId())
                    .dateBorrowed(loan.getDateBorrowed())
                    .dateToReturn(loan.getDateToReturn())
                    .dateReturned(loan.getDateReturned())
                    .extended(loan.isExtended())
                    .url(book.getUrl())
                    .bookId(book.getId())
                    .build());
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .bookRequestDtos(bookRequestDtos)
                .loanDtos(loanDtos)
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }

    private static <T> T getObjectIfObjectIsPresent(Optional<T> optional, String objectName) {
        if(optional.isPresent()){
            return optional.get();
        }else{
            throw new NotFoundException(String.format("No %s was found.", objectName));
        }
    }
}
