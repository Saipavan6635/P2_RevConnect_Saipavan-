package com.rev.app.rest;

import com.rev.app.entity.User;
import com.rev.app.exception.AccessDeniedException;
import com.rev.app.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String q) {
        return userService.searchUsers(q);
    }

    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<Void> deleteUser(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You can delete only your own account.");
        }
        userService.deleteUser(currentUser.getId());
        return org.springframework.http.ResponseEntity.noContent().build();
    }
}
