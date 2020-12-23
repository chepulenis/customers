drop table if exists customers;

create table customers (
id bigserial not null primary key,
created bigint,
updated bigint,
full_name varchar (50) not null,
email varchar (100) not null,
phone varchar (14),
is_deleted boolean not null default false
);

commit;