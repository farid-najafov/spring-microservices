package users.data;

import feign.FeignException;
import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import users.ui.model.AlbumResponseModel;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {
    
    @GetMapping("/users/{id}/albums")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {
    
    @Override
    public AlbumsServiceClient create(Throwable cause) {
        return new AlbumsServiceClientFallback(cause);
    }
    
}

@Log4j2
@AllArgsConstructor
class AlbumsServiceClientFallback implements AlbumsServiceClient {
    
    private final Throwable cause;
    
    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        
        if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
            log.error("404 error took place when getAlbums was called with userId: " + id + ". Error message: "
                    + cause.getLocalizedMessage());
        } else {
            log.error("Other error took place: " + cause.getLocalizedMessage());
        }
        
        return new ArrayList<>();
    }
    
}
