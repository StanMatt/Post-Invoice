package pl.postinvoice.invoice.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    @Query("select cl from Client cl join cl.accountants ac where ac.id = :accountantId")
    Page<Client> find(Long accountantId, Pageable name);
}
