package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.BookRequestDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookRequestDto;
import world.inetumrealdolmen.backendrlibrary.service.BookRequestService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book-requests")
@CrossOrigin
public class BookRequestController {

    @Autowired
    private final BookRequestService bookRequestService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "",produces = "application/json")
    public List<BookRequestDto> getAllBookRequests() {
        return bookRequestService.getAllBookRequests();}

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{bookRequestId}",produces = "application/json")
    public BookRequestDto getBookRequestById(@PathVariable Long bookRequestId) {
        return bookRequestService.getBookRequestById(bookRequestId);}

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{userId}",produces = "application/json")
    public BookRequestDto createBookRequestForUser(@PathVariable Long userId, @RequestBody BookRequestDao bookRequestDao){
        return bookRequestService.createBookRequestForUser(userId, bookRequestDao);
    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{userId}/{bookRequestId}",produces = "application/json")
    public void deleteBookRequest(@PathVariable Long userId, @PathVariable Long bookRequestId){
        bookRequestService.deleteBookRequest(userId, bookRequestId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{bookRequestId}",produces = "application/json")
    public BookRequestDto updateBookRequest(@PathVariable Long bookRequestId, @RequestBody BookRequestDao bookRequestDao){
        return bookRequestService.updateBookRequest(bookRequestId, bookRequestDao);
    }
}
