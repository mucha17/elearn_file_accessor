package pl.kniotes.elearn_file_accessor.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kniotes.elearn_file_accessor.Models.Audio.Audio;
import pl.kniotes.elearn_file_accessor.Repositories.AudioRepository;
import pl.kniotes.elearn_file_accessor.Services.AudioService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AudioController {

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private AudioService audioService;

    @CrossOrigin("*")
    @GetMapping("/api/audios")
    public List<Audio> getAudios() {
        return this.audioRepository.findAll();
    }

    @CrossOrigin("*")
    @GetMapping("/api/audios/{id}")
    public Audio getAudio(@PathVariable("id") String id) {
        return this.audioService.getAudio(id);
    }

    @CrossOrigin("*")
    @PostMapping("/api/audios")
    public Audio postAudio(String title, String description, String owner, Boolean isPrivate, MultipartFile file, String url) {
        return this.audioService.addAudio(title, description, owner, isPrivate, file, url);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/audios/{id}")
    public Audio deleteAudio(@PathVariable("id") String id) {
        return this.audioService.removeAudio(id);
    }
}
