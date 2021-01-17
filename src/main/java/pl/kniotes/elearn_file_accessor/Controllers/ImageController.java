package pl.kniotes.elearn_file_accessor.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kniotes.elearn_file_accessor.Models.Image.Image;
import pl.kniotes.elearn_file_accessor.Repositories.ImageRepository;
import pl.kniotes.elearn_file_accessor.Services.ImageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @CrossOrigin("*")
    @GetMapping("/api/images")
    public List<Image> getImages() {
        return this.imageRepository.findAll();
    }

    @CrossOrigin("*")
    @GetMapping("/api/images/{id}")
    public Image getImage(@PathVariable("id") String id) {
        return this.imageService.getImage(id);
    }

    @CrossOrigin("*")
    @PostMapping("/api/images")
    public Image postImage(String title, String description, String owner, Boolean isPrivate, MultipartFile file, String url) {
        return this.imageService.addImage(title, description, owner, isPrivate, file, url);
    }

    @CrossOrigin("*")
    @DeleteMapping("/api/images/{id}")
    public Image deleteImage(@PathVariable("id") String id) {
        return this.imageService.removeImage(id);
    }
}
