package world.inetumrealdolmen.backendrlibrary.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import world.inetumrealdolmen.backendrlibrary.domain.Office;
import world.inetumrealdolmen.backendrlibrary.domain.dao.OfficeDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.OfficeDto;
import world.inetumrealdolmen.backendrlibrary.exception.AlreadyExistsException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.OfficeRepository;
import world.inetumrealdolmen.backendrlibrary.service.impl.OfficeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfficeServiceImplTests {

    @Mock
    private OfficeRepository officeRepository;
    @InjectMocks
    private OfficeServiceImpl officeServiceImpl;

    private OfficeDao officeDao;
    private Office office1;
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
        Office office2 = Office.builder()
                .id(2L)
                .name("name 2")
                .postalCode("2222")
                .city("city 2")
                .street("street 2")
                .number("2")
                .build();
        offices.add(office1);
        offices.add(office2);

        officeDao = OfficeDao.builder()
                .name("New Name")
                .city("New City")
                .postalCode("1234")
                .street("New Street")
                .number("12")
                .build();
    }

    @Test
    public void getAllOffices_IfOfficesAreRegistered(){

        when(officeRepository.findAll()).thenReturn(offices);

        List<OfficeDto> officeDtos = officeServiceImpl.getAllOffices();

        assertNotNull(officeDtos);
        assertEquals("name 1", officeDtos.get(0).getName());
        assertEquals(2, officeDtos.size());
    }

    @Test
    public void getAllOffices_ShouldThrowNotFoundException_IfNoUserAreRegistered(){

        when(officeRepository.findAll()).thenReturn(new ArrayList<>());
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> officeServiceImpl.getAllOffices());

        String expectedMessage = "No offices are registered in the application.";
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> officeServiceImpl.getAllOffices());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenOfficeDao_SaveOfficeIfNameNotExists_ThenReturnOfficeDto(){

        OfficeDto officeDto = officeServiceImpl.createOffice(officeDao);

        assertNotNull(officeDto);
        assertEquals(officeDao.getName(), officeDto.getName());
    }

    @Test
    public void givenOfficeDao_SaveShouldThrowAlreadyExistsException_IfOfficeWithNameExists(){

        when(officeRepository.findByName(anyString())).thenReturn(Optional.ofNullable(office1));

        officeDao.setName("name 1");
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, ()-> officeServiceImpl.createOffice(officeDao));

        String expectedMessage = String.format("Office with name %s already exists", officeDao.getName());
        String actualMessage = exception.getMessage();

        assertThrows(AlreadyExistsException.class, ()-> officeServiceImpl.createOffice(officeDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenOfficeId_DeleteShouldThrowNotFoundException_IfOfficeNotExists(){
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> officeServiceImpl.deleteOffice(1L));

        String expectedMessage = String.format("No office with id %s was found.", 1L);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> officeServiceImpl.deleteOffice(1L));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenOfficeId_ShouldDeleteOffice_IfExists(){
        when(officeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(office1));

        officeServiceImpl.deleteOffice(office1.getId());
        verify(officeRepository, times(1)).delete(office1);
    }

    @Test
    public void givenOfficeIdAndOfficeDao_ShouldUpdateOffice_ThenReturnOfficeDto(){
        when(officeRepository.findById(1L)).thenReturn(Optional.ofNullable(office1));

        OfficeDto officeDto = officeServiceImpl.updateOffice(1L, officeDao);

        assertEquals(officeDto.getName(),officeDao.getName());
    }

    @Test
    public void givenOfficeIdAndOfficeDao_UpdateShouldThrowAlreadyExistsException_IfOfficeWithNameExists() {
        when(officeRepository.findById(office1.getId())).thenReturn(Optional.of(office1));
        when(officeRepository.findByName(anyString())).thenReturn(Optional.ofNullable(office1));
        officeDao.setName("name 1");
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> officeServiceImpl.updateOffice(1L, officeDao));

        String expectedMessage = String.format("Office with name %s already exists", officeDao.getName());
        String actualMessage = exception.getMessage();

        assertThrows(AlreadyExistsException.class, () -> officeServiceImpl.createOffice(officeDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void givenOfficeIdAndOfficeDao_UpdateShouldThrowNotFoundException_IfOfficeNotExists(){
        NotFoundException exception = assertThrows(NotFoundException.class, ()-> officeServiceImpl.updateOffice(1L, officeDao));

        String expectedMessage = String.format("No office with id %s was found.", 1L);
        String actualMessage = exception.getMessage();

        assertThrows(NotFoundException.class, ()-> officeServiceImpl.updateOffice(1L, officeDao));
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
