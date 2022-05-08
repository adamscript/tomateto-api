insert into users (username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('tabbyavery', 'Tabby Avery', 'Im not a cat', 0, 0, 0, CURRENT_DATE);

insert into users (username, display_name, bio, follow_count, followers_count, posts_count, date)
values ('adamdarmawan', 'Adam Darmawawn', 'Creator of Tomateto', 0, 0, 0, CURRENT_DATE);

insert into post (user_id, content, likes_count, comments_count, is_edited)
values (1, 'Hello this is my first post', 0, 0, false);

insert into post (user_id, content, likes_count, comments_count, is_edited)
values (1, 'And this is my second post', 0, 0, false);

insert into post (user_id, content, likes_count, comments_count, is_edited)
values (2, 'Hello World', 0, 0, false);

insert into post_likes (post_id, user_id)
values (1, 1);

insert into post_likes (post_id, user_id)
values (1, 2);

insert into post_likes (post_id, user_id)
values (2, 2);

insert into post_likes (post_id, user_id)
values (3, 1);

insert into user_follow (user_following, user_followed)
values (1, 2);

insert into user_follow (user_following, user_followed)
values (2, 1);