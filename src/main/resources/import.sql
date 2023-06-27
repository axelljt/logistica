
/* Datos de clientes*/

INSERT INTO clientes (nombre,direccion,email,telefono) VALUES('Radio Shack', 'San Salvador, El Salvador', 'radioshack@gmail.com', '77758563');
INSERT INTO clientes (nombre,direccion,email,telefono) VALUES('Optica la Realeza', 'Santa Ana El Salvador', 'opticaRealeza@gmail.com', '88866236');

/*Datos de productos */

INSERT INTO productos (nombre_producto,descripcion) VALUES('Monitores LG','Monitores OLED para computadoras');
INSERT INTO productos (nombre_producto,descripcion) VALUES('Aros metalicos','Aros para lentes de sol');

/*Datos de bodegas */

INSERT INTO bodegas (nombre_bodega,ubicacion) VALUES('Bodega Central','Col Escalon San Salvador, El Salvador');
INSERT INTO bodegas (nombre_bodega,ubicacion) VALUES('Bodega Occidental','Santa Ana, El Salvador');

/*Datos de puertos */

INSERT INTO puertos (nombre_puerto,ubicacion) VALUES('Puerto Acajutla','SONSONATE, El Salvador');
INSERT INTO puertos (nombre_puerto,ubicacion) VALUES('Puerto cutuco','La UNION, El Salvador');

/* Creamos algunos usuarios con sus roles */

INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('andres','$2a$12$n7ORAc9w.FbjBSRQ72Bd2ep8xVsmUOYa9GhxoUMCAgCxVML.WpxZe',1, 'Andres', 'Guzman','profesor@bolsadeideas.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('admin','$2a$12$/vKf48k/5OPfzoNh9pJ9dO5c6Or9BMRgMWtKsQNn4YGmfP6bWKhSW',1, 'John', 'Doe','jhon.doe@bolsadeideas.com');

INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (1, 1);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2, 2);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2, 1);