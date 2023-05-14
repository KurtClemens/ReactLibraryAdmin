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
@Table(name = "publisher")
@Data
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String name;
    @ToString.Exclude

    @OneToMany(mappedBy ="publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public void addBook(Book newBook){
        newBook.setPublisher(this);
        books.add(newBook);
    }
}
