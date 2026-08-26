
use auditoria_usuario_producto_in4av;



drop procedure if exists sp_usuario_crear;
drop procedure if exists sp_usuario_login;

delimiter $$

 #create
create procedure sp_usuario_crear(
    in p_name varchar(50),
    in p_lastname varchar(50),
    in p_email varchar(50),
    in p_user varchar(25),
    in p_password varchar(35),
    in p_rol varchar(20)
)
begin
    insert into usuarios (id_user, name, lastname, email, user, password, rol)
    values (uuid(), p_name, p_lastname, p_email, p_user, p_password, p_rol);
end$$

delimiter ;

delimiter $$
#read
create procedure sp_usuario_login(
    in p_user varchar(25),
    in p_password varchar(35)
)
begin
    select id_user, name, lastname, email, user, rol
    from usuarios
    where user = p_user and password = p_password;
end$$

delimiter ;

 #=================================
 #productos
 #======================================

drop procedure if exists sp_producto_crear;
drop procedure if exists sp_producto_listar;
drop procedure if exists sp_producto_actualizar;
drop procedure if exists sp_producto_eliminar;

delimiter $$

 #create
create procedure sp_producto_crear(
    in p_nombre varchar(150),
    in p_precio decimal(10,2),
    in p_descripcion varchar(500),
    in p_categoria varchar(80),
    in p_imagen varchar(500)
)
begin
    insert into productos (id_producto, nombre, precio, descripcion, categoria, imagen)
    values (uuid(), p_nombre, p_precio, p_descripcion, p_categoria, p_imagen);
end$$
delimiter ;

delimiter $$
 #read
create procedure sp_producto_listar()
begin
    select id_producto, nombre, precio, descripcion, categoria, imagen
    from productos;
end$$

delimiter ;


delimiter $$
#update
create procedure sp_producto_actualizar(
    in p_id_producto varchar(36),
    in p_nombre varchar(150),
    in p_precio decimal(10,2),
    in p_descripcion varchar(500),
    in p_categoria varchar(80),
    in p_imagen varchar(500)
)
begin
    update productos
    set nombre = p_nombre,
        precio = p_precio,
        descripcion = p_descripcion,
        categoria = p_categoria,
        imagen = p_imagen
    where id_producto = p_id_producto;
end$$

delimiter ;

delimiter $$
 #delete
create procedure sp_producto_eliminar(
    in p_id_producto varchar(36)
)
begin
    delete from productos where id_producto = p_id_producto;
end$$

delimiter ;

#=========================================
 #auditoria
#============================================





delimiter $$

#create
create procedure sp_auditoria_registrar(
    in p_usuario varchar(25),
    in p_accion varchar(50),
    in p_entidad varchar(50),
    in p_detalle varchar(500)
)
begin
    insert into auditoria (id_auditoria, usuario, accion, entidad, detalle)
    values (uuid(), p_usuario, p_accion, p_entidad, p_detalle);
end$$

delimiter ;


delimiter $$
#read
create procedure sp_auditoria_listar()
begin
    select id_auditoria, usuario, accion, entidad, detalle, fecha
    from auditoria
    order by fecha desc;
end$$

delimiter ;

delimiter $$

#create
create procedure sp_compra_crear(
    in p_id_compra varchar(36),
    in p_usuario varchar(25),
    in p_total decimal(10,2)
)
begin
    insert into compras (id_compra, usuario, total)
    values (p_id_compra, p_usuario, p_total);
end$$
delimiter ;

delimiter $$

create procedure sp_detalle_compra_crear(
    in p_id_compra varchar(36),
    in p_id_producto varchar(36),
    in p_cantidad int,
    in p_precio_unitario decimal(10,2)
)
begin
    insert into detalle_compra (id_detalle, id_compra, id_producto, cantidad, precio_unitario)
    values (uuid(), p_id_compra, p_id_producto, p_cantidad, p_precio_unitario);
end$$

delimiter ;
