package world.inetumrealdolmen.backendrlibrary.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.inetumrealdolmen.backendrlibrary.domain.Subject;
import world.inetumrealdolmen.backendrlibrary.domain.dao.SubjectDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.SubjectDto;
import world.inetumrealdolmen.backendrlibrary.exception.AlreadyExistsException;
import world.inetumrealdolmen.backendrlibrary.exception.NotFoundException;
import world.inetumrealdolmen.backendrlibrary.repository.SubjectRepository;
import world.inetumrealdolmen.backendrlibrary.service.SubjectService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService {
    private SubjectRepository subjectRepository;

    @Override
    public List<SubjectDto> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        if(subjects.isEmpty()){
            throw new NotFoundException("No subjects are registered in the application");
        }
        return createSubjectDtos(subjects);
    }

    @Override
    public SubjectDto createSubject(SubjectDao subjectDao) {
        Subject newSubject;
        if(subjectDao.getId() == 0) {
            newSubject = Subject.builder()
                    .technologyName(subjectDao.getTechnologyName())
                    .build();

            subjectRepository.save(newSubject);
            return createSubjectDto((newSubject));

        } else{
            throw new AlreadyExistsException(String.format("Subject with id %s already exists", subjectDao.getId()));
        }
    }

    private List<SubjectDto> createSubjectDtos(List<Subject> subjects) {
        List<SubjectDto> subjectDtos = new ArrayList<>();
        for(var subject:subjects){
            subjectDtos.add(createSubjectDto(subject));
        }
        return subjectDtos;
    }

    private SubjectDto createSubjectDto(Subject subject) {
        return SubjectDto.builder()
                .id(subject.getId())
                .technologyName(subject.getTechnologyName())
                .build();
    }

}