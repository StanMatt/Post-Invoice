package pl.postinvoice.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends CrudRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("select p from Post p")
    List<Post> find(PostStatus postStatus);

    @Query("select p from Post p left join fetch p.comments where p.id=:id")
    Optional<Post> findByIdFetchComments(Long id);

    @Query("select p from Post p where p.status=:postStatus")
    List<Post> findByStatuses(PostStatus postStatus, Sort sort);

    @Query("select p from Post p where p.status in :postStatusSet and p.author like :author% ")
    List<Post> find(Set<PostStatus> postStatusSet, @Param("author") String authorStartWith);

    Page<Post> findByStatus(PostStatus postStatus, Pageable pageable);

    @Query("select p from Post p where p.status=:postStatus")
    Page<Post> findAllByStatus(PostStatus postStatus, Pageable pageable);

    @Query("select p from Post p where p.text like %:textContaining% " +
            " and p.status='ACTIVE' " +
            "and (p.publicationDate is null or p.publicationDate <= :publicationDate ) ")
    Page<Post> findActiveAndPublished(String textContaining, LocalDateTime publicationDate, Pageable pageable);

    List<Post> findByStatus(PostStatus postStatus);

    List<Post> findByStatusOrderByCreatedDateTime(PostStatus postStatus);

    List<Post> findByStatusOrderByCreatedDateTimeDesc(PostStatus postStatus);

    List<Post> findByStatus(PostStatus postStatus, Sort sort);

    Long countByStatus(PostStatus postStatus);

    boolean existsByStatus(PostStatus postStatus);
}
