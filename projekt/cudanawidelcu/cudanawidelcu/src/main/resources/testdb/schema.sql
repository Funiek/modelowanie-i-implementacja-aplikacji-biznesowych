drop table T_ACCOUNT if exists;

create table T_ACCOUNT (ID bigint identity primary key, NUMBER varchar(9),
                        NAME varchar(50) not null, BALANCE decimal(8,2), unique(NUMBER));
                        
ALTER TABLE T_ACCOUNT ALTER COLUMN BALANCE SET DEFAULT 0.0;

drop table if exists product CASCADE;
drop table if exists recipe CASCADE;
drop table if exists recipe_product CASCADE;
drop table if exists role CASCADE;
drop table if exists user CASCADE;
drop table if exists user_role CASCADE;
drop table if exists vote CASCADE;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;
create table product (id bigint not null, measure varchar(255), name varchar(255), qty double, primary key (id));
create table recipe (id bigint not null, category varchar(255), count_votes integer not null, description varchar(255), name varchar(255), rating double, primary key (id));
create table recipe_product (recipe_id bigint not null, product_id bigint not null);
create table role (id bigint not null, name varchar(255), primary key (id));
create table user (id bigint not null, email varchar(255), enabled boolean not null, login varchar(255), name varchar(255), password varchar(255), primary key (id));
create table user_role (user_id bigint not null, role_id bigint not null, primary key (user_id, role_id));
create table vote (id bigint not null, rating double not null, recipe_id bigint not null, primary key (id));
alter table recipe_product add constraint FK935n2g21lw35urnpf7vwcyp0w foreign key (product_id) references product;
alter table recipe_product add constraint FKb3poss884qc7j3wsvbq63aad3 foreign key (recipe_id) references recipe;
alter table user_role add constraint FKa68196081fvovjhkek5m97n3y foreign key (role_id) references role;
alter table user_role add constraint FK859n2jvi8ivhui0rl0esws6o foreign key (user_id) references user;
alter table vote add constraint FK5ho5lyqoe90v71m3qhb3l3gb foreign key (recipe_id) references recipe;
