package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.PublisherDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.SubjectDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.PublisherDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.SubjectDto;
import world.inetumrealdolmen.backendrlibrary.service.PublisherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publishers")
@CrossOrigin
public class PublisherController {

    @Autowired
    private final PublisherService publisherService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = "application/json")
    public List<PublisherDto> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "",produces = "application/json")
    public PublisherDto addPublisher(@RequestBody PublisherDao publisherDao){
        return publisherService.createPublisher(publisherDao);
    }
}
