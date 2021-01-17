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
import pl.kniotes.elearn_file_accessor.Models.Audio.Audio;
import pl.kniotes.elearn_file_accessor.Models.Audio.AudioProvider;
import pl.kniotes.elearn_file_accessor.Models.Audio.AudioType;
import pl.kniotes.elearn_file_accessor.Repositories.AudioRepository;

import java.io.IOException;
import java.util.Objects;

@Service
public class AudioService {

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private Environment env;

    public Audio addAudio(String title, String description, String owner, Boolean isPrivate, MultipartFile file, String url) {
        Audio audio;
        try {
            audio = new Audio();
            if (title != null && !title.isBlank()) {
                audio.setTitle(title);
            }

            if (description != null && !description.isBlank()) {
                audio.setDescription(description);
            }

            if (url != null && !url.isBlank()) {
                audio.setProvider(AudioProvider.REMOTE);
                audio.setUrl(url);
                if (url.contains(".flac")) {
                    audio.setType(AudioType.FLAC);
                } else if (url.contains(".mp3")) {
                    audio.setType(AudioType.MP3);
                } else if (url.contains(".m4a")) {
                    audio.setType(AudioType.M4A);
                } else if (url.contains(".mp4")) {
                    audio.setType(AudioType.MP4);
                } else if (url.contains(".wav")) {
                    audio.setType(AudioType.WAV);
                } else if (url.contains(".wma")) {
                    audio.setType(AudioType.WMA);
                } else if (url.contains(".aac")) {
                    audio.setType(AudioType.AAC);
                }
            } else if (file != null && !file.isEmpty()) {
                audio.setProvider(AudioProvider.LOCAL);
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
                        case "audio/flac" -> audio.setType(AudioType.FLAC);
                        case "audio/mp3" -> audio.setType(AudioType.MP3);
                        case "audio/m4a" -> audio.setType(AudioType.M4A);
                        case "audio/mp4" -> audio.setType(AudioType.MP4);
                        case "audio/wav" -> audio.setType(AudioType.WAV);
                        case "audio/wma" -> audio.setType(AudioType.WMA);
                        case "audio/aac" -> audio.setType(AudioType.AAC);
                    }
                }
                String hyperlink = Objects.requireNonNull(env.getProperty("app.hyperlink"));
                audio.setUrl(hyperlink + "api/files/" + id.toString());
            }

            if (owner != null && !owner.isBlank()) {
                audio.setOwner(owner);
            }

            if (isPrivate != null) {
                audio.setIsPrivate(isPrivate);
            }
            this.audioRepository.save(audio);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return audio;
    }

    public Audio getAudio(String id) {
        Audio audio;
        try {
            audio = this.audioRepository.findAudioById(id);
            if (audio == null) throw new Exception("Audio not found");
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

    public Audio removeAudio(String id) {
        Audio audio;
        try {
            audio = this.audioRepository.findAudioById(id);
            if (audio == null) throw new Exception("Audio not found");
            if (audio.getProvider() == AudioProvider.LOCAL) {
                String hyperlink = Objects.requireNonNull(env.getProperty("app.hyperlink"));
                String fileId = audio.getUrl().split(hyperlink + "api/files/")[1];
                this.gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
            }
            if (this.audioRepository.deleteAudioById(audio.getId()) == null) {
                throw new Exception("Audio not found");
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return audio;
    }
}
