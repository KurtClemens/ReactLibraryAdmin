package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.OfficeDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.BookDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.OfficeDto;
import world.inetumrealdolmen.backendrlibrary.service.OfficeService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/offices")
@CrossOrigin
public class OfficeController {

    @Autowired
    private final OfficeService officeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "",produces = "application/json")
    public List<OfficeDto> getAllOffices() {return officeService.getAllOffices();}

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{officeId}",produces = "application/json")
    public OfficeDto getOfficeById(@PathVariable Long officeId){
        return officeService.getOfficeById(officeId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "",produces = "application/json")
    public OfficeDto addOffice(@RequestBody OfficeDao officeDao){
        return officeService.createOffice(officeDao);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{officeId}",produces = "application/json")
    public void deleteOffice(@PathVariable Long officeId){
        officeService.deleteOffice(officeId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{officeId}",produces = "application/json")
    public OfficeDto updateOffice(@PathVariable Long officeId, @RequestBody OfficeDao officeDao){
        return officeService.updateOffice(officeId, officeDao);
    }
}
