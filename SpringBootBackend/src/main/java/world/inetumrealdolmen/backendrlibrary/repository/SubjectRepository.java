package world.inetumrealdolmen.backendrlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.inetumrealdolmen.backendrlibrary.domain.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
