package users.shared;

import lombok.Data;
import users.ui.model.AlbumResponseModel;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {
    
    private static final long serialVersionUID = -7169709935192076448L;
    
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String userId;
    private String encryptedPassword;
    private List<AlbumResponseModel> albums;
    
}
