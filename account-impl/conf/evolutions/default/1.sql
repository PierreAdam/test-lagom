# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                            bigserial not null,
  uid                           uuid not null,
  name                          varchar(40) not null,
  constraint uq_account_uid unique (uid),
  constraint pk_account primary key (id)
);


# --- !Downs

drop table if exists account cascade;

