package users.ui.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUser {
    
    @NotNull(message = "first name cannot be null")
    @Size(min = 2, message = "first name shouldn't be less than 2 characters")
    private String firstName;
    
    @NotNull(message = "last name cannot be null")
    @Size(min = 2, message = "last name shouldn't be less than 2 characters")
    private String lastName;
    
    @NotNull(message = "password cannot be null")
    @Size(min = 3, max = 16, message = "password must be greater that 3, less than 16 characters")
    private String password;
    
    @NotNull(message = "first name cannot be null")
    @Email
    private String email;
}
