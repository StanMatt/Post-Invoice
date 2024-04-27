package pl.postinvoice.groupInfo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groupsInfo")
@RequiredArgsConstructor
@Slf4j
public class GroupInfoController {

    private final GroupInfoService groupInfoService;

    @PostMapping
    public void create(@Valid @RequestBody CreateGroupInfoRequest createGroupInfoRequest) {
        groupInfoService.create(createGroupInfoRequest);
    }

    @GetMapping
    public ResponseEntity<Page<FindGroupInfoResponse>> find(@RequestParam(value = "u_id") Long userId,
                                                            @RequestParam int page,
                                                            @RequestParam int size) {
        Page<FindGroupInfoResponse> body = groupInfoService.find(userId, page, size);

        return ResponseEntity.ok(body);
    }
}
