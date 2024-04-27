package pl.postinvoice.post;


import lombok.Value;
import pl.postinvoice.comment.Comment;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Value
class ReadPostResponse {

    Long id;
    Integer version;

    String text;

    LocalDateTime createdDateTime;


    postScope scopeCope;


    String author;

    LocalDateTime publicationDate;

    PostStatus status;

    List<ReadComment> comments;


    public static ReadPostResponse from(Post post) {
        return new ReadPostResponse(
                post.getId(),
                post.getVersion(),
                post.getText(),
                post.getCreatedDateTime(),
                post.getScopecope(),
                post.getAuthor(),
                post.getPublicationDate(),
                post.getStatus(),
                post.getComments().stream()
                        .map(ReadComment::from)
                        .sorted(Comparator.comparing(ReadComment::getCreatedDateTime))
                        .collect(Collectors.toList())
        );
    }

    @Value
    public static class ReadComment {


        Long id;

        String text;

        LocalDateTime createdDateTime;

        String author;


        public static ReadComment from(Comment comment) {
            return new ReadComment(
                    comment.getId(),
                    comment.getText(),
                    comment.getCreatedDateTime(),
                    comment.getAuthor()
            );
        }
    }
}
