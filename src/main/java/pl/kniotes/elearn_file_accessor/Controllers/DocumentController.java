package pl.kniotes.elearn_file_accessor.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.kniotes.elearn_file_accessor.Models.Document.Document;
import pl.kniotes.elearn_file_accessor.Repositories.DocumentRepository;
import pl.kniotes.elearn_file_accessor.Services.DocumentService;

@RequiredArgsConstructor
@RestController
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @CrossOrigin("*")
    @GetMapping("/api/documents/{id}")
    public Document getDocument(@PathVariable("id") String id) { return this.documentService.getDocument(id); }
}
