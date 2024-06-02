CREATE TABLE IF NOT EXISTS vote (
    id bigint generated by default as identity,
    recipe_id bigint,
    rating float(53),
    created_at timestamp(6) not null,
    primary key (id)
);