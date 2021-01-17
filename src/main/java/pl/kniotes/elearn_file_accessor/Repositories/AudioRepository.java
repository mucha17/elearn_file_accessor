package pl.kniotes.elearn_file_accessor.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.kniotes.elearn_file_accessor.Models.Audio.Audio;

@Repository
public interface AudioRepository extends MongoRepository<Audio, String> {
    Audio findAudioById(String id);

    Audio deleteAudioById(String id);
}
