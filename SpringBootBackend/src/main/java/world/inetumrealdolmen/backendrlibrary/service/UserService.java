package world.inetumrealdolmen.backendrlibrary.service;

import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.dao.UserDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;

import java.util.List;

public interface UserService {
    @Transactional(readOnly = true)
    List<UserDto> getAllUsers();
    UserDto createUser(UserDao userDao);
    void deleteUser(Long userId, boolean booksReturned);
    UserDto updateUser(Long userId, UserDao userDao);

    UserDto getUserById(Long userId);
}
