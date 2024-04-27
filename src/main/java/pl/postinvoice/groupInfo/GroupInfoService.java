package pl.postinvoice.groupInfo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupInfoService {
    private final GroupInfoRepository groupInfoRepository;

    @Transactional
    public void create(CreateGroupInfoRequest createGroupInfoRequest) {
        GroupInfo groupInfo = GroupInfo.builder()
                .name(createGroupInfoRequest.getName())
                .build();

        groupInfoRepository.save(groupInfo);
    }


    public GroupInfo finById(Long groupId) {
        return groupInfoRepository.findById(groupId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<FindGroupInfoResponse> find(Long userId, int page, int size) {
        Page<GroupInfo> groupInfos = groupInfoRepository.find(userId,
                PageRequest.of(page, size, Sort.by(Sort.Order.desc("name"))));
        return groupInfos.map(FindGroupInfoResponse::from);
    }
}

