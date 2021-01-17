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
import pl.kniotes.elearn_file_accessor.Models.Video.Video;
import pl.kniotes.elearn_file_accessor.Models.Video.VideoProvider;
import pl.kniotes.elearn_file_accessor.Models.Video.VideoType;
import pl.kniotes.elearn_file_accessor.Repositories.VideoRepository;

import java.io.IOException;
import java.util.Objects;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private Environment env;

    public Video addVideo(String title, String description, String owner, Boolean isPrivate, MultipartFile file, String url) {
        Video video;
        try {
            video = new Video();
            if (title != null && !title.isBlank()) {
                video.setTitle(title);
            }

            if (description != null && !description.isBlank()) {
                video.setDescription(description);
            }

            if (url != null && !url.isBlank()) {
                if (url.contains("youtube.com")) {
                    video.setProvider(VideoProvider.YOUTUBE);
                } else if (url.contains("dailymotion.com")) {
                    video.setProvider(VideoProvider.DAILYMOTION);
                } else {
                    video.setProvider(VideoProvider.REMOTE);
                }
                video.setUrl(url);
                if (url.contains(".mp4")) {
                    video.setType(VideoType.MP4);
                } else if (url.contains(".avi")) {
                    video.setType(VideoType.AVI);
                } else if (url.contains(".flv")) {
                    video.setType(VideoType.FLV);
                } else if (url.contains(".mkv")) {
                    video.setType(VideoType.MKV);
                } else if (url.contains(".mov")) {
                    video.setType(VideoType.MOV);
                } else if (url.contains(".avchd")) {
                    video.setType(VideoType.AVCHD);
                } else if (url.contains(".webm")) {
                    video.setType(VideoType.WEBM);
                } else if (url.contains(".wmv")) {
                    video.setType(VideoType.WMV);
                }
            } else if (file != null && !file.isEmpty()) {
                video.setProvider(VideoProvider.LOCAL);
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
                        case "video/mp4" -> video.setType(VideoType.MP4);
                        case "video/mov" -> video.setType(VideoType.MOV);
                        case "video/wmv" -> video.setType(VideoType.WMV);
                        case "video/flv" -> video.setType(VideoType.FLV);
                        case "video/avi" -> video.setType(VideoType.AVI);
                        case "video/avchd" -> video.setType(VideoType.AVCHD);
                        case "video/webm" -> video.setType(VideoType.WEBM);
                        case "video/mkv" -> video.setType(VideoType.MKV);
                    }
                }
                String hyperlink = Objects.requireNonNull(env.getProperty("app.hyperlink"));
                video.setUrl(hyperlink + "api/files/" + id.toString());
            }

            if (owner != null && !owner.isBlank()) {
                video.setOwner(owner);
            }

            if (isPrivate != null) {
                video.setIsPrivate(isPrivate);
            }
            this.videoRepository.save(video);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return video;
    }

    public Video getVideo(String id) {
        Video video;
        try {
            video = this.videoRepository.findVideoById(id);
            if (video == null) throw new Exception("Video not found");
            if (video.getProvider() == VideoProvider.LOCAL || video.getProvider() == VideoProvider.REMOTE || video.getProvider() == VideoProvider.YOUTUBE) {
                if (video.getUrl() == null || video.getUrl().isBlank()) throw new Exception("Video url is empty");
            } else throw new Exception("Video has an unhandled provider");
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Video has an illegal provider", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return video;
    }

    public Video removeVideo(String id) {
        Video video;
        try {
            video = this.videoRepository.findVideoById(id);
            if (video == null) throw new Exception("Video not found");
            if (video.getProvider() == VideoProvider.LOCAL) {
                String hyperlink = Objects.requireNonNull(env.getProperty("app.hyperlink"));
                String fileId = video.getUrl().split(hyperlink + "api/files/")[1];
                this.gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
            }
            if (this.videoRepository.deleteVideoById(video.getId()) == null) {
                throw new Exception("Video not found");
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return video;
    }
}
