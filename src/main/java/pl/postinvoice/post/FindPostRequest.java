package pl.postinvoice.post;

import java.time.LocalDateTime;
import java.util.Set;

public record FindPostRequest(Set<PostStatus> postStatusSet,
                              String text,
                              LocalDateTime publicationDate,
                              LocalDateTime createdDateTimeStart,
                              LocalDateTime createdDateTimeEnd) {
}
