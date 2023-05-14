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
import world.inetumrealdolmen.backendrlibrary.domain.Office;
import world.inetumrealdolmen.backendrlibrary.repository.OfficeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class OfficeControllerTests {

    @MockBean
    private OfficeRepository officeRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Office office1;
    private Office office2;
    private List<Office> offices;

    @BeforeEach
    public void setup() {
        offices = new ArrayList<>();
        office1 = Office.builder()
                .id(1L)
                .name("name 1")
                .postalCode("1111")
                .city("city 1")
                .street("street 1")
                .number("1")
                .build();
        office2 = Office.builder()
                .id(2L)
                .name("name 2")
                .postalCode("2222")
                .city("city 2")
                .street("street 2")
                .number("2")
                .build();
        offices.add(office1);
        offices.add(office2);
    }

    @Test
    public void getAllOffices_ShouldReturnAllOffices() throws Exception {
        when(officeRepository.findAll()).thenReturn(offices);

        mvc.perform(get("/offices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(office1.getName()))
                .andExpect(jsonPath("$[1].name").value(office2.getName()))
                .andDo(print());
    }

    @Test
    public void getAllOffices_ShouldThrowNotFoundException_IfNoOfficesFound() throws Exception {
        mvc.perform(get("/offices"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No offices are registered in the application."))
                .andDo(print());
    }

    @Test
    public void createOffice_ShouldCreateOffice_IfNameNotExists() throws Exception {
        mvc.perform(post("/offices").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(office1)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void createOffice_ShouldThrowAlreadyExistsException_IfNameDoesExists() throws Exception {
        when(officeRepository.findByName(office1.getName())).thenReturn(Optional.of(office1));

        mvc.perform(post("/offices").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(office1)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Office with name name 1 already exists"))
                .andDo(print());
    }

    @Test
    public void deleteOffice_ShouldDelete_IfOfficeExists() throws Exception {
        long id = 1L;

        when(officeRepository.findById(id)).thenReturn(Optional.of(office1));
        mvc.perform(delete("/offices/{officeId}", id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteOffice_ShouldThrowNotFoundException_IfOfficeNotExists() throws Exception {
        long id = 1L;

        mvc.perform(delete("/offices/{officeId}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No office with id 1 was found."))
                .andDo(print());
    }

    @Test
    public void shouldUpdateOffice_IfNewNameNotExists() throws Exception {
        long id = 1L;
        Office officeUpdated = office2;
        officeUpdated.setId(id);
        officeUpdated.setName("New Name");

        when(officeRepository.findById(id)).thenReturn(Optional.of(office1));
        when(officeRepository.save(any(Office.class))).thenReturn(officeUpdated);

        mvc.perform(put("/offices/{officeId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(officeUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(officeUpdated.getName()))
                .andExpect(jsonPath("$.city").value(officeUpdated.getCity()))
                .andDo(print());
    }

    @Test
    public void updateOffice_ShouldThrowAlreadyExistsException_IfNewNameAlreadyExists() throws Exception {
        long id = 1L;
        Office officeUpdated = office2;
        officeUpdated.setId(id);
        officeUpdated.setName("name 2");

        when(officeRepository.findById(id)).thenReturn(Optional.of(office1));
        when(officeRepository.findByName(office2.getName())).thenReturn(Optional.of(office2));
        when(officeRepository.save(any(Office.class))).thenReturn(officeUpdated);

        mvc.perform(put("/offices/{officeId}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(officeUpdated)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Office with name name 2 already exists"))
                .andDo(print());
    }

}
