package world.inetumrealdolmen.backendrlibrary.service;

import world.inetumrealdolmen.backendrlibrary.domain.dao.SubjectDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.SubjectDto;

import java.util.List;

public interface SubjectService {
    List<SubjectDto> getAllSubjects();

    SubjectDto createSubject(SubjectDao subjectDao);
}
