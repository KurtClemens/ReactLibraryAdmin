package world.inetumrealdolmen.backendrlibrary.service;

import world.inetumrealdolmen.backendrlibrary.domain.dao.BookOnShelveDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.LoanDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.LoanDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;

import java.util.List;

public interface LoanService {
    List<LoanDto> getAllLoans();

    List<LoanDto> getLoansByUser(Long userId);

    UserDto createLoanForUser(Long userId, BookOnShelveDao bookOnShelveDao);

    LoanDto updateLoan(Long loanId, LoanDao loanDao);

    UserDto extendLoanForUser(Long userId, Long loanId);

    List<LoanDto> getLoansOverdue();
    LoanDto getLoanById(Long loanId);

    void sendEmailToUser(String userEmail, String message);
}
