package pl.postinvoice.post;


import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import pl.postinvoice.Util.BaseIT;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



class PostControllerIT extends BaseIT {


    @Test
    void givenWrongRequest_whenCreate_thenBadRequest() throws Exception {
        // given
        CreatePostRequest request = new CreatePostRequest(null, null, null, null);
        ResultActions resultActions = performPost( "/api/posts", request);

        // then

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.author").value("must not be blank"))
                .andExpect(jsonPath("$.text").value("must not be blank"))
                .andExpect(jsonPath("$.scopecope").value("must not be null"));

    }

    @Test
    void givenWrongRequest_whenCreate_thenRequest() throws Exception {
        // given
        String text = "text";
        postScope scope = postScope.PUBLIC;
        String author = "author";
        LocalDateTime publicationDate = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);

        CreatePostRequest request = new CreatePostRequest(
                text,
                scope,
                author,
                publicationDate
        );
        ResultActions resultActions = performPost("/api/posts", request);

        // then

        resultActions.andExpect(status().isOk());

        List<Post> posts = entityManager.createQuery("select p from Post p left join fetch p.comments").getResultList();

        assertThat(posts).hasSize(1);
        Post post = posts.get(0);
        assertThat(post)
                .extracting(
                        Post::getId,
                        Post::getCreatedDateTime,
                        Post::getLastModifiedDateTime
                ).isNotNull();

        assertThat(post)
                .extracting(
                        Post::getVersion,
                        Post::getText,
                        Post::getAuthor,
                        Post::getPublicationDate,
                        Post::getScopecope,
                        Post::getStatus
                ).containsExactly(
                        0,
                        text,
                        author,
                        publicationDate,
                        scope,
                        PostStatus.ACTIVE
                );

        assertThat(post.getComments()).isEmpty();


    }

}