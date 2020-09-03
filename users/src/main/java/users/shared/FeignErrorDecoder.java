package users.shared;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {
	
	private Environment env;

	@Override
	public Exception decode(String methodKey, Response response) {
		switch (response.status()) {
		case 400:
			// Do something
			// return new BadRequestException();
			break;
		case 404: {
			if (methodKey.contains("getAlbums")) {
				return new ResponseStatusException(
						HttpStatus.valueOf(response.status()),
						env.getProperty("albums.exceptions.albums-not-found")
				);
			}
			break;
		}
		default:
			return new Exception(response.reason());
		}
		return null;
	}

}
