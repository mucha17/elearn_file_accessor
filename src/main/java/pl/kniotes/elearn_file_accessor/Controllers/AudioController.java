package pl.kniotes.elearn_file_accessor.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.kniotes.elearn_file_accessor.Models.Audio.Audio;
import pl.kniotes.elearn_file_accessor.Repositories.AudioRepository;
import pl.kniotes.elearn_file_accessor.Services.AudioService;

@RequiredArgsConstructor
@RestController
public class AudioController {

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private AudioService audioService;

    @CrossOrigin("*")
    @GetMapping("/api/audios/{id}")
    public Audio getAudio(@PathVariable("id") String id) { return this.audioService.getAudio(id); }
}
