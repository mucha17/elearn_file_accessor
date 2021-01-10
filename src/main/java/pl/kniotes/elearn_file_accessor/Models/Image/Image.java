package pl.kniotes.elearn_file_accessor.Models.Image;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("images")
public class Image {
    @Id
    private String id;
    private String title;
    private String description;
    private String owner;
    private Boolean isPrivate;
    @NonNull
    private ImageProvider provider;
    @NonNull
    private ImageType type;
    private String url;

    public Image(String owner, Boolean isPrivate, ImageProvider provider, ImageType type) {
        this.owner = owner;
        this.isPrivate = isPrivate;
        this.provider = provider;
        this.type = type;
    }
}
