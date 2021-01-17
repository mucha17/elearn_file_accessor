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
import pl.kniotes.elearn_file_accessor.Models.Document.Document;
import pl.kniotes.elearn_file_accessor.Models.Document.DocumentProvider;
import pl.kniotes.elearn_file_accessor.Models.Document.DocumentType;
import pl.kniotes.elearn_file_accessor.Repositories.DocumentRepository;

import java.io.IOException;
import java.util.Objects;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private Environment env;

    public Document addDocument(String title, String description, String owner, Boolean isPrivate, MultipartFile file, String url) {
        Document document;
        try {
            document = new Document();
            if (title != null && !title.isBlank()) {
                document.setTitle(title);
            }

            if (description != null && !description.isBlank()) {
                document.setDescription(description);
            }

            if (url != null && !url.isBlank()) {
                document.setProvider(DocumentProvider.REMOTE);
                document.setUrl(url);
                if (url.contains(".pdf")) {
                    document.setType(DocumentType.PDF);
                } else if (url.contains(".docx")) {
                    document.setType(DocumentType.DOCX);
                } else if (url.contains(".odf")) {
                    document.setType(DocumentType.ODF);
                }
            } else if (file != null && !file.isEmpty()) {
                document.setProvider(DocumentProvider.LOCAL);
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
                        case "application/pdf" -> document.setType(DocumentType.PDF);
                        case "application/docx" -> document.setType(DocumentType.DOCX);
                        case "application/odf" -> document.setType(DocumentType.ODF);
                    }
                }
                String hyperlink = Objects.requireNonNull(env.getProperty("app.hyperlink"));
                document.setUrl(hyperlink + "api/files/" + id.toString());
            }

            if (owner != null && !owner.isBlank()) {
                document.setOwner(owner);
            }

            if (isPrivate != null) {
                document.setIsPrivate(isPrivate);
            }
            this.documentRepository.save(document);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return document;
    }

    public Document getDocument(String id) {
        Document document;
        try {
            document = this.documentRepository.findDocumentById(id);
            if (document == null) throw new Exception("Document not found");
            if (document.getProvider() == DocumentProvider.LOCAL || document.getProvider() == DocumentProvider.REMOTE) {
                if (document.getUrl() == null || document.getUrl().isBlank())
                    throw new Exception("Document url is empty");
            } else throw new Exception("Document has an unhandled provider");
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document has an illegal provider", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return document;
    }

    public Document removeDocument(String id) {
        Document document;
        try {
            document = this.documentRepository.findDocumentById(id);
            if (document == null) throw new Exception("Document not found");
            if (document.getProvider() == DocumentProvider.LOCAL) {
                String hyperlink = Objects.requireNonNull(env.getProperty("app.hyperlink"));
                String fileId = document.getUrl().split(hyperlink + "api/files/")[1];
                this.gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
            }
            if (this.documentRepository.deleteDocumentById(document.getId()) == null) {
                throw new Exception("Document not found");
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return document;
    }
}
