package pl.postinvoice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("select u from User u join u.groupsInfo gi where gi.id = :groupId")
    Page<User> find(Long groupId, Pageable pageable);

    @Query("select u from User u join u.groupsInfo gi where gi.id = :groupId " +
            " and (:login is null or u.login like %:login%)")
    Page<User> find(Long groupId, String login, Pageable pageable);
}
