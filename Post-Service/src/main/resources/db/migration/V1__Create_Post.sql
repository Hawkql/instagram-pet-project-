create table posts (
    id uuid primary key ,
    author_id uuid not null ,
    caption text,
    like_count INTEGER not null default 0 check ( like_count>=0 ),
    comment_count INTEGER not null default 0 check ( comment_count>=0 ),
    media_key varchar(255) not null ,
    status varchar(20) not null check ( status in ('DRAFT','UPLOADING','PUBLISHED','ARCHIVED')) ,
    created_at timestamptz not null default now(),
    deleted_at timestamptz
);

create index idx_posts_author_created on posts (author_id, created_at desc);