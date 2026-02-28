package com.rev.app.service;

import com.rev.app.dto.PostDTO;
import com.rev.app.entity.Post;
import com.rev.app.entity.User;
import com.rev.app.exception.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostValidationService {

    public void validateForCreateOrUpdate(User actor, PostDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Post payload is required.");
        }
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("Post content cannot be empty.");
        }

        boolean isPersonal = actor.getRole() == User.UserRole.PERSONAL;
        boolean hasAdvancedFields = dto.getPostType() == Post.PostType.PROMOTIONAL
                || dto.getPostType() == Post.PostType.ANNOUNCEMENT
                || (dto.getCtaLabel() != null && !dto.getCtaLabel().isBlank())
                || (dto.getCtaUrl() != null && !dto.getCtaUrl().isBlank())
                || dto.getScheduledAt() != null
                || dto.isPinned();

        if (isPersonal && hasAdvancedFields) {
            throw new AccessDeniedException("Advanced post options are available only for creator/business accounts.");
        }

        if (dto.getScheduledAt() != null && !dto.getScheduledAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled time must be in the future.");
        }

        if ((dto.getCtaLabel() != null && !dto.getCtaLabel().isBlank())
                ^ (dto.getCtaUrl() != null && !dto.getCtaUrl().isBlank())) {
            throw new IllegalArgumentException("CTA label and URL must be provided together.");
        }
    }
}

