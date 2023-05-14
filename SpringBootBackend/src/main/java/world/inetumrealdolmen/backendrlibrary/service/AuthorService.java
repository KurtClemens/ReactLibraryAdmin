package world.inetumrealdolmen.backendrlibrary.service;

import world.inetumrealdolmen.backendrlibrary.domain.dao.AuthorDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> getAllAuthors();

    AuthorDto createAuthor(AuthorDao authorDao);
}
