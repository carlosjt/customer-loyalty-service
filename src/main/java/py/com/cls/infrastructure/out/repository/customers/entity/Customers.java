package py.com.cls.infrastructure.out.repository.customers.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_customers")
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotEmpty(message = "First name may not be empty")
    @Size(max = 50, message = "First name may not exceed 50 characters")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotEmpty(message = "Last name may not be empty")
    @Size(max = 50, message = "Last name may not exceed 50 characters")
    private String lastName;

    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    @NotEmpty(message = "Document number may not be empty")
    @Size(max = 20, message = "Document number may not exceed 20 characters")
    private String documentNumber;

    @Column(name = "document_type", nullable = false, length = 10)
    @NotEmpty(message = "Document type may not be empty")
    @Pattern(regexp = "CI|RUC|PASSPORT", message = "Document type must be CI, RUC, or PASSPORT")
    private String documentType;

    @Column(name = "nationality", length = 50)
    @Size(max = 50, message = "Nationality may not exceed 50 characters")
    private String nationality;

    @Column(name = "email", unique = true, length = 100)
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email may not exceed 100 characters")
    private String email;

    @Column(name = "phone", length = 20)
    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Phone number format is invalid")
    @Size(max = 20, message = "Phone number may not exceed 20 characters")
    private String phone;

    @Column(name = "birth_date")
    @Past(message = "Birthdate must be a past date")
    private LocalDate birthDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
