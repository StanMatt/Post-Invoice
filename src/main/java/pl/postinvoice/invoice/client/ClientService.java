package pl.postinvoice.invoice.client;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.postinvoice.invoice.accountant.Accountant;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    public void create(CreateClientRequest createClientRequest) {
        Client client = Client.builder()
                .name(createClientRequest.getName())
                .build();
        clientRepository.save(client);
    }

    public Client findById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<FindClientResponse> find(Long accountantId, int page, int size) {
        Page<Client> clients = clientRepository.find(accountantId,
                PageRequest.of(page, size, Sort.by(Sort.Order.desc("name"))));
        return clients.map(FindClientResponse::from);

    }

    public Page<FindClientResponse> find(FindClientRequest findClientRequest, Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(prepareSpec(findClientRequest), pageable);
        return clients.map(FindClientResponse::from);
    }

    private Specification<Client> prepareSpec(FindClientRequest findClientRequest) {
        return (root, query, criteriaBuilder) -> {
            Join<Client, Accountant> joinAccountant = root.join("accountants");


            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(joinAccountant.get("id"), findClientRequest.accountantId()));

            if (findClientRequest.name() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%%" + findClientRequest.name() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
