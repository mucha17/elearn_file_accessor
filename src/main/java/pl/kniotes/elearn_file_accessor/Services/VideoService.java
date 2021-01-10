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
import pl.kniotes.elearn_file_accessor.Models.Video.Video;
import pl.kniotes.elearn_file_accessor.Models.Video.VideoProvider;
import pl.kniotes.elearn_file_accessor.Repositories.VideoRepository;

import java.io.IOException;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    public String addVideo(String title, MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", "video");
        metaData.put("title", title);
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
        return id.toString();
    }

    public Video getVideo(String id) {
        Video video;
        try {
            video = this.videoRepository.findVideoById(id);
            if (video == null) throw new Exception("Video not found");
            if (video.getProvider() == VideoProvider.LOCAL || video.getProvider() == VideoProvider.REMOTE) {
                if (video.getUrl() == null || video.getUrl().isBlank()) throw new Exception("Video url is empty");
            } else throw new Exception("Video has an unhandled provider");
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Video has an illegal provider", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return video;
    }
}
