package pl.kniotes.elearn_file_accessor.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.kniotes.elearn_file_accessor.Models.Document.Document;

@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {
    Document findDocumentById(String id);
}
