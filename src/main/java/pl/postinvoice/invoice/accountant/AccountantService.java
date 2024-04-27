package pl.postinvoice.invoice.accountant;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.postinvoice.invoice.client.Client;
import pl.postinvoice.invoice.client.ClientService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountantService {

    private final AccountantRepository accountantRepository;
    private final ClientService clientService;

    public void create(CreateAccountantRequest createAccountantRequest) {
        Accountant accountant = Accountant.builder()
                .name(createAccountantRequest.getName())
                .build();

        accountantRepository.save(accountant);
    }

    @Transactional
    public void jointClientToAccountant(JoinClientToAccountantRequest joinClientToAccountantRequest) {
        Accountant accountant = accountantRepository.findByIdFetchGroupsInfo(joinClientToAccountantRequest.getAccountantId())
                .orElseThrow(EntityNotFoundException::new);

        Client client = clientService.findById(joinClientToAccountantRequest.getClientId());

        accountant.getClients().add(client);

    }

    @Transactional
    public void leaveClientToAccountant(LeaveClientFromAccountantRequest leaveClientToAccountantRequest) {
        Accountant accountant = accountantRepository.findByIdFetchGroupsInfo(leaveClientToAccountantRequest.getAccountantId())
                .orElseThrow(EntityNotFoundException::new);

        Client client = clientService.findById(leaveClientToAccountantRequest.getClientId());

        accountant.getClients().remove(client);
    }

    public Page<FindAccountantResponse> find(Long clientId, int page, int size) {
        Page<Accountant> accountants = accountantRepository.find(clientId,
                PageRequest.of(page, size, Sort.by(Sort.Order.desc("name"))));
        return accountants.map(FindAccountantResponse::from);
    }

    public Page<FindAccountantResponse> find(FindAccountantRequest findAccountantRequest, Pageable pageable) {
        Page<Accountant> accountants = accountantRepository.findAll(prepareSpec(findAccountantRequest), pageable);
        return accountants.map(FindAccountantResponse::from);
    }

    private Specification<Accountant> prepareSpec(FindAccountantRequest findAccountantRequest) {
        return (root, query, criteriaBuilder) -> {
            Join<Accountant, Client> joinClient = root.join("clients");


            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(joinClient.get("id"), findAccountantRequest.clientId()));

            if (findAccountantRequest.name() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%%" + findAccountantRequest.name() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


