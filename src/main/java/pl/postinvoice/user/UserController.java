package pl.postinvoice.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public void create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        userService.create(createUserRequest);
    }

    @PostMapping("/join-group")
    public void joinGroup(@Valid @RequestBody JoinGroupRequest joinGroupRequest) {
        userService.joinGroup(joinGroupRequest);
    }

    @PostMapping("/leave-group")
    public void leaveGroup(@Valid @RequestBody LeaveGroupRequest leaveGroupRequest) {
        userService.leaveGroup(leaveGroupRequest);
    }

    @GetMapping
    public ResponseEntity<Page<FindUserResponse>> find(@RequestParam(value = "g_id") Long groupId,
                                                       @RequestParam int page,
                                                       @RequestParam int size) {
        Page<FindUserResponse> body = userService.find(groupId, page, size);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/find")
    public ResponseEntity<Page<FindUserResponse>> find(@Valid @RequestBody FindUserRequest findUserRequest, Pageable pageable) {
        Page<FindUserResponse> body = userService.find(findUserRequest, pageable);

        return ResponseEntity.ok(body);
    }


}
