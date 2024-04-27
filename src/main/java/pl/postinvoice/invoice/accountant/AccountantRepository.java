package pl.postinvoice.invoice.accountant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountantRepository extends CrudRepository<Accountant, Long>, JpaSpecificationExecutor<Accountant> {
    @Query("select a from Accountant a left join fetch a.clients where a.id=:accountantId")
    Optional<Accountant> findByIdFetchGroupsInfo(Long accountantId);

    @Query("select ac from Accountant ac join ac.clients cl where cl.id = :clientId")
    Page<Accountant> find(Long clientId, Pageable login);
}
