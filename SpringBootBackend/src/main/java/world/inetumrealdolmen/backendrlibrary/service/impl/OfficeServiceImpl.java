package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.Book;
import world.inetumrealdolmen.backendrlibrary.domain.Office;
import world.inetumrealdolmen.backendrlibrary.domain.dao.OfficeDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.OfficeDto;
import world.inetumrealdolmen.backendrlibrary.exception.AlreadyExistsException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.OfficeRepository;
import world.inetumrealdolmen.backendrlibrary.service.OfficeService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class OfficeServiceImpl implements OfficeService {
    private final OfficeRepository officeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OfficeDto> getAllOffices() {
        List<Office> offices = officeRepository.findAll();
        if (offices.isEmpty()){
            throw new NotFoundException("No offices are registered in the application.");
        }
        return createOfficeDtos(offices);
    }

    @Override
    public OfficeDto createOffice(OfficeDao officeDao) {
        Office office = officeRepository.findByName(officeDao.getName()).orElse(null);
        if(office != null){
            throw new AlreadyExistsException(String.format("Office with name %s already exists", officeDao.getName()));
        }else{
            Office newOffice = Office.builder()
                    .name(officeDao.getName())
                    .postalCode(officeDao.getPostalCode())
                    .city(officeDao.getCity())
                    .street(officeDao.getStreet())
                    .number(officeDao.getNumber())
                    .booksOnShelve(new ArrayList<>())
                    .build();
            officeRepository.save(newOffice);
            return createOfficeDto(newOffice);
        }
    }

    @Override
    public void deleteOffice(Long officeId) {
        Office office = officeById(officeId);
        officeRepository.delete(office);
    }

    @Override
    public OfficeDto updateOffice(Long officeId, OfficeDao officeDao) {
        Office officeUpdate = officeById(officeId);

        Office officeWithExistingname = officeRepository.findByName(officeDao.getName()).orElse(null);
        if(officeWithExistingname != null) {
            throw new AlreadyExistsException(String.format("Office with name %s already exists", officeDao.getName()));
        }
        officeUpdate.setName(officeDao.getName());
        officeUpdate.setPostalCode(officeDao.getPostalCode());
        officeUpdate.setCity(officeDao.getCity());
        officeUpdate.setStreet(officeDao.getStreet());
        officeUpdate.setNumber(officeDao.getNumber());
        return createOfficeDto(officeUpdate);
    }

    @Override
    public OfficeDto getOfficeById(Long officeId) {
        Office office = officeRepository.findById(officeId).orElseThrow(()->
                new NotFoundException(String.format("No office with id %s was found.", officeId)));
        return createOfficeDto(office);
    }

    private OfficeDto createOfficeDto(Office office) {
        return OfficeDto.builder()
                .id(office.getId())
                .name(office.getName())
                .postalCode(office.getPostalCode())
                .city(office.getCity())
                .street(office.getStreet())
                .number(office.getNumber())
                .build();
    }

    private List<OfficeDto> createOfficeDtos(List<Office> offices) {
        List<OfficeDto> officeDtos = new ArrayList<>();
        for(var office:offices){
            officeDtos.add(createOfficeDto(office));
        }
        return officeDtos;
    }

    private Office officeById(Long officeId) {
        return officeRepository.findById(officeId).orElseThrow(()->
                new NotFoundException(String.format("No office with id %s was found.", officeId)));
    }
}
