create table  post_likes(
    id uuid not null primary key ,
    post_id uuid not null ,
    liked_author uuid not null ,
    created_at timestamptz not null default now(),
    unique (post_id,liked_author)
);