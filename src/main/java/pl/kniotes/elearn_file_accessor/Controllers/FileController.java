package pl.kniotes.elearn_file_accessor.Controllers;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.kniotes.elearn_file_accessor.Exceptions.FileHasNoContent;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileController {

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @CrossOrigin("*")
    @GetMapping("/api/files/{id}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("id") String id) {
        GridFSFile file;
        InputStreamResource inputStreamResource;
        MediaType mediaType;
        try {
            file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
            if (file == null) throw new Exception("File was not found in the database");
            if (file.getMetadata() == null) throw new Exception("File has no metadata");
            mediaType = MediaType.valueOf(gridFsOperations.getResource(file).getContentType());
            inputStreamResource = new InputStreamResource(gridFsOperations.getResource(file).getInputStream());
            try {
                gridFsOperations.getResource(file).getInputStream();
            } catch (IOException exception) {
                throw new FileHasNoContent("File has no content");
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        return ResponseEntity.ok().contentType(mediaType).body(inputStreamResource);
    }
}
