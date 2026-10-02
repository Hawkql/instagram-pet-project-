create table users (
        id uuid primary key,
        name varchar(100) not null,
        family varchar(100) not null,
        user_name varchar(50) not null unique,
        email varchar(255) not null unique,
        bio text,
        avatar_url varchar(512),
        created_at timestamptz not null default now()
);