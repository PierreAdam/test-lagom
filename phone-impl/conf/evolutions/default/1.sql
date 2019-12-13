# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table phone (
  id                            bigserial not null,
  uid                           uuid not null,
  account_uid                   uuid not null,
  phone_number                  varchar(20) not null,
  constraint uq_phone_uid unique (uid),
  constraint pk_phone primary key (id)
);


# --- !Downs

drop table if exists phone cascade;

