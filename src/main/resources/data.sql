insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('1', 'tabbyavery', 'Tabby Avery', 'Im not a cat', 0, 0, 0, CURRENT_DATE);

insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('2', 'adamdarmawan', 'Adam Darmawawn', 'Creator of Tomateto', 0, 0, 0, CURRENT_DATE);

insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('GsmTDjCaqvaYN1eszOqT3QmT7D73', 'helloworld', 'Hello World', 'Im a machine', 1, 1, 0, CURRENT_DATE);

insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('zNHgnv8t49VoszAMl83VhafMBMI3', 'helloworld2', 'Hello World 2.0', 'Im hello world but better', 1, 1, 0, CURRENT_DATE);

insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('U1k7x5pBXXNleCHRqfRTYZkQlmD3', 'testing', 'Test Account', 'This is a test', 1, 1, 0, CURRENT_DATE);

insert into post (users, content, likes_count, comments_count, is_edited, date)
values ('U1k7x5pBXXNleCHRqfRTYZkQlmD3', 'Hello this is my first post', 2, 1, false, '2020-01-01');

insert into post (users, content, photo, likes_count, comments_count, is_edited, date)
values ('U1k7x5pBXXNleCHRqfRTYZkQlmD3', 'And this is my second post', 'https://pbs.twimg.com/media/FUMBh4CWYAEnzxw?format=jpg&name=large', 1, 0, false, '2021-01-01');

insert into post (users, content, likes_count, comments_count, is_edited, date)
values ('GsmTDjCaqvaYN1eszOqT3QmT7D73', 'Hello World', 1, 0, false, '2022-05-29');

insert into comment(users, post, content, likes_count, date)
values('U1k7x5pBXXNleCHRqfRTYZkQlmD3', 1, 'Hello Im a tomathought', 0, '2020-01-02');

insert into post_likes (post, users)
values (1, 'GsmTDjCaqvaYN1eszOqT3QmT7D73');

insert into post_likes (post, users)
values (1, 'U1k7x5pBXXNleCHRqfRTYZkQlmD3');

insert into post_likes (post, users)
values (2, 'GsmTDjCaqvaYN1eszOqT3QmT7D73');

insert into post_likes (post, users)
values (3, 'U1k7x5pBXXNleCHRqfRTYZkQlmD3');

insert into user_follow (user_following, user_followed)
values ('U1k7x5pBXXNleCHRqfRTYZkQlmD3', 'GsmTDjCaqvaYN1eszOqT3QmT7D73');

insert into user_follow (user_following, user_followed)
values ('GsmTDjCaqvaYN1eszOqT3QmT7D73', 'U1k7x5pBXXNleCHRqfRTYZkQlmD3')