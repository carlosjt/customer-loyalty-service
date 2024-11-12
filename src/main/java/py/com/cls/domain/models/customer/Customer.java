package py.com.cls.domain.models.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Customer {
    private Integer id;

    @NotEmpty(message = "First name may not be empty")
    @Size(max = 50, message = "First name may not exceed 50 characters")
    private String firstName;

    @NotEmpty(message = "Last name may not be empty")
    @Size(max = 50, message = "Last name may not exceed 50 characters")
    private String lastName;

    @NotEmpty(message = "Document number may not be empty")
    @Size(max = 20, message = "Document number may not exceed 20 characters")
    private String documentNumber;

    @NotEmpty(message = "Document type may not be empty")
    @Pattern(regexp = "CI|RUC|PASSPORT", message = "Document type must be CI, RUC, or PASSPORT")
    private String documentType;

    @Size(max = 50, message = "Nationality may not exceed 50 characters")
    private String nationality;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email may not exceed 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Phone number format is invalid")
    @Size(max = 20, message = "Phone number may not exceed 20 characters")
    private String phone;

    @Past(message = "Birthdate must be a past date")
    private LocalDate birthDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;}
