package pl.postinvoice.comment;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.postinvoice.BaseUnitTest;
import pl.postinvoice.comment.ReadCommentResponse.PostResponse;
import pl.postinvoice.post.Post;
import pl.postinvoice.post.PostService;
import pl.postinvoice.post.PostStatus;
import pl.postinvoice.post.postScope;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CommentServiceTest extends BaseUnitTest {
    @InjectMocks
    private CommentService underTest;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostService postService;

    @Test
    void givenCommentIdNotExist_whenFindById_thenEntityNotFoundException() {
        // given
        Long expectedCommentId = 3445L;

        when(commentRepository.findByIdFetchPost(expectedCommentId)).thenReturn(Optional.empty());

        // when
        Executable executable = () -> underTest.findById(expectedCommentId);
        //then

        Assertions.assertThrows(EntityNotFoundException.class, executable);
    }

    @Test
    void givenCommentIdExists_whenFindById_thenReturnResponse() {
        // given
        Long expectedCommentId = 3445L;
        String expectedCommentText = "aattt";
        String expectedCommentAuthor = "Author";
        LocalDateTime expectedCreatedCommentTime = LocalDateTime.of(2024, 4, 1, 1, 20, 41);
        long expectedPostId = 123L;
        String expectedPostText = "Jakis Tekst";
        String expectedPostAuthor = "post Author";
        LocalDateTime expectedCreatedPostTime = LocalDateTime.now();
        Post post = Post.builder()
                .id(expectedPostId)
                .version(0)
                .text(expectedPostText)
                .createdDateTime(expectedCreatedPostTime)
                .scopecope(postScope.PUBLIC)
                .author(expectedPostAuthor)
                .publicationDate(LocalDateTime.now())
                .status(PostStatus.ACTIVE)
                .build();
        Comment comment = Comment.builder()
                .id(expectedCommentId)
                .text(expectedCommentText)
                .createdDateTime(expectedCreatedCommentTime)
                .author(expectedCommentAuthor)
                .post(post)
                .build();


        when(commentRepository.findByIdFetchPost(expectedCommentId)).thenReturn(Optional.of(comment));
        // when
        ReadCommentResponse response = underTest.findById(expectedCommentId);
        //then

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(expectedCommentId);
        assertThat(response.getText()).isEqualTo(expectedCommentText);
        assertThat(response.getCreatedDateTime()).isEqualToIgnoringNanos(expectedCreatedCommentTime);

        assertThat(response)
                .extracting(ReadCommentResponse::getAuthor, ReadCommentResponse::getText)
                .containsExactly(expectedCommentAuthor, expectedCommentText);

        PostResponse responsePost = response.getPost();
        assertThat(responsePost.getId()).isEqualTo(expectedPostId);
        assertThat(responsePost.getText()).isEqualTo(expectedPostText);
        assertThat(responsePost.getCreatedDateTime()).isEqualToIgnoringNanos(expectedCreatedPostTime);

        assertThat(responsePost)
                .extracting(
                        "id",
                        "author")
                .containsExactly(
                        expectedPostId,
                        expectedPostAuthor
                );

    }
}