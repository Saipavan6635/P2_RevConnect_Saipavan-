package com.rev.app.service;

import com.rev.app.dto.PostDTO;
import com.rev.app.entity.Post;
import com.rev.app.entity.User;
import com.rev.app.exception.AccessDeniedException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class PostValidationServiceTest {

    private PostValidationService postValidationService;
    private User personalUser;
    private User creatorUser;

    @Before
    public void setUp() {
        postValidationService = new PostValidationService();

        personalUser = new User();
        personalUser.setRole(User.UserRole.PERSONAL);

        creatorUser = new User();
        creatorUser.setRole(User.UserRole.CREATOR);
    }

    @Test(expected = AccessDeniedException.class)
    public void personalCannotCreatePromotionalPost() {
        PostDTO dto = new PostDTO();
        dto.setContent("Promo content");
        dto.setPostType(Post.PostType.PROMOTIONAL);
        postValidationService.validateForCreateOrUpdate(personalUser, dto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scheduleMustBeFuture() {
        PostDTO dto = new PostDTO();
        dto.setContent("Scheduled post");
        dto.setScheduledAt(LocalDateTime.now().minusMinutes(1));
        postValidationService.validateForCreateOrUpdate(creatorUser, dto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctaNeedsBothLabelAndUrl() {
        PostDTO dto = new PostDTO();
        dto.setContent("CTA post");
        dto.setCtaLabel("Learn More");
        postValidationService.validateForCreateOrUpdate(creatorUser, dto);
    }

    @Test
    public void creatorCanUseAdvancedFields() {
        PostDTO dto = new PostDTO();
        dto.setContent("New product launch");
        dto.setPostType(Post.PostType.ANNOUNCEMENT);
        dto.setScheduledAt(LocalDateTime.now().plusMinutes(30));
        dto.setCtaLabel("Learn More");
        dto.setCtaUrl("https://example.com");
        dto.setPinned(true);
        postValidationService.validateForCreateOrUpdate(creatorUser, dto);
    }
}

