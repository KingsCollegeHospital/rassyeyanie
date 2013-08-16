drop schema if exists kch_lookup cascade;

create schema kch_lookup;

comment on schema kch_lookup is 'Schema containing configuration values';

create table kch_lookup.context (
    id serial not null primary key,
    context character varying(50) not null,
    defaultvalue text,
    lastupdatedby name not null default current_user,
    lastupdatedtime timestamp with time zone not null default current_timestamp
);

create table kch_lookup.lookup (
    id serial not null primary key,
    contextid integer not null references kch_lookup.context(id),
    key character varying(50) not null,
    value text,
    startdate date not null default current_date,
    enddate date,
    lastupdatedby name not null default current_user,
    lastupdatedtime timestamp with time zone not null default current_timestamp
);


