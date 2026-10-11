create table follows (
                         id uuid primary key,
                         follower_id uuid not null references users(id) on delete cascade,
                         followee_id uuid not null references users(id) on delete cascade,
                         created_at timestamptz not null default now(),
                         unique (follower_id, followee_id)
);
create index idx_follows_followee on follows(followee_id);