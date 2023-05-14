package world.inetumrealdolmen.backendrlibrary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookrequest")
@Data
public class BookRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank(message = "Title is mandatory")
    @Column(nullable = false, length = 100)
    @Size(min = 2, max = 100)
    private String title;
    @NotBlank(message = "Publisher is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String publisher;
    @NotBlank(message = "Subject is mandatory")
    @Column(nullable = false, length = 100)
    @Size(min = 2, max = 100)
    private String subject;
    @NotBlank(message = "Author is mandatory")
    @Column(nullable = false, length = 100)
    @Size(min = 2, max = 100)
    private String author;
    @Past(message = "Date cannot be in the future")
    @Column(nullable = false)
    private LocalDateTime dateRequested;
    @NotNull(message = "Approved is mandatory")
    @Column(nullable = false)
    private boolean approved;
    @NotBlank(message = "Reason is mandatory")
    @Column(nullable = false)
    @Size(min = 2, max = 255)
    private String reason;
    @NotNull(message = "Mail send is mandatory")
    @Column(nullable = false)
    private boolean mailSend;
    @NotNull(message = "Purchased is mandatory")
    @Column(nullable = false)
    private boolean purchased;

    @ManyToOne(fetch=FetchType.LAZY)
    @ToString.Exclude
    private User user;
}
