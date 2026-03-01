# Testing Artifacts

## Automated Tests

Run all tests:

```bash
mvn test
```

Current status:

- Service and exception-layer tests pass.
- Existing tests cover user registration, posts, notifications, messaging, follows, connections, analytics, and validation.

## Manual QA Checklist

### Authentication

- Register with each role (`PERSONAL`, `CREATOR`, `BUSINESS`)
- Login via username and via email
- Logout and relogin

### Profile

- Edit profile fields (name, bio, location, website, privacy)
- Upload profile picture
- For creator/business: update category, contact info, external links, offerings
- For business: update address and hours

### Feed and Posts

- Create regular post
- Create post with hashtags and image
- Creator/business post with CTA + scheduled time + pinned flag
- Edit/delete own post
- Like/unlike, comment, delete comment
- Share/repost
- Filter feed by hashtag, post type, and user role

### Network

- Send/accept/reject connection requests (personal-to-personal)
- Remove connection
- Follow/unfollow creator/business
- Open followers/following pages

### Notifications

- Verify events produce notifications (connect/follow/like/comment/share)
- Mark individual notification as read
- Clear all notifications
- Update notification preferences and re-verify behavior

### Messaging

- Start conversation from profile
- Send/receive messages
- Delete message and delete conversation

### Analytics

- Verify account metrics
- Verify engagement metrics
- Verify post-level analytics table
- Verify demographics panels

### Security / Privacy

- Private profile should hide posts/lists from non-connected users
- Post detail should block access when author profile is private and requester is not connected
- Hashtag search should only show visible posts
