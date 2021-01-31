package pl.kniotes.elearn_file_accessor.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.kniotes.elearn_file_accessor.DatabaseTestConfiguration;
import pl.kniotes.elearn_file_accessor.Models.Video.Video;
import pl.kniotes.elearn_file_accessor.Models.Video.VideoProvider;
import pl.kniotes.elearn_file_accessor.Models.Video.VideoType;
import pl.kniotes.elearn_file_accessor.Repositories.VideoRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(DatabaseTestConfiguration.class)
public class VideoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private VideoRepository videoRepository;

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {
        this.mongoTemplate.dropCollection(Video.class);
        this.gridFsTemplate.delete(new Query(Criteria.where("_id").exists(true)));
    }

//    @Test
//    public void addLocalVideoAndGetUrl() throws FileNotFoundException {
//        InputStream inputStream = new FileInputStream("src/test/resources/test.mp4");
//        DBObject metaData = new BasicDBObject();
//        metaData.put("title", "Tytuł video");
//        String id = gridFsTemplate.store(inputStream, "test.mp4", "video/mp4", metaData).toString();
//        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
//        Assertions.assertNotNull(file);
//        Video video = new Video("test", false, VideoProvider.LOCAL, VideoType.MP4);
//        video.setTitle(file.getMetadata().get("title").toString());
//        video.setUrl("http://localhost:8080/api/files/" + file.getId().asObjectId().getValue().toString());
//        this.videoRepository.save(video);
//        System.out.println(video.getUrl());
//    }

//    @Test
//    public void addYoutubeVideoAndGetUrl() {
//        Video video = new Video("test", false, VideoProvider.YOUTUBE, VideoType.MP4);
//        video.setTitle("Testowe video");
//        video.setUrl("https://youtu.be/OgfyAZJQFhg?list=PL5jZApaR4GDPj2TzKPGu5vmqRY52c4lMp");
//        String id = this.videoRepository.save(video).getId();
//        video = this.videoRepository.findVideoById(id);
//        Assertions.assertNotNull(video);
//        System.out.println(video.getUrl());
//    }
}
