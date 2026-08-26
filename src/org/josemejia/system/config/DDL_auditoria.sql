create database if not exists auditoria_usuario_producto_in4av;
use auditoria_usuario_producto_in4av;

create table if not exists usuarios (
    id_user varchar(36) not null,
    name varchar(50) not null check (length(name) <= 50),
    lastname varchar(50) not null check (length(lastname) <= 50),
    email varchar(50) not null check (length(email) <= 50),
    user varchar(25) not null check (length(user) <= 25),
    password varchar(35) not null check (length(password) <= 35),
    rol varchar(20) not null default 'usuario',
    constraint pk_usuarios primary key (id_user),
    constraint uq_usuarios_user unique (user)
);

create table if not exists productos (
    id_producto varchar(36) not null,
    nombre varchar(150) not null,
    precio decimal(10,2) not null,
    descripcion varchar(500),
    categoria varchar(80),
    imagen varchar(500),
    constraint pk_productos primary key (id_producto)
);

create table if not exists auditoria (
    id_auditoria varchar(36) not null,
    usuario varchar(25) not null,
    accion varchar(50) not null,
    entidad varchar(50) not null,
    detalle varchar(500),
    fecha timestamp default current_timestamp,
    constraint pk_auditoria primary key (id_auditoria)
);

create table if not exists compras (
    id_compra varchar(36) not null,
    usuario varchar(25) not null,
    fecha timestamp default current_timestamp,
    total decimal(10,2) not null,
    constraint pk_compras primary key (id_compra)
);

create table if not exists detalle_compra (
    id_detalle varchar(36) not null,
    id_compra varchar(36) not null,
    id_producto varchar(36) not null,
    cantidad int not null,
    precio_unitario decimal(10,2) not null,
    constraint pk_detalle_compra primary key (id_detalle),
    constraint fk_detalle_compra_compra foreign key (id_compra) references compras(id_compra),
    constraint fk_detalle_compra_producto foreign key (id_producto) references productos(id_producto)
);
