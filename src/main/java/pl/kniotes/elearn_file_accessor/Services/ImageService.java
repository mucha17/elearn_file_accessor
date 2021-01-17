package pl.kniotes.elearn_file_accessor.Services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.kniotes.elearn_file_accessor.Exceptions.FileHasNoContent;
import pl.kniotes.elearn_file_accessor.Models.Image.Image;
import pl.kniotes.elearn_file_accessor.Models.Image.ImageProvider;
import pl.kniotes.elearn_file_accessor.Models.Image.ImageType;
import pl.kniotes.elearn_file_accessor.Repositories.ImageRepository;

import java.io.IOException;
import java.util.Objects;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private Environment env;

    public Image addImage(String title, String description, String owner, Boolean isPrivate, MultipartFile file, String url) {
        Image image;
        try {
            image = new Image();
            if (title != null && !title.isBlank()) {
                image.setTitle(title);
            }

            if (description != null && !description.isBlank()) {
                image.setDescription(description);
            }

            if (url != null && !url.isBlank()) {
                image.setProvider(ImageProvider.REMOTE);
                image.setUrl(url);
                if (url.contains(".png")) {
                    image.setType(ImageType.PNG);
                } else if (url.contains(".jpeg") || url.contains(".jpg")) {
                    image.setType(ImageType.JPEG);
                } else if (url.contains(".svg")) {
                    image.setType(ImageType.SVG);
                } else if (url.contains(".bmp")) {
                    image.setType(ImageType.BMP);
                } else if (url.contains(".gif")) {
                    image.setType(ImageType.GIF);
                }
            } else if (file != null && !file.isEmpty()) {
                image.setProvider(ImageProvider.LOCAL);
                DBObject metaData = new BasicDBObject();
                metaData.put("title", title);
                try {
                    file.getInputStream();
                } catch (IOException exception) {
                    throw new FileHasNoContent("File is empty");
                }
                ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
                if (file.getContentType() != null && !file.getContentType().isBlank()) {
                    String contentType = file.getContentType();
                    switch (contentType) {
                        case "image/png" -> image.setType(ImageType.PNG);
                        case "image/jpeg" -> image.setType(ImageType.JPEG);
                        case "image/svg+xml" -> image.setType(ImageType.SVG);
                        case "image/bmp" -> image.setType(ImageType.BMP);
                        case "image/gif" -> image.setType(ImageType.GIF);
                    }
                }
                String hyperlink = Objects.requireNonNull(env.getProperty("app.hyperlink"));
                image.setUrl(hyperlink + "api/files/" + id.toString());
            }

            if (owner != null && !owner.isBlank()) {
                image.setOwner(owner);
            }

            if (isPrivate != null) {
                image.setIsPrivate(isPrivate);
            }
            this.imageRepository.save(image);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return image;
    }

    public Image getImage(String id) {
        Image image;
        try {
            image = this.imageRepository.findImageById(id);
            if (image == null) throw new Exception("Image not found");
            if (image.getProvider() == ImageProvider.LOCAL || image.getProvider() == ImageProvider.REMOTE) {
                if (image.getUrl() == null || image.getUrl().isBlank()) throw new Exception("Image url is empty");
            } else throw new Exception("Image has an unhandled provider");
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image has an illegal provider", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return image;
    }

    public Image removeImage(String id) {
        Image image;
        try {
            image = this.imageRepository.findImageById(id);
            if (image == null) throw new Exception("Image not found");
            if (image.getProvider() == ImageProvider.LOCAL) {
                String hyperlink = Objects.requireNonNull(env.getProperty("app.hyperlink"));
                String fileId = image.getUrl().split(hyperlink + "api/files/")[1];
                this.gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
            }
            if (this.imageRepository.deleteImageById(image.getId()) == null) {
                throw new Exception("Image not found");
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return image;
    }
}
