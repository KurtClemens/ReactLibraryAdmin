package world.inetumrealdolmen.backendrlibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import world.inetumrealdolmen.backendrlibrary.domain.dao.OfficeDao;
import world.inetumrealdolmen.backendrlibrary.domain.dao.SubjectDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.OfficeDto;
import world.inetumrealdolmen.backendrlibrary.domain.dto.SubjectDto;
import world.inetumrealdolmen.backendrlibrary.service.SubjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subjects")
@CrossOrigin
public class SubjectController {

    @Autowired
    private final SubjectService subjectService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = "application/json")
    public List<SubjectDto> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "",produces = "application/json")
    public SubjectDto addSubject(@RequestBody SubjectDao subjectDao){
        return subjectService.createSubject(subjectDao);
    }

}