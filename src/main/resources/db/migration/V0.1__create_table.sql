drop table if exists classes;

create table classes (
    id int auto_increment primary key,
    class_number int4 null,
    fio varchar(255) null,
    subject varchar(255) null,
    estimation int4 null,
    date_receive timestamp NULL
);