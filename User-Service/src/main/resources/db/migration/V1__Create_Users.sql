CREATE TABLE  users(
    id uuid PRIMARY KEY ,
    name varchar(255) not null ,
    family varchar(255) not null ,
    user_name varchar(255) not null unique ,
    email varchar(255) unique ,
    bio varchar(255) not null ,
    avatar_url varchar(255) ,
    created_at timestamptz not null default now()

)