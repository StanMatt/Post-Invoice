package pl.postinvoice.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.postinvoice.comment.Comment;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotNull
    private Integer version;

    @NotNull
    @Size(max = 5000)
    private String text;

    @CreatedDate
    @NotNull
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @NotNull
    private LocalDateTime lastModifiedDateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private postScope scopecope;

    @NotBlank
    @NotNull
    @Size(max = 100)
    @NotAudited
    private String author;

    @FutureOrPresent
    private LocalDateTime publicationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;


    public Post(Post old) {
        this.id = old.id;
        this.version = old.version;
        this.createdDateTime = old.createdDateTime;
        this.lastModifiedDateTime = old.lastModifiedDateTime;
        this.text = old.text;
        this.scopecope = old.scopecope;
        this.author = old.author;
        this.publicationDate = old.publicationDate;
        this.status = old.status;


    }


    public Post(String text, postScope scopecope, String author, LocalDateTime publicationDate) {
        this.text = text;
        this.scopecope = scopecope;
        this.author = author;
        this.publicationDate = publicationDate;
        this.status = PostStatus.ACTIVE;
    }


    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(LocalDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setScopecope(postScope scopecope) {
        this.scopecope = scopecope;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }


    public postScope getScopecope() {
        return scopecope;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public PostStatus getStatus() {
        return status;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", version=" + version +
                ", text='" + text + '\'' +
                ", createdDateTime=" + createdDateTime +
                ", lastModifiedDateTime=" + lastModifiedDateTime +
                ", scopecope=" + scopecope +
                ", author='" + author + '\'' +
                ", localDateTime=" + publicationDate +
                ", status=" + status +
                '}';
    }
}
