create database if not exists beerhouse character set utf8 collate utf8_general_ci;

drop user 'beerhouse'@'localhost';

create user 'beerhouse'@'localhost' identified by 'beerhouse';

grant all privileges on beerhouse.* to 'beerhouse'@'localhost';

flush privileges;

use beerhouse;

create table if not exists `beer` (
`id` int(10) not null primary key auto_increment,
`name` varchar(50) not null,
`ingredients` varchar(500),
`alcohol_content` varchar(500),
`price` double(10,2) not null,
`category` varchar(50),
constraint `beer_name_unique` unique (`name`)
);
