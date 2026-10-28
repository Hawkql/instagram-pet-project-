create  table comments(
    id uuid not null primary key ,
    post_id uuid not null ,
    user_id uuid not null ,
    comment text not null ,
    created_at timestamptz not null default now()
);

create index idx_comments_post_created on comments(post_id,created_at desc );