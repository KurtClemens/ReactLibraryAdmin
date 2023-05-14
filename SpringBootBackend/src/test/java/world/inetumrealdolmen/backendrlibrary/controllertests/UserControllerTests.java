package world.inetumrealdolmen.backendrlibrary.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import world.inetumrealdolmen.backendrlibrary.domain.User;
import world.inetumrealdolmen.backendrlibrary.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTests {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;
    private List<User> users;

    @BeforeEach
    public void setup(){
        users = new ArrayList<>();
        user1 = User.builder()
                .id(1L)
                .firstName("TestFirstName")
                .name("TestName")
                .email("test@inetum-realdolmen.world")
                .password("test")
                .role("ROLE_ADMIN")
                .loans(new ArrayList<>())
                .bookRequests(new ArrayList<>())
                .build();
        user2 = User.builder()
                .id(2L)
                .firstName("TestFirstName2")
                .name("TestName2")
                .email("test2@inetum-realdolmen.world")
                .password("test")
                .role("ROLE_ADMIN")
                .loans(new ArrayList<>())
                .bookRequests(new ArrayList<>())
                .build();
        users.add(user1);
        users.add(user2);

    }

    @Test
    public void getAllUsers_ShouldReturnAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
                .andDo(print());
    }

    @Test
    public void getUserById_ShouldReturnUser() throws Exception {
        long id = 1L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));

        mvc.perform(get("/users/{userId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user1.getFirstName()))
                .andDo(print());
    }

    @Test
    public void getAllUsers_ShouldThrowNotFoundException_IfNoUsersAreRegistered() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No users are registered in the application"))
                .andDo(print());
    }

    @Test
    public void createUser_ShouldAddUser_IfEmailDomainIsCorrectAndEmailNotExists() throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void createUser_ShouldThrowEmailException_IfEmailDomainIsNotCorrect() throws Exception {
        user1.setEmail("test@test.be");
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email domain is not correct. Use an email with domain @realdolmen.com or @inetum-realdolmen.world"))
                .andDo(print());
    }

    @Test
    public void createUser_ShouldThrowAlreadyExistsException_IfEmailExists() throws Exception {
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User with email "+ user1.getEmail()+" already exists"))
                .andDo(print());
    }

    @Test
    public void deleteUser_ShouldPlaceOnInactive_AndReturnBooks() throws Exception {
        long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        mvc.perform(delete("/users/{userId}", id).param("booksReturned", String.valueOf(true)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteUser_ShouldPlaceOnInactive_AndNotReturnBooks() throws Exception {
        long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        mvc.perform(delete("/users/{userId}", id).param("booksReturned", String.valueOf(false)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldUpdateUser_IfEmailNotExistsAndEmailDomainIsCorrect() throws Exception {
        long id = 1L;
        User userUpdated = user2;
        userUpdated.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(userUpdated);

        mvc.perform(put("/users/{userId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userUpdated.getEmail()))
                .andExpect(jsonPath("$.id").value(userUpdated.getId()))
                .andDo(print());
    }
    @Test
    public void updateUser_ShouldThrowEmailException_IfEmailDomainIsNotCorrect() throws Exception {
        user1.setEmail("test@test.be");
        long id = 1L;
        User userUpdated = user2;
        userUpdated.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(userUpdated);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email domain is not correct. Use an email with domain @realdolmen.com or @inetum-realdolmen.world"))
                .andDo(print());
    }

    @Test
    public void updateUser_ShouldThrowAlreadyExistsException_IfEmailExists() throws Exception {
        User userUpdated = user2;
        userUpdated.setEmail(user1.getEmail());

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(userUpdated);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User with email "+ user1.getEmail()+" already exists"))
                .andDo(print());
    }
}
