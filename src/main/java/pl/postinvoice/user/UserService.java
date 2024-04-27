package pl.postinvoice.user;

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
import pl.postinvoice.groupInfo.GroupInfo;
import pl.postinvoice.groupInfo.GroupInfoService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final GroupInfoService groupInfoService;

    @Transactional
    public void create(CreateUserRequest createUserRequest) {
        User user = User.builder()
                .login(createUserRequest.getLogin())
                .build();

        userRepository.save(user);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void joinGroup(JoinGroupRequest joinGroupRequest) {
        User user = userRepository.findById(joinGroupRequest.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        GroupInfo groupInfo = groupInfoService.finById(joinGroupRequest.getGroupId());

        user.getGroupsInfo().add(groupInfo);
    }

    @Transactional
    public void leaveGroup(LeaveGroupRequest leaveGroupRequest) {
        User user = userRepository.findById(leaveGroupRequest.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        GroupInfo groupInfo = groupInfoService.finById(leaveGroupRequest.getGroupId());

        user.getGroupsInfo().remove(groupInfo);
    }

    public Page<FindUserResponse> find(Long groupId, int page, int size) {
        Page<User> users = userRepository.find(groupId,
                PageRequest.of(page, size, Sort.by(Sort.Order.desc("login"))));
        return users.map(FindUserResponse::from);
    }

    public Page<FindUserResponse> find(FindUserRequest findUserRequest, Pageable pageable) {
        Page<User> users = userRepository.findAll(prepareSpec(findUserRequest), pageable);
        return users.map(FindUserResponse::from);
    }

    private Specification<User> prepareSpec(FindUserRequest findUserRequest) {
        return (root, query, criteriaBuilder) -> {
            Join<User, GroupInfo> joinGroupsInfo = root.join("groupsInfo");


            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(joinGroupsInfo.get("id"), findUserRequest.groupId()));

            if (findUserRequest.login() != null) {
                predicates.add(criteriaBuilder.like(root.get("login"), "%%" + findUserRequest.login() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

