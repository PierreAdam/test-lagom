# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                            varchar(255) not null,
  name                          varchar(255) not null
);


# --- !Downs

drop table if exists account cascade;

