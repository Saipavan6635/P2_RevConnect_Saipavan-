package com.rev.app.service;

import com.rev.app.entity.Post;
import com.rev.app.entity.User;
import com.rev.app.repository.PostRepository;
import com.rev.app.repository.LikeRepository;
import com.rev.app.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final FollowService followService;
    private final UserService userService;
    private final com.rev.app.repository.ConnectionRepository connectionRepository;

    public AnalyticsService(PostRepository postRepository,
            LikeRepository likeRepository,
            CommentRepository commentRepository,
            FollowService followService,
            UserService userService,
            com.rev.app.repository.ConnectionRepository connectionRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.followService = followService;
        this.userService = userService;
        this.connectionRepository = connectionRepository;
    }

    /**
     * Returns analytics for each published post of the given user.
     */
    public List<Map<String, Object>> getPostAnalytics(Long authorId) {
        List<Post> posts = postRepository.findByAuthorIdAndPublishedTrueOrderByPinnedDescCreatedAtDesc(authorId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Post post : posts) {
            Map<String, Object> analytics = new HashMap<>();
            analytics.put("postId", post.getId());
            analytics.put("content", post.getContent().length() > 80
                    ? post.getContent().substring(0, 80) + "..."
                    : post.getContent());
            analytics.put("createdAt", post.getCreatedAt());
            analytics.put("likes", likeRepository.countByPostId(post.getId()));
            analytics.put("comments", commentRepository.countByPostId(post.getId()));
            analytics.put("shares", post.getShares() != null ? post.getShares().size() : 0);
            result.add(analytics);
        }
        return result;
    }

    /**
     * Returns summary metrics for the user's account.
     */
    public Map<String, Object> getAccountMetrics(Long userId) {
        User user = userService.findById(userId);
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalPosts", postRepository.countPublishedPostsByAuthor(userId));
        metrics.put("totalFollowers", followService.countFollowers(userId));
        metrics.put("totalFollowing", followService.countFollowing(userId));
        metrics.put("totalConnections", connectionRepository.countConnections(user));
        return metrics;
    }

    /**
     * Returns aggregate engagement and lightweight reach estimation.
     */
    public Map<String, Object> getEngagementMetrics(Long userId) {
        List<Post> posts = postRepository.findByAuthorIdAndPublishedTrueOrderByPinnedDescCreatedAtDesc(userId);
        long totalLikes = 0L;
        long totalComments = 0L;
        long totalShares = 0L;

        for (Post post : posts) {
            totalLikes += likeRepository.countByPostId(post.getId());
            totalComments += commentRepository.countByPostId(post.getId());
            totalShares += post.getShares() != null ? post.getShares().size() : 0;
        }

        long totalInteractions = totalLikes + totalComments + totalShares;
        long totalPosts = posts.size();
        long followerCount = followService.countFollowers(userId);
        User user = userService.findById(userId);
        long connectionCount = connectionRepository.countConnections(user);

        double avgEngagementPerPost = totalPosts == 0 ? 0.0 : (double) totalInteractions / totalPosts;
        double engagementRate = followerCount == 0 ? 0.0 : ((double) totalInteractions / followerCount) * 100.0;

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalLikes", totalLikes);
        metrics.put("totalComments", totalComments);
        metrics.put("totalShares", totalShares);
        metrics.put("totalInteractions", totalInteractions);
        metrics.put("avgEngagementPerPost", round2(avgEngagementPerPost));
        metrics.put("engagementRate", round2(engagementRate));
        metrics.put("estimatedReach", followerCount + connectionCount);
        return metrics;
    }

    /**
     * Returns followers grouped by role and top follower locations.
     */
    public Map<String, Object> getFollowerDemographics(Long userId) {
        List<User> followers = followService.getFollowers(userId);

        Map<String, Long> byRole = followers.stream()
                .collect(Collectors.groupingBy(u -> u.getRole().name(), Collectors.counting()));

        Map<String, Long> byLocation = followers.stream()
                .map(User::getLocation)
                .filter(location -> location != null && !location.isBlank())
                .collect(Collectors.groupingBy(location -> location, Collectors.counting()));

        List<Map.Entry<String, Long>> topLocations = byLocation.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .toList();

        Map<String, Object> demographics = new HashMap<>();
        demographics.put("followerRoleMix", byRole);
        demographics.put("topFollowerLocations", topLocations);
        demographics.put("followersWithLocation", byLocation.values().stream().mapToLong(Long::longValue).sum());
        return demographics;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
