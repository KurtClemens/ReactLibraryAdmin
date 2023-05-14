package world.inetumrealdolmen.backendrlibrary.service;

import org.springframework.http.ResponseEntity;
import world.inetumrealdolmen.backendrlibrary.domain.dao.UserDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;

public interface LoginService {
    UserDto login(UserDao userDao);
}
