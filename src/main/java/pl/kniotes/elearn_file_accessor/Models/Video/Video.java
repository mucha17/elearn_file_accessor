package pl.kniotes.elearn_file_accessor.Models.Video;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("videos")
public class Video {
    @Id
    private String id;
    private String title;
    private String description;
    private String owner;
    private Boolean isPrivate;
    @NonNull
    private VideoProvider provider;
    @NonNull
    private VideoType type;
    private String url;

    public Video(String owner, Boolean isPrivate, VideoProvider provider, VideoType type) {
        this.owner = owner;
        this.isPrivate = isPrivate;
        this.provider = provider;
        this.type = type;
    }
}
