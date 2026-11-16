create table if not exists types
(
    Id serial
        primary key,
    name varchar(20) not null
);

create table if not exists marshs
(
    Id serial
        primary key,
    name varchar(40) not null
);

create table if not exists posts
(
    Id serial
        primary key,
    name varchar(30) not null
);

create table if not exists parts
(
    Id serial
        primary key,
    name varchar(40) not null
);

create table if not exists places
(
    Id serial
        primary key,
    name varchar(40) not null
);

create table if not exists type_properties
(
    Id  serial
        primary key,
    type_Id integer not null,
       foreign key (type_Id) references types(Id),
    name varchar(40) not null
);

create table if not exists transport
(
    Id serial
        primary key,
    type_Id integer not null,
       foreign key (type_Id) references types(Id),
    place_Id integer not null,
       foreign key (place_Id) references places(Id),  
    dt date not null,
    working_life date not null
);

create table if not exists type_value
(
    Id  serial
        primary key,
    transport_Id integer not null,
       foreign key (transport_Id) references transport(id),
    prop_Id integer not null,
       foreign key (prop_Id) references type_properties(Id),  
    value integer not null
);

create table if not exists empl
(
    Id  serial
        primary key,
    FIO             varchar(50) not null,
    post_Id             integer not null,
       foreign key (post_Id) references posts(Id),
    head_Id           integer not null,
    check(head_Id >= 0)
);

create table if not exists transport_empl
(
    Id  serial
        primary key,
    transport_Id integer not null,
       foreign key (transport_Id) references transport(id),
    empl_Id   integer not null,
       foreign key (empl_Id) references empl(Id)
);

create table if not exists repair_reports
(
    Id     serial 
        primary key,
    date_of_receipt date not null,
    completion_date date not null,
    transport_Id integer not null,
       foreign key (transport_Id) references transport(id),
    work_type varchar(200) not null,
    cost integer not null,
    check(cost >= 0),
    empl_Id   integer not null,
       foreign key (empl_Id) references empl(Id)
);

create table if not exists repair_parts
(
    Id     serial 
        primary key,
    rr_Id integer not null,
       foreign key (rr_Id) references repair_reports(Id),
    part_Id   integer not null,
       foreign key (part_Id) references parts(Id),
    count integer not null,
        check(count > 0)   
);

create table if not exists schedules
(
    Id  serial
        primary key,
    transport_Id integer not null,
       foreign key (transport_Id) references transport(id),
    marshrut_Id integer not null,
       foreign key (marshrut_Id) references marshs(Id)
);

create table if not exists daily_reports
(
    Id  serial 
        primary key,
    transport_Id integer not null,
       foreign key (transport_Id) references transport(id),    
    report_date date not null,
    tickets_count integer not null,
    check(tickets_count >= 0),
    mileage integer not null,
    cargo_transportation integer not null
);

DROP TABLE types CASCADE;
DROP TABLE marshs CASCADE;
DROP TABLE posts CASCADE;
DROP TABLE parts CASCADE;
DROP TABLE places CASCADE;
DROP TABLE transport CASCADE;
DROP TABLE type_properties CASCADE;
DROP TABLE type_value CASCADE;
DROP TABLE empl CASCADE;
DROP TABLE transport_empl CASCADE;
DROP TABLE repair_reports CASCADE;
DROP TABLE repair_parts CASCADE;
DROP TABLE schedules CASCADE;
DROP TABLE daily_reports CASCADE;