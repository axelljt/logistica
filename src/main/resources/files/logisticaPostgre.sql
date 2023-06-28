CREATE TABLE "Clientes" (
  "id_cliente" Long PRIMARY KEY,
  "nombre_cliente" varchar,
  "direccion" varchar,
  "email" varchar,
  "telefono" varchar
);

CREATE TABLE "Productos" (
  "id_producto" Long PRIMARY KEY,
  "nombre_producto" varchar,
  "descripcion" varchar
);

CREATE TABLE "Bodegas" (
  "id_bodega" Long PRIMARY KEY,
  "nombre_bodega" varchar,
  "ubicacion" varchar
);

CREATE TABLE "Puertos" (
  "id_puerto" Long PRIMARY KEY,
  "nombre_puerto" varchar,
  "ubicacion" varchar
);

CREATE TABLE "Logistica_Terrestre" (
  "id_logistica_t" Long PRIMARY KEY,
  "id_cliente" Long,
  "id_producto" Long,
  "id_bodega" Long,
  "cantidad" integer,
  "fecha_registro" timestamp,
  "fecha_entrega" timestamp,
  "precio_envio" decimal(5,2),
  "precio_normal" decimal(5,2),
  "descuento" decimal(5,2),
  "placa_vehiculo" varchar,
  "numero_guia" varchar(10)
);

CREATE TABLE "Logistica_Maritima" (
  "id_logistica_m" Long PRIMARY KEY,
  "id_cliente" Long,
  "id_producto" Long,
  "id_puerto" Long,
  "cantidad" integer,
  "fecha_registro" timestamp,
  "fecha_entrega" timestamp,
  "precio_envio" decimal(5,2),
  "precio_normal" decimal(5,2),
  "descuento" decimal(5,2),
  "numero_flota" varchar,
  "numero_guia" varchar(10)
);

CREATE TABLE "Usuarios" (
  "id_usuario" Long PRIMARY KEY,
  "username" varchar,
  "password" varchar,
  "enabled" boolean
);

CREATE TABLE "Roles" (
  "id_role" Long PRIMARY KEY,
  "nombre" varchar
);

CREATE TABLE "Usuarios_Roles" (
  "id_usuario" Long,
  "id_role" Long
);

ALTER TABLE "Logistica_Terrestre" ADD FOREIGN KEY ("id_cliente") REFERENCES "Clientes" ("id_cliente");

ALTER TABLE "Logistica_Terrestre" ADD FOREIGN KEY ("id_producto") REFERENCES "Productos" ("id_producto");

ALTER TABLE "Logistica_Terrestre" ADD FOREIGN KEY ("id_bodega") REFERENCES "Bodegas" ("id_bodega");

ALTER TABLE "Logistica_Maritima" ADD FOREIGN KEY ("id_cliente") REFERENCES "Clientes" ("id_cliente");

ALTER TABLE "Logistica_Maritima" ADD FOREIGN KEY ("id_producto") REFERENCES "Productos" ("id_producto");

ALTER TABLE "Logistica_Maritima" ADD FOREIGN KEY ("id_puerto") REFERENCES "Puertos" ("id_puerto");

ALTER TABLE "Usuarios_Roles" ADD FOREIGN KEY ("id_usuario") REFERENCES "Usuarios" ("id_usuario");

ALTER TABLE "Usuarios_Roles" ADD FOREIGN KEY ("id_role") REFERENCES "Roles" ("id_role");
