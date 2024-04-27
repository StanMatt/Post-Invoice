package pl.postinvoice.invoice.accountant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accountants")
@RequiredArgsConstructor
@Slf4j
public class AccountantController {

    private final AccountantService accountantService;

    @PostMapping
    public void create(@Valid @RequestBody CreateAccountantRequest createAccountantRequest) {
        accountantService.create(createAccountantRequest);
    }

    @PostMapping("/accountant-client")
    public void jointClientToAccountant(@Valid @RequestBody JoinClientToAccountantRequest joinClientToAccountantRequest) {
        accountantService.jointClientToAccountant(joinClientToAccountantRequest);
    }

    @PostMapping("/leave-accountant-client")
    public void leaveClientToAccountant(@Valid @RequestBody LeaveClientFromAccountantRequest leaveClientToAccountantRequest) {
        accountantService.leaveClientToAccountant(leaveClientToAccountantRequest);
    }

    @GetMapping
    public ResponseEntity<Page<FindAccountantResponse>> find(@RequestParam(value = "cl_id") Long clientId,
                                                             @RequestParam int page,
                                                             @RequestParam int size) {
        Page<FindAccountantResponse> body = accountantService.find(clientId, page, size);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/find")
    public ResponseEntity<Page<FindAccountantResponse>> find(@Valid @RequestBody FindAccountantRequest findAccountantRequest, Pageable pageable) {
        Page<FindAccountantResponse> body = accountantService.find(findAccountantRequest, pageable);

        return ResponseEntity.ok(body);
    }
}
