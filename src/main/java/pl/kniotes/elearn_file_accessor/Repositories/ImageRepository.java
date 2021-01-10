package pl.kniotes.elearn_file_accessor.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.kniotes.elearn_file_accessor.Models.Image.Image;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    Image findImageById(String id);
}
