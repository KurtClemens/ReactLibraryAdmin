package world.inetumrealdolmen.backendrlibrary.service;

import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.dao.OfficeDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.OfficeDto;

import java.util.List;

public interface OfficeService {
    @Transactional(readOnly = true)
    List<OfficeDto> getAllOffices();
    OfficeDto createOffice(OfficeDao officeDao);
    void deleteOffice(Long officeId);
    OfficeDto updateOffice(Long officeId, OfficeDao officeDao);

    OfficeDto getOfficeById(Long officeId);

}
