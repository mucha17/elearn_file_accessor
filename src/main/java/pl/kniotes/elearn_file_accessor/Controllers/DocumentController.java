package pl.kniotes.elearn_file_accessor.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kniotes.elearn_file_accessor.Models.Document.Document;
import pl.kniotes.elearn_file_accessor.Repositories.DocumentRepository;
import pl.kniotes.elearn_file_accessor.Services.DocumentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @CrossOrigin("*")
    @GetMapping("/api/documents")
    public List<Document> getDocuments() {
        return this.documentRepository.findAll();
    }

    @CrossOrigin("*")
    @GetMapping("/api/documents/{id}")
    public Document getDocument(@PathVariable("id") String id) {
        return this.documentService.getDocument(id);
    }

    @CrossOrigin("*")
    @PostMapping("/api/documents")
    public Document postDocument(String title, String description, String owner, Boolean isPrivate, MultipartFile file, String url) {
        return this.documentService.addDocument(title, description, owner, isPrivate, file, url);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/documents/{id}")
    public Document deleteDocument(@PathVariable("id") String id) {
        return this.documentService.removeDocument(id);
    }
}
