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
import pl.kniotes.elearn_file_accessor.Models.Audio.Audio;
import pl.kniotes.elearn_file_accessor.Models.Audio.AudioProvider;
import pl.kniotes.elearn_file_accessor.Models.Audio.AudioType;
import pl.kniotes.elearn_file_accessor.Repositories.AudioRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(DatabaseTestConfiguration.class)
public class AudioTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private AudioRepository audioRepository;

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {
        this.mongoTemplate.dropCollection(Audio.class);
        this.gridFsTemplate.delete(new Query(Criteria.where("_id").exists(true)));
    }

//    @Test
//    public void addLocalAudioAndGetUrl() throws FileNotFoundException {
//        InputStream inputStream = new FileInputStream("src/test/resources/test.mp3");
//        DBObject metaData = new BasicDBObject();
//        metaData.put("title", "Tytuł klipu audio");
//        String id = gridFsTemplate.store(inputStream, "test.mp3", "audio/mpeg", metaData).toString();
//        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
//        Assertions.assertNotNull(file);
//        Audio audio = new Audio("test", false, AudioProvider.LOCAL, AudioType.MP3);
//        audio.setTitle(file.getMetadata().get("title").toString());
//        audio.setUrl("http://localhost:8080/api/files/" + file.getId().asObjectId().getValue().toString());
//        this.audioRepository.save(audio);
//        System.out.println(audio.getUrl());
//    }

//    @Test
//    public void addRemoteAudioAndGetUrl() {
//        Audio audio = new Audio("test", false, AudioProvider.REMOTE, AudioType.MP3);
//        audio.setTitle("Testowy klip audio");
//        audio.setUrl("https://sampleswap.org/samples-ghost/INSTRUMENTS%20(MULTISAMPLED)/GUITAR/Nylon%20Guitar%20Chords/2374[kb]NylonChordA7-01.wav.mp3");
//        String id = this.audioRepository.save(audio).getId();
//        audio = this.audioRepository.findAudioById(id);
//        Assertions.assertNotNull(audio);
//        System.out.println(audio.getUrl());
//    }
}
