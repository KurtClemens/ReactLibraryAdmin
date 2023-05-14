package world.inetumrealdolmen.backendrlibrary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookonshelve")
@Data
public class BookOnShelve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
//    @NotBlank(message = "Date added is mandatory")

    @Column(nullable = false)
    private LocalDateTime dateAdded;

    @NotNull(message = "Available is mandatory")
    @Column(nullable = false)
    private boolean available;
//    private String barcode;
    @ToString.Exclude
    @OneToMany(mappedBy = "bookOnShelve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;
    @ToString.Exclude
    @OneToMany(mappedBy = "bookOnShelve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Office office;

    public void addBook(Book newBook){
        newBook.setBookOnShelve(this);
        books.add(newBook);
    }
}
