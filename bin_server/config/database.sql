CREATE TABLE IF NOT EXISTS coordinates (
    coordinates_id serial primary key,
    x double precision not null,
    y double precision not null
);

CREATE TABLE IF NOT EXISTS venue (
    venue_id bigserial primary key,
    venue_name text not null check(venue_name <> ''),
    capacity integer check(capacity > 0 OR capacity is NULL),
    venue_type text,
    check(
            venue_type is NULL 
            OR venue_type='PUB'  
            OR venue_type='BAR' 
            OR venue_type='MALL'
         )

);

CREATE TABLE IF NOT EXISTS ticket (
    id bigserial primary key,
    name text not null check(name <> ''),
    coordinates_id integer not null references coordinates (coordinates_id) on delete cascade,
    creation_date timestamp not null default NOW(),
    price integer check(price > 0 OR price is NULL),
    ticket_type text not null,
    venue_id integer references venue (venue_id) on delete cascade,
	check(
	        ticket_type='VIP' 
            OR ticket_type='USUAL' 
            OR ticket_type='BUDGETARY' 
            OR ticket_type='CHEAP'
         )
);

CREATE TABLE IF NOT EXISTS user_item (
	login text not null references users (login) on delete cascade,
	id integer not null references ticket (id) on delete cascade
);

CREATE TABLE IF NOT EXISTS users (
	login text primary key,
	psswrd text not null,
	salt text not null,
	UNIQUE(login)
);
