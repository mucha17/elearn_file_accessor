package pl.kniotes.elearn_file_accessor.Services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.kniotes.elearn_file_accessor.Models.Image.Image;
import pl.kniotes.elearn_file_accessor.Models.Image.ImageProvider;
import pl.kniotes.elearn_file_accessor.Repositories.ImageRepository;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    public String addImage(String title, MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", "image");
        metaData.put("title", title);
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
        return id.toString();
    }

    public Image getImage(String id) {
        Image image;
        try {
            image = this.imageRepository.findImageById(id);
            if(image == null) throw new Exception("Image not found");
            if (image.getProvider() == ImageProvider.LOCAL || image.getProvider() == ImageProvider.REMOTE) {
                if(image.getUrl() == null || image.getUrl().isBlank()) throw new Exception("Image url is empty");
            } else throw new Exception("Image has an unhandled provider");
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image has an illegal provider", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return image;
    }
}
