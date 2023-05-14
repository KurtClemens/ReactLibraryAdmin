package world.inetumrealdolmen.backendrlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.inetumrealdolmen.backendrlibrary.domain.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
