insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('1', 'tabbyavery', 'Tabby Avery', 'Im not a cat', 0, 0, 0, CURRENT_DATE);

insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('2', 'adamdarmawan', 'Adam Darmawawn', 'Creator of Tomateto', 0, 0, 0, CURRENT_DATE);

insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('GsmTDjCaqvaYN1eszOqT3QmT7D73', 'helloworld', 'Hello World', 'Im a machine', 0, 0, 0, CURRENT_DATE);

insert into users (id, username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('U1k7x5pBXXNleCHRqfRTYZkQlmD3', 'testing', 'Test Account', 'This is a test', 0, 0, 0, CURRENT_DATE);

insert into post (users, content, likes_count, comments_count, is_edited, date)
values (1, 'Hello this is my first post', 11, 0, false, '2020-01-01');

insert into post (users, content, likes_count, comments_count, is_edited, date)
values (1, 'And this is my second post', 22, 0, false, '2021-01-01');

insert into post (users, content, likes_count, comments_count, is_edited, date)
values (2, 'Hello World', 4, 0, false, '2021-03-01');

insert into comment(users, post, content, likes_count)
values(1, 1, 'Hello Im a tomathought', 35);

insert into post_likes (post, users)
values (1, 1);

insert into post_likes (post, users)
values (1, 2);

insert into post_likes (post, users)
values (2, 2);

insert into post_likes (post, users)
values (3, 1);

insert into user_follow (user_following, user_followed)
values (1, 2);

insert into user_follow (user_following, user_followed)
values (2, 1);