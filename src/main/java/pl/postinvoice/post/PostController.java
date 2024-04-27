package pl.postinvoice.post;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReadPostResponse> read(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping
    public void create(@Valid @RequestBody CreatePostRequest postRequest) {

        postService.create(postRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UpdatePostRequest updatePostRequest) {
        postService.update(id, updatePostRequest);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleted(@PathVariable("id") Long id) {
        postService.deleted(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable("id") Long id) {
        postService.archive(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<FindPostResponse>> find(@RequestParam(value = "q", defaultValue = "") String textContaining,
                                                       @RequestParam int page,
                                                       @RequestParam int size) {
        Page<FindPostResponse> body = postService.find(textContaining, page, size);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/find")
    public ResponseEntity<Page<FindPostResponse>> find(@RequestBody FindPostRequest findPostRequest, Pageable pageable) {
        Page<FindPostResponse> body = postService.find(findPostRequest, pageable);

        return ResponseEntity.ok(body);
    }

}

