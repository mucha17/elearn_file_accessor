package pl.kniotes.elearn_file_accessor.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.kniotes.elearn_file_accessor.Models.Video.Video;
import pl.kniotes.elearn_file_accessor.Repositories.VideoRepository;
import pl.kniotes.elearn_file_accessor.Services.VideoService;

@RequiredArgsConstructor
@RestController
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoService videoService;

    @CrossOrigin("*")
    @GetMapping("/api/videos/{id}")
    public Video getVideo(@PathVariable("id") String id) {
        return this.videoService.getVideo(id);
    }
}
