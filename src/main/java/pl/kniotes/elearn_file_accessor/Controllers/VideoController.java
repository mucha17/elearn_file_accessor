package pl.kniotes.elearn_file_accessor.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kniotes.elearn_file_accessor.Models.Video.Video;
import pl.kniotes.elearn_file_accessor.Repositories.VideoRepository;
import pl.kniotes.elearn_file_accessor.Services.VideoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoService videoService;

    @CrossOrigin("*")
    @GetMapping("/api/videos")
    public List<Video> getVideos() {
        return this.videoRepository.findAll();
    }

    @CrossOrigin("*")
    @GetMapping("/api/videos/{id}")
    public Video getVideo(@PathVariable("id") String id) {
        return this.videoService.getVideo(id);
    }

    @CrossOrigin("*")
    @PostMapping("/api/videos")
    public Video postVideo(String title, String description, String owner, Boolean isPrivate, MultipartFile file, String url) {
        return this.videoService.addVideo(title, description, owner, isPrivate, file, url);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/videos/{id}")
    public Video deleteVideo(@PathVariable("id") String id) {
        return this.videoService.removeVideo(id);
    }
}
