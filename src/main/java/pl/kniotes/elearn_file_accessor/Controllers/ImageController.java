package pl.kniotes.elearn_file_accessor.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.kniotes.elearn_file_accessor.Models.Image.Image;
import pl.kniotes.elearn_file_accessor.Repositories.ImageRepository;
import pl.kniotes.elearn_file_accessor.Services.ImageService;

@RequiredArgsConstructor
@RestController
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @CrossOrigin("*")
    @GetMapping("/api/images/{id}")
    public Image getImage(@PathVariable("id") String id) {
        return this.imageService.getImage(id);
    }
}
