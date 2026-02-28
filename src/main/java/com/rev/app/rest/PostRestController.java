package com.rev.app.rest;

import com.rev.app.dto.PostDTO;
import com.rev.app.entity.Post;
import com.rev.app.entity.User;
import com.rev.app.service.PostService;
import com.rev.app.service.PostValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final com.rev.app.service.UserService userService;
    private final PostValidationService postValidationService;

    public PostRestController(PostService postService,
            com.rev.app.service.UserService userService,
            PostValidationService postValidationService) {
        this.postService = postService;
        this.userService = userService;
        this.postValidationService = postValidationService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) throws java.io.IOException {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        postValidationService.validateForCreateOrUpdate(currentUser, dto);
        Post created = postService.createPost(currentUser, dto, null);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,
            @RequestBody PostDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        postValidationService.validateForCreateOrUpdate(currentUser, dto);
        Post updated = postService.updatePost(id, currentUser.getId(), dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Post> sharePost(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        Post shared = postService.sharePost(id, currentUser);
        return ResponseEntity.ok(shared);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        postService.deletePost(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
