package pl.kniotes.elearn_file_accessor.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.kniotes.elearn_file_accessor.Models.Video.Video;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    Video findVideoById(String id);

    Video deleteVideoById(String id);
}
