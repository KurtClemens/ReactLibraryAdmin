package world.inetumrealdolmen.backendrlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.inetumrealdolmen.backendrlibrary.domain.BookOnShelve;

@Repository
public interface BookOnShelveRepository extends JpaRepository<BookOnShelve, Long> {
}
