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
import pl.kniotes.elearn_file_accessor.Models.Document.Document;
import pl.kniotes.elearn_file_accessor.Models.Document.DocumentProvider;
import pl.kniotes.elearn_file_accessor.Repositories.DocumentRepository;

import java.io.IOException;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    public String addDocument(String title, MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("type", "video");
        metaData.put("title", title);
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
        return id.toString();
    }

    public Document getDocument(String id) {
        Document document;
        try {
            document = this.documentRepository.findDocumentById(id);
            if (document == null) throw new Exception("Video not found");
            if (document.getProvider() == DocumentProvider.LOCAL || document.getProvider() == DocumentProvider.REMOTE) {
                if (document.getUrl() == null || document.getUrl().isBlank()) throw new Exception("Document url is empty");
            } else throw new Exception("Document has an unhandled provider");
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document has an illegal provider", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return document;
    }
}
