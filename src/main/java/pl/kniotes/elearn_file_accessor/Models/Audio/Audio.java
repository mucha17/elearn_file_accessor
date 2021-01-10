package pl.kniotes.elearn_file_accessor.Models.Audio;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("audios")
public class Audio {
    @Id
    private String id;
    private String title;
    private String description;
    private String owner;
    private Boolean isPrivate;
    @NonNull
    private AudioProvider provider;
    @NonNull
    private AudioType type;
    private String url;

    public Audio(String owner, Boolean isPrivate, AudioProvider provider, AudioType type) {
        this.owner = owner;
        this.isPrivate = isPrivate;
        this.provider = provider;
        this.type = type;
    }
}
