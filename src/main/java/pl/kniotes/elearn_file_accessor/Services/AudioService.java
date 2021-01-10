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
import pl.kniotes.elearn_file_accessor.Models.Audio.Audio;
import pl.kniotes.elearn_file_accessor.Models.Audio.AudioProvider;
import pl.kniotes.elearn_file_accessor.Repositories.AudioRepository;

import java.io.IOException;

@Service
public class AudioService {

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    public String addAudio(String title, MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", "video");
        metaData.put("title", title);
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
        return id.toString();
    }

    public Audio getAudio(String id) {
        Audio audio;
        try {
            audio = this.audioRepository.findAudioById(id);
            if (audio == null) throw new Exception("Video not found");
            if (audio.getProvider() == AudioProvider.LOCAL || audio.getProvider() == AudioProvider.REMOTE) {
                if (audio.getUrl() == null || audio.getUrl().isBlank()) throw new Exception("Audio url is empty");
            } else throw new Exception("Audio has an unhandled provider");
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Audio has an illegal provider", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return audio;
    }
}
