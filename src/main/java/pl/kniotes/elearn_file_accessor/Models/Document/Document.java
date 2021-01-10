package pl.kniotes.elearn_file_accessor.Models.Document;

import lombok.*;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@org.springframework.data.mongodb.core.mapping.Document("documents")
public class Document {
    @Id
    private String id;
    private String title;
    private String description;
    private String owner;
    private Boolean isPrivate;
    @NonNull
    private DocumentProvider provider;
    @NonNull
    private DocumentType type;
    private String url;

    public Document(String owner, Boolean isPrivate, DocumentProvider provider, DocumentType type) {
        this.owner = owner;
        this.isPrivate = isPrivate;
        this.provider = provider;
        this.type = type;
    }
}
