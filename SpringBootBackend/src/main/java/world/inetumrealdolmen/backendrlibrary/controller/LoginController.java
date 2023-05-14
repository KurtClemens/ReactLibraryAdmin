package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.UserDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.UserDto;
import world.inetumrealdolmen.backendrlibrary.service.LoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    @Autowired
    private LoginService loginService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "",produces = "application/json")
    public UserDto login(@RequestBody UserDao userDao){

        return loginService.login(userDao);
    }
}
