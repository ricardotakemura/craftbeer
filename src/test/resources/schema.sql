drop table if exists beer;
create table if not exists beer (
id identity not null primary key,
name varchar not null,
ingredients varchar,
alcohol_content varchar,
price double not null,
category varchar
);
CREATE INDEX IF NOT EXISTS beer_name_index on beer(name);