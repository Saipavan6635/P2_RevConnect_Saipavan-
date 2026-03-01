package com.rev.app.controller;

import com.rev.app.dto.PostDTO;
import com.rev.app.entity.Post;
import com.rev.app.entity.User;
import com.rev.app.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/feed")
public class FeedController {

    private static final Logger logger = LogManager.getLogger(FeedController.class);

    private final PostService postService;
    private final UserService userService;
    private final ConnectionService connectionService;
    private final FollowService followService;
    private final NotificationService notificationService;
    private final PostValidationService postValidationService;

    public FeedController(PostService postService, UserService userService,
            ConnectionService connectionService, FollowService followService,
            NotificationService notificationService,
            PostValidationService postValidationService) {
        this.postService = postService;
        this.userService = userService;
        this.connectionService = connectionService;
        this.followService = followService;
        this.notificationService = notificationService;
        this.postValidationService = postValidationService;
    }

    @GetMapping
    public String feed(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String hashtag,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String userRole,
            Model model) {
        User currentUser = userService.findByUsername(userDetails.getUsername());

        // Build user ID list: own + connections + following
        List<Long> feedUserIds = new ArrayList<>();
        feedUserIds.add(currentUser.getId());
        feedUserIds.addAll(connectionService.getConnectionIds(currentUser));
        feedUserIds.addAll(followService.getFollowedIds(currentUser.getId()));

        List<Post> posts;
        Post.PostType postType = null;
        User.UserRole authorRole = null;
        if (type != null && !type.isBlank()) {
            try {
                postType = Post.PostType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                logger.debug("Invalid post type filter: {}", type);
            }
        }
        if (userRole != null && !userRole.isBlank()) {
            try {
                authorRole = User.UserRole.valueOf(userRole.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                logger.debug("Invalid user role filter: {}", userRole);
            }
        }

        if ((hashtag != null && !hashtag.isBlank()) || postType != null || authorRole != null) {
            posts = postService.filterFeedPosts(feedUserIds, postType, hashtag, authorRole);
        } else {
            Page<Post> feedPage = postService.getFeed(feedUserIds, page, 10);
            posts = feedPage.getContent();
            model.addAttribute("totalPages", feedPage.getTotalPages());
            model.addAttribute("currentPage", page);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("newPost", new PostDTO());
        model.addAttribute("unreadCount", notificationService.getUnreadCount(currentUser.getId()));
        model.addAttribute("connectionCount", connectionService.getConnections(currentUser).size());
        model.addAttribute("followerCount", followService.countFollowers(currentUser.getId()));
        model.addAttribute("trending", postService.getTrendingPosts(0).getContent());
        model.addAttribute("trendingHashtags", postService.getTrendingHashtags(12));
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedUserRole", userRole);
        model.addAttribute("selectedHashtag", hashtag);
        return "feed";
    }

    @PostMapping("/post")
    public String createPost(@AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute PostDTO postDTO,
            @RequestParam(value = "image", required = false) org.springframework.web.multipart.MultipartFile image,
            RedirectAttributes redirectAttributes) {
        User currentUser = userService.findByUsername(userDetails.getUsername());

        logger.info("Controller: Received post request from user: {}", currentUser.getUsername());
        logger.info("Controller: Content length: {}", postDTO.getContent() != null ? postDTO.getContent().length() : 0);
        logger.info("Controller: Image presence: {}, Original Filename: {}", (image != null),
                (image != null ? image.getOriginalFilename() : "N/A"));

        try {
            postValidationService.validateForCreateOrUpdate(currentUser, postDTO);
            postService.createPost(currentUser, postDTO, image);
            redirectAttributes.addFlashAttribute("successMessage", "Post created!");
        } catch (IllegalArgumentException | com.rev.app.exception.AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (java.io.IOException e) {
            logger.error("Controller: Failed to upload post image", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload image.");
        }
        return "redirect:/feed";
    }
}
