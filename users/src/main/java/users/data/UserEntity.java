package users.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "users")
@Data
public class UserEntity implements Serializable {
    
    private static final long serialVersionUID = 2551031886052704000L;
    
    @Id
    @GeneratedValue
    private long id;
    
    @Column(nullable = false, length = 50)
    private String firstName;
    
    @Column(nullable = false, length = 50)
    private String lastName;
    
    @Column(nullable = false, length = 120, unique = true)
    private String email;
    
    @Column(nullable = false, unique = true)
    private String userId;
    
    @Column(nullable = false, unique = true)
    private String encryptedPassword;
}
