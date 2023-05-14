package world.inetumrealdolmen.backendrlibrary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "office")
@Data
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String name;
    @NotBlank(message = "Postal code is mandatory")
    @Column(nullable = false, length = 11)
    @Size(min = 2, max = 11)
    private String postalCode;
    @NotBlank(message = "City is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String city;
    @NotBlank(message = "Street is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String street;
    @NotBlank(message = "Number is mandatory")
    @Column(nullable = false, length = 10)
    @Size(min = 2, max = 10)
    private String number;
    @ToString.Exclude

    @OneToMany(mappedBy = "office", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookOnShelve> booksOnShelve = new ArrayList<>();

}
