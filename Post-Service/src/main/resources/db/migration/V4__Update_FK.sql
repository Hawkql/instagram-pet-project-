DELETE FROM comments
WHERE post_id NOT IN (SELECT id FROM posts);

DELETE FROM post_likes
WHERE post_id NOT IN (SELECT id FROM posts);

alter table comments
    add constraint fk_comments_post
        foreign key (post_id) references posts(id) on delete cascade;

alter table post_likes
    add constraint fk_post_likes_post
        foreign key (post_id) references posts(id) on delete cascade;


drop index idx_posts_author_created;

create index idx_posts_author_created on posts (author_id, created_at desc)
    where deleted_at is null;