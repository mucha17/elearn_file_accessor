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
import pl.kniotes.elearn_file_accessor.Models.Document.Document;
import pl.kniotes.elearn_file_accessor.Models.Document.DocumentProvider;
import pl.kniotes.elearn_file_accessor.Models.Document.DocumentType;
import pl.kniotes.elearn_file_accessor.Repositories.DocumentRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(DatabaseTestConfiguration.class)
public class DocumentTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {
        this.mongoTemplate.dropCollection(Document.class);
        this.gridFsTemplate.delete(new Query(Criteria.where("_id").exists(true)));
    }

    @Test
    public void addLocalImageAndGetUrl() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("src/test/resources/test.pdf");
        DBObject metaData = new BasicDBObject();
        metaData.put("title", "Tytuł dokumentu");
        String id = gridFsTemplate.store(inputStream, "test.png", "image/png", metaData).toString();
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        Assertions.assertNotNull(file);
        Document document = new Document("test", false, DocumentProvider.REMOTE, DocumentType.PDF);
        document.setTitle(file.getMetadata().get("title").toString());
        document.setUrl("http://localhost:8080/api/files/" + file.getId().asObjectId().getValue().toString());
        this.documentRepository.save(document);
        System.out.println(document.getUrl());
    }

    @Test
    public void addRemoteImageAndGetUrl() {
        Document document = new Document("test", false, DocumentProvider.REMOTE, DocumentType.PDF);
        document.setTitle("Testowy dokument");
        document.setUrl("we.umg.edu.pl/sites/default/files/zalaczniki/umg-informator-2020.pdf");
        String id = this.documentRepository.save(document).getId();
        document = this.documentRepository.findDocumentById(id);
        Assertions.assertNotNull(document);
        System.out.println(document.getUrl());
    }
}
