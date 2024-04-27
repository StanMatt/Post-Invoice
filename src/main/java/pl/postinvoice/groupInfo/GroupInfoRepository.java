package pl.postinvoice.groupInfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GroupInfoRepository extends CrudRepository<GroupInfo, Long> {
    @Query("select gi from GroupInfo gi join gi.users u where u.id = :userId")
    Page<GroupInfo> find(Long userId, Pageable name);
}
