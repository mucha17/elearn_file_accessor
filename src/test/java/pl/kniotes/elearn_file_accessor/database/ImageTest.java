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
import pl.kniotes.elearn_file_accessor.Models.Image.Image;
import pl.kniotes.elearn_file_accessor.Models.Image.ImageProvider;
import pl.kniotes.elearn_file_accessor.Models.Image.ImageType;
import pl.kniotes.elearn_file_accessor.Repositories.ImageRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(DatabaseTestConfiguration.class)
public class ImageTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {
        this.mongoTemplate.dropCollection(Image.class);
        this.gridFsTemplate.delete(new Query(Criteria.where("_id").exists(true)));
    }

//    @Test
//    public void addLocalImageAndGetUrl() throws FileNotFoundException {
//        InputStream inputStream = new FileInputStream("src/test/resources/test.png");
//        DBObject metaData = new BasicDBObject();
//        metaData.put("title", "Tytuł zdjęcia");
//        String id = gridFsTemplate.store(inputStream, "test.png", "image/png", metaData).toString();
//        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
//        Assertions.assertNotNull(file);
//        Image image = new Image("test", false, ImageProvider.LOCAL, ImageType.PNG);
//        image.setTitle(file.getMetadata().get("title").toString());
//        image.setUrl("http://localhost:8080/api/files/" + file.getId().asObjectId().getValue().toString());
//        this.imageRepository.save(image);
//        System.out.println(image.getUrl());
//    }

//    @Test
//    public void addRemoteImageAndGetUrl() {
//        Image image = new Image("test", false, ImageProvider.REMOTE, ImageType.SVG);
//        image.setTitle("Testowe zdjęcie");
//        image.setUrl("https://www.baeldung.com/wp-content/themes/baeldung/icon/logo.svg");
//        String id = this.imageRepository.save(image).getId();
//        image = this.imageRepository.findImageById(id);
//        Assertions.assertNotNull(image);
//        System.out.println(image.getUrl());
//    }
}
