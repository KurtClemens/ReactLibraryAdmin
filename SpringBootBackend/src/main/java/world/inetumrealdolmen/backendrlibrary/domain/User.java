package world.inetumrealdolmen.backendrlibrary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String name;
    @NotBlank(message = "First name is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String firstName;
    @NotBlank(message = "Email is mandatory")
    @Column(nullable = false)
    @Size(min = 2, max = 255)
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false, length = 100)
    @Size(min = 6, max = 100)
    private String password;
    @NotBlank(message = "Role is mandatory")
    @Column(nullable = false, length = 25)
    @Size(min = 2, max = 25)
    private String role;

    @Column(nullable = false)
    private boolean active;
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<BookRequest> bookRequests;
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Loan> loans;
    public void addBookRequest(BookRequest newBookRequest){
        newBookRequest.setUser(this);
        bookRequests.add(newBookRequest);
    }

    public void removeBookRequest(BookRequest bookRequest){
        bookRequests.remove(bookRequest);
    }
    public void addLoan(Loan newLoan){
        newLoan.setUser(this);
        loans.add(newLoan);
    }
}
