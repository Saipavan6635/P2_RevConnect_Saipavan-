# Requirements Traceability

This matrix maps requested capabilities to implemented modules.

## Authentication & Profile

- Register/login/logout: `AuthController`, Spring Security config
- Profile edit/view/privacy/search: `ProfileController`, `UserService`, `profile*.html`, `search-users.html`
- Creator/business extended fields (category/contact/address/hours/external links/offerings): `User`, `ProfileUpdateDTO`, `UserService`, `profile-edit.html`, `profile.html`

## Post Management

- Create/edit/delete posts: `FeedController`, `PostController`, `PostService`
- Hashtags, feed display, pinned/scheduled, CTA fields: `Post`, `PostDTO`, `PostService`, `feed.html`, `post-detail.html`
- Share/repost: `PostController#sharePost`, `PostService#sharePost`

## Social Interactions

- Like/unlike: `InteractionService#toggleLike`, `PostController#likePost`
- Comment add/delete: `InteractionService`, `PostController`

## Network Building

- Connections (request/accept/reject/remove): `ConnectionController`, `ConnectionService`
- Follow/unfollow creator/business: `FollowController`, `FollowService`
- Followers/following list pages: `FollowController`, `user-list.html`

## Notifications

- Notification events for connection/follow/like/comment/share: `NotificationService`
- Unread count/read/clear/preferences: `NotificationController`, `NotificationPreference`, `notification*.html`

## Feed & Discovery

- Personalized feed from own+connections+following: `FeedController`, `PostRepository#findFeedPostsByUserIds`
- Trending posts + trending hashtags: `PostService#getTrendingPosts`, `PostService#getTrendingHashtags`
- Feed filters by hashtag/post type/user role: `FeedController`, `PostService#filterFeedPosts`, `PostSpecification`
- Hashtag search page: `PostController#searchHashtag`, `search-hashtag.html`

## Messaging

- Inbox/conversation/send/delete: `MessageController`, `MessageService`, `messages/*.html`

## Analytics

- Post metrics/account metrics/engagement/demographics: `AnalyticsService`, `AnalyticsController`, `analytics.html`

## Privacy & Access Control

- Private profile post visibility: `ProfileController`
- Private profile enforcement in post detail + hashtag results + follow lists: `PostController`, `FollowController`
