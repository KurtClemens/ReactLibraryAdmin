package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.Author;
import world.inetumrealdolmen.backendrlibrary.domain.dao.AuthorDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.AuthorDto;
import world.inetumrealdolmen.backendrlibrary.exception.AlreadyExistsException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.AuthorRepository;
import world.inetumrealdolmen.backendrlibrary.service.AuthorService;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;
    @Override
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if(authors.isEmpty()){
            throw new NotFoundException("No authors are registered in the application");
        }
        return createAuthorDtos(authors);
    }

    @Override
    public AuthorDto createAuthor(AuthorDao authorDao) {
        Author newAuthor;
        if(authorDao.getId() == 0){
            newAuthor = Author.builder()
                    .firstName(authorDao.getFirstName())
                    .name(authorDao.getName())
                    .dateOfBirth(authorDao.getDateOfBirth())
                    .books(new ArrayList<>())
                    .build();

            authorRepository.save(newAuthor);
            return createAuthorDto(newAuthor);
        }else{
            throw new AlreadyExistsException(String.format("Author with id %s already exists", authorDao.getId()));
        }
    }

    private List<AuthorDto> createAuthorDtos(List<Author> authors) {
        List<AuthorDto> publisherDtos = new ArrayList<>();
        for(var author:authors){
            publisherDtos.add(createAuthorDto(author));
        }
        return publisherDtos;
    }

    private AuthorDto createAuthorDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .firstName(author.getFirstName())
                .dateOfBirth(author.getDateOfBirth())
                .build();
    }

}