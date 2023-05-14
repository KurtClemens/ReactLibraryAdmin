package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.UserDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;
import world.inetumrealdolmen.backendrlibrary.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "",produces = "application/json")
    public List<UserDto> getAllUsers() {return userService.getAllUsers();}

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userId}",produces = "application/json")
    public UserDto getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "",produces = "application/json")
    public UserDto addUser(@RequestBody UserDao userDao){
        return userService.createUser(userDao);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{userId}",produces = "application/json")
    public void deleteUser(@PathVariable Long userId, @RequestParam boolean booksReturned){
        userService.deleteUser(userId, booksReturned);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{userId}",produces = "application/json")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDao userDao){
        return userService.updateUser(userId, userDao);
    }
}
