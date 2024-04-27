package pl.postinvoice.post;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.postinvoice.Util.LogUtil;
import pl.postinvoice.Util.SpecificationUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public void create(CreatePostRequest postRequest) {

        Post post = new Post(
                postRequest.getText(),
                postRequest.getScopecope(),
                postRequest.getAuthor(),
                postRequest.getPublicationDate());


        postRepository.save(post);

    }

    public ReadPostResponse findById(Long id) {
        return postRepository.findByIdFetchComments(id)
                .map(ReadPostResponse::from)
                .orElseThrow(EntityNotFoundException::new);

    }

    public Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

    }

    @Transactional
    public void update(Long id, UpdatePostRequest updatePostRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Post newPost = new Post(post);
        newPost.setText(updatePostRequest.getText());
        newPost.setScopecope(updatePostRequest.getScopecope());
        newPost.setVersion(updatePostRequest.getVersion());

        postRepository.save(newPost);

    }

    @Transactional
    public void deleted(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void archive(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Post newPost = new Post(post);
        newPost.setStatus(PostStatus.DELETED);
        postRepository.save(newPost);
    }

    @Transactional
    public Page<FindPostResponse> find(String textContaining, int page, int size) {
        return postRepository.findActiveAndPublished(textContaining,
                        LocalDateTime.now(),
                        PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdDateTime"))))
                .map(FindPostResponse::from);


    }

    public Page<FindPostResponse> find(FindPostRequest findPostRequest, Pageable pageable) {
        Specification<Post> specification = prepareSpecificationUsingPredicates(findPostRequest);
        return postRepository.findAll(specification, pageable)
                .map(FindPostResponse::from);

    }

    private Specification<Post> prepareSpecificationUsingPredicates(FindPostRequest findPostRequest) {
        return (root, query, criteriaBuilder) ->
        {
            if (!SpecificationUtil.isCountQuery(query)) {
                root.fetch("comments", JoinType.LEFT);

            }

            List<Predicate> predicates = new ArrayList<>();
            if (findPostRequest.postStatusSet() != null) {
                predicates.add(root.get("status").in(Set.of(findPostRequest.postStatusSet())));
            }
            if (findPostRequest.text() != null) {
                predicates.add(criteriaBuilder.like(root.get("text"), "%%" + findPostRequest.text() + "%"));
            }
            if (findPostRequest.publicationDate() != null) {
                Predicate publicationDateIsNullPred = criteriaBuilder.isNull(root.get("publicationDate"));
                Predicate publicationDateLePred = criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), findPostRequest.publicationDate());
                predicates.add(criteriaBuilder.or(publicationDateLePred, publicationDateIsNullPred));
            }
            if (findPostRequest.createdDateTimeStart() != null && findPostRequest.createdDateTimeEnd() != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDateTime"),
                        findPostRequest.createdDateTimeStart(),
                        findPostRequest.createdDateTimeEnd()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Post> prepareSpecyfication(FindPostRequest findPostRequest) {
        Specification<Post> specification = Specification.where(null);

        if (findPostRequest.postStatusSet() != null) {
            Specification<Post> statusInSpec = (root, query, criteriaBuilder) ->
                    root.get("status").in(findPostRequest.postStatusSet());
            specification = specification.and(statusInSpec);
        }
        if (findPostRequest.text() != null) {
            Specification<Post> textInSpec = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("text"), "%%" + findPostRequest.text() + "%");
            specification = specification.and(textInSpec);
        }
        if (findPostRequest.publicationDate() != null) {
            Specification<Post> publicationDateSpec = (root, query, criteriaBuilder) ->
            {
                Predicate publicationDateIsNullPred = criteriaBuilder.isNull(root.get("publicationDate"));
                Predicate publicationDateLePred = criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), findPostRequest.publicationDate());
                return criteriaBuilder.or(publicationDateLePred, publicationDateIsNullPred);

            };
            specification = specification.and(publicationDateSpec);
        }
        if (findPostRequest.createdDateTimeStart() != null && findPostRequest.createdDateTimeEnd() != null) {

            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("createdDateTime"),
                            findPostRequest.createdDateTimeStart(),
                            findPostRequest.createdDateTimeEnd()));
        }

        return specification;
    }


    public void find() {

        log(() -> postRepository.find(PostStatus.ACTIVE), "find");
        log(() -> postRepository.findByStatuses(PostStatus.ACTIVE,
                Sort.by(Sort.Order.desc("createdDateTime"),
                        Sort.Order.asc("author"))), "findByStatuses");

        log(() -> postRepository.find(Set.of(PostStatus.ACTIVE, PostStatus.DELETED), "Mati"), "FindAuthor");

        LogUtil.logPage(() -> postRepository.findByStatus(
                        PostStatus.ACTIVE,
                        PageRequest.of(0, 2, Sort.by(Sort.Order.desc("id")))),
                "findByStatus");
        LogUtil.logPage(() -> postRepository.findByStatus(
                        PostStatus.ACTIVE,
                        PageRequest.of(1, 2, Sort.by(Sort.Order.desc("id")))),
                "findByStatus");
        LogUtil.logPage(() -> postRepository.findByStatus(
                        PostStatus.ACTIVE,
                        PageRequest.of(2, 2, Sort.by(Sort.Order.desc("id")))),
                "findByStatus");
        LogUtil.logPage(() -> postRepository.findByStatus(
                        PostStatus.ACTIVE,
                        PageRequest.of(3, 2, Sort.by(Sort.Order.desc("id")))),
                "findByStatus");

        LogUtil.logPage(() -> postRepository.findAllByStatus(
                        PostStatus.ACTIVE,
                        PageRequest.of(0, 2, Sort.by(Sort.Order.desc("id")))),
                "findAllByStatus"
        );
        LogUtil.logPage(() -> postRepository.findAllByStatus(
                        PostStatus.ACTIVE,
                        PageRequest.of(1, 2, Sort.by(Sort.Order.desc("id")))),
                "findAllByStatus"
        );

    }

    public void log(List<Post> posts, String methodName) {
        System.out.println("*******************" + methodName + "******************************");

        posts.forEach(System.out::println);

    }

    public void log(Supplier<List<Post>> postsList, String methodName) {
        System.out.println("*****************************" + methodName + "******************************");

        postsList.get().forEach(System.out::println);
    }


}
