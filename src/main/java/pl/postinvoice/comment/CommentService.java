package pl.postinvoice.comment;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.postinvoice.Util.SpecificationUtil;
import pl.postinvoice.post.Post;
import pl.postinvoice.post.PostService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;


    @Transactional
    public void create(CreateCommentRequest createCommentRequest) {
        Post post = postService.findPostById(createCommentRequest.getPostId());

        Comment comment = Comment.builder()
                .text(createCommentRequest.getText())
                .author(createCommentRequest.getAuthor())
                .post(post)
                .build();

        commentRepository.save(comment);
    }

    public ReadCommentResponse findById(Long id) {

        return commentRepository.findByIdFetchPost(id)
                .map(ReadCommentResponse::from)
                .orElseThrow(EntityNotFoundException::new);

    }

    @Transactional
    public void update(Long id, UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Comment newComment = comment.toBuilder()
                .text(updateCommentRequest.getText())
                .author(updateCommentRequest.getAuthor())
                .build();

        commentRepository.save(newComment);


    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public Page<ReadCommentResponse> find(Long postId, Pageable pageable) {
        return commentRepository.findAll(prepareSpecification(postId), pageable)
                .map(ReadCommentResponse::from);


    }

    private Specification<Comment> prepareSpecification(Long postId) {
        return (root, query, criteriaBuilder) -> {
            if (!SpecificationUtil.isCountQuery(query)) {
                root.fetch("post");
            }
            return criteriaBuilder.equal(root.get("post").get("id"), postId);
        };
    }

}
