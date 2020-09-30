package users.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import users.data.AlbumsServiceClient;
import users.data.UserEntity;
import users.data.UserRepository;
import users.shared.UserDto;
import users.ui.model.AlbumResponseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final RestTemplate rest;
    private final Environment env;
    private final AlbumsServiceClient albumsServiceClient;
    
    public UserDto createUser(UserDto userDetails) {
        
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(encoder.encode(userDetails.getPassword()));
        
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        
        userRepo.save(userEntity);
        
        return modelMapper.map(userEntity, UserDto.class);
    }
    
    private static User mapper_to_user(UserEntity userEntity) {
        return new User(
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .map(UserService::mapper_to_user)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User %s is not found in our system", username)
                ));
    }
    
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User %s is not found in our system", email)
                ));
        
        return new ModelMapper().map(userEntity, UserDto.class);
    }
    
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepo.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with id: %s is not found in our system", userId)
                ));
        
//        String albumsUrl = String.format(env.getProperty("albums.url"), userId);;
//        ResponseEntity<List<AlbumResponseModel>> albumsResp = rest.exchange(albumsUrl,
//                                                                        HttpMethod.GET,
//                                                                        null,
//                                                                        new ParameterizedTypeReference<>() {});
//        List<AlbumResponseModel> albums = albumsResp.getBody();
        
        log.info("Before calling albums Microservice");
        List<AlbumResponseModel> albums = albumsServiceClient.getAlbums(userId);
        log.info("After calling albums Microservice");
    
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        userDto.setAlbums(albums);
        
        return userDto;
    }
}
