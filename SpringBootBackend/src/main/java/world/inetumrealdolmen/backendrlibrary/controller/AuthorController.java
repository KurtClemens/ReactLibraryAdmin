package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.AuthorDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.SubjectDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.AuthorDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.SubjectDto;
import world.inetumrealdolmen.backendrlibrary.service.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
@CrossOrigin
public class AuthorController {

    @Autowired
    private final AuthorService authorService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = "application/json")
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "",produces = "application/json")
    public AuthorDto addAuthor(@RequestBody AuthorDao authorDao){
        return authorService.createAuthor(authorDao);
    }
}