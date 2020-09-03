package albums.io.controllers;

import albums.data.AlbumEntity;
import albums.service.AlbumsService;
import albums.ui.model.AlbumResponseModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/users/{id}/albums")
public class AlbumsController {
    
    private final AlbumsService albumsService;
    
    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            })
    public List<AlbumResponseModel> userAlbums(@PathVariable String id) {
        
        List<AlbumResponseModel> returnValue = new ArrayList<>();
        
        List<AlbumEntity> albumsEntities = albumsService.getAlbums(id);
        
        if (albumsEntities == null || albumsEntities.isEmpty()) {
            return returnValue;
        }
        
        Type listType = new TypeToken<List<AlbumResponseModel>>() {
        }.getType();
        
        returnValue = new ModelMapper().map(albumsEntities, listType);
        
        log.info("Returning " + returnValue.size() + " albums");
        return returnValue;
    }
}
