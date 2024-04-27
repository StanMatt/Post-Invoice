package pl.postinvoice.comment;

import lombok.Value;
import pl.postinvoice.post.Post;
import pl.postinvoice.post.PostStatus;
import pl.postinvoice.post.postScope;

import java.time.LocalDateTime;

@Value
public class ReadCommentResponse {


    Long id;

    String text;

    LocalDateTime createdDateTime;

    String author;

    PostResponse post;


    public static ReadCommentResponse from(Comment comment) {
        return new ReadCommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedDateTime(),
                comment.getAuthor(),
                PostResponse.from(comment.getPost())
        );
    }
    @Value
    static class PostResponse {

        Long id;
        Integer version;

        String text;

        LocalDateTime createdDateTime;


        postScope scopeCope;


        String author;

        LocalDateTime publicationDate;

        PostStatus status;


        public static PostResponse from(Post post) {
            return new PostResponse(
                    post.getId(),
                    post.getVersion(),
                    post.getText(),
                    post.getCreatedDateTime(),
                    post.getScopecope(),
                    post.getAuthor(),
                    post.getPublicationDate(),
                    post.getStatus()
            );
        }
    }
}

