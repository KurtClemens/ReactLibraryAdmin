package world.inetumrealdolmen.backendrlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.inetumrealdolmen.backendrlibrary.domain.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
