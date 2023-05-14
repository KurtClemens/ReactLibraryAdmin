package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookOnShelveDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.LoanDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.LoanDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;
import world.inetumrealdolmen.backendrlibrary.service.LoanService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
@CrossOrigin
public class LoanController {

    @Autowired
    private final LoanService loanService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "",produces = "application/json")
    public List<LoanDto> getAllLoans() {return loanService.getAllLoans();}

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{loanId}",produces = "application/json")
    public LoanDto getLoanById(@PathVariable Long loanId) {
        return loanService.getLoanById(loanId);}

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/overdue",produces = "application/json")
    public List<LoanDto> getLoansOverdue() {return loanService.getLoansOverdue();}

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/mail-user/{userEmail}",produces = "application/json")
    public void sendEmailToUser(@PathVariable String userEmail, @RequestParam String message)
    { loanService.sendEmailToUser(userEmail, message);}

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/user/{userId}", produces = "application/json")
    public List<LoanDto> getLoansByUser(@PathVariable Long userId) {
        return loanService.getLoansByUser(userId);}

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{userId}", produces = "application/json")
    public UserDto createLoanForUser(@PathVariable Long userId, @RequestBody BookOnShelveDao bookOnShelveDao){
        return loanService.createLoanForUser(userId, bookOnShelveDao);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{loanId}", produces = "application/json")
    public LoanDto updateLoan(@PathVariable Long loanId, @RequestBody LoanDao loanDao){
        return loanService.updateLoan(loanId, loanDao);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{userId}/{loanId}", produces = "application/json")
    public UserDto extendLoanForUser(@PathVariable Long userId, @PathVariable Long loanId){
        return loanService.extendLoanForUser(userId, loanId);
    }
}
