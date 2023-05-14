package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.Book;
import world.inetumrealdolmen.backendrlibrary.domain.BookOnShelve;
import world.inetumrealdolmen.backendrlibrary.domain.Loan;
import world.inetumrealdolmen.backendrlibrary.domain.User;
import world.inetumrealdolmen.backendrlibrary.domain.dao.UserDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookRequestDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.LoanDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;
import world.inetumrealdolmen.backendrlibrary.exception.AlreadyExistsException;
import world.inetumrealdolmen.backendrlibrary.exception.EmailException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;
import world.inetumrealdolmen.backendrlibrary.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final String EMAIL_DOMAIN_INETUM = "@inetum-realdolmen.world";
    private final String EMAIL_DOMAIN_REALDOLMEN = "@realdolmen.com";


    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new NotFoundException("No users are registered in the application");
        }
        return createUserDtos(users);
    }

    @Override
    public UserDto createUser(UserDao userDao) {
        User user = userRepository.findByEmail(userDao.getEmail()).orElse(null);

        if (!userDao.getEmail().endsWith(EMAIL_DOMAIN_REALDOLMEN) && !userDao.getEmail().endsWith(EMAIL_DOMAIN_INETUM)) {
            throw new EmailException(String.format("Email domain is not correct. Use an email with domain %s or %s", EMAIL_DOMAIN_REALDOLMEN, EMAIL_DOMAIN_INETUM));
        }else if(user != null){
            throw new AlreadyExistsException(String.format("User with email %s already exists", userDao.getEmail()));
        }else {
            User newUser = User.builder()
                    .name(userDao.getName())
                    .firstName(userDao.getFirstName())
                    .email(userDao.getEmail())
                    .password(userDao.getPassword())
                    .role("ROLE_" + userDao.getRole().toUpperCase())
                    .loans(new ArrayList<>())
                    .bookRequests(new ArrayList<>())
                    .active(true)
                    .build();
            userRepository.save(newUser);
            return createUserDto(newUser);
        }
    }

    @Override
    public void deleteUser(Long userId, boolean booksReturned) {
        User user = findUserById(userId);
        List<Loan> loans = user.getLoans();
        if(booksReturned){
            for (Loan loan:loans){
                loan.getBookOnShelve().setAvailable(true);
                loan.setDateReturned(LocalDateTime.now());
            }
        }
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDao userDao) {
        if (!userDao.getEmail().endsWith(EMAIL_DOMAIN_REALDOLMEN) && !userDao.getEmail().endsWith(EMAIL_DOMAIN_INETUM)) {
            throw new EmailException(String.format("Email domain is not correct. Use an email with domain %s or %s", EMAIL_DOMAIN_REALDOLMEN, EMAIL_DOMAIN_INETUM));
        }
        User userUpdate = findUserById(userId);
        userUpdate.setName(userDao.getName());
        userUpdate.setRole("ROLE_" + userDao.getRole().toUpperCase());
        userUpdate.setEmail(userDao.getEmail());
        userUpdate.setFirstName(userDao.getFirstName());
        return createUserDto(userUpdate);
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new NotFoundException(String.format("No user with id %s was found.", userId)));
        return createUserDto(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->
                new NotFoundException(String.format("No user with id %s was found.", userId)));
    }

    private List<UserDto> createUserDtos(List<User> users){
        List<UserDto> userDtos = new ArrayList<>();
        for(var user: users){
            userDtos.add(createUserDto(user));
        }
        return userDtos;
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
                    .url(book.getUrl())
                    .bookId(book.getId())
                    .build());
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .role(user.getRole())
                .bookRequestDtos(bookRequestDtos)
                .loanDtos(loanDtos)
                .active(user.isActive())
                .build();
    }
}
