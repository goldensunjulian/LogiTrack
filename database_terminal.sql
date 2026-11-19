DROP DATABASE IF EXISTS logitrack_db;
CREATE DATABASE IF NOT EXISTS logitrack_db DEFAULT CHARACTER SET utf8mb4;
USE logitrack_db;

CREATE TABLE IF NOT EXISTS usuario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  email VARCHAR(225) NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS rol (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS credenciales (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  usuario_fk BIGINT NOT NULL,
  rol_fk BIGINT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY username (username),
  UNIQUE KEY usuario_fk (usuario_fk),
  FOREIGN KEY (usuario_fk) REFERENCES usuario (id),
  FOREIGN KEY (rol_fk) REFERENCES rol (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS auditoria (
  id BIGINT NOT NULL AUTO_INCREMENT,
  operacion ENUM('insert','update','delete') NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usuario_fk BIGINT NOT NULL,
  entidad VARCHAR(45) NOT NULL,
  entidad_id BIGINT NOT NULL,
  valores_antes JSON NULL DEFAULT NULL,
  valores_despues JSON NULL DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (usuario_fk) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS categoria (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS bodega (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  ciudad VARCHAR(100) NOT NULL,
  direccion VARCHAR(150) NOT NULL,
  capacidad INT NOT NULL,
  usuario_encargado_fk BIGINT NULL DEFAULT NULL,
  activado TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  FOREIGN KEY (usuario_encargado_fk) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS producto (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  categoria_fk BIGINT NOT NULL,
  precio DECIMAL(12,2) NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  FOREIGN KEY (categoria_fk) REFERENCES categoria (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inventario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  bodega_fk BIGINT NOT NULL,
  producto_fk BIGINT NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uq_inventario (bodega_fk, producto_fk),
  FOREIGN KEY (bodega_fk) REFERENCES bodega (id),
  FOREIGN KEY (producto_fk) REFERENCES producto (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movimiento (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  tipo_movimiento ENUM('entrada','salida','transferencia') NOT NULL,
  usuario_fk BIGINT NOT NULL,
  bodega_origen_fk BIGINT NULL DEFAULT NULL,
  bodega_destino_fk BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (usuario_fk) REFERENCES usuario (id),
  FOREIGN KEY (bodega_origen_fk) REFERENCES bodega (id),
  FOREIGN KEY (bodega_destino_fk) REFERENCES bodega (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movimiento_detalle (
  id BIGINT NOT NULL AUTO_INCREMENT,
  movimiento_fk BIGINT NOT NULL,
  producto_fk BIGINT NOT NULL,
  cantidad INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (movimiento_fk) REFERENCES movimiento (id) ON DELETE CASCADE,
  FOREIGN KEY (producto_fk) REFERENCES producto (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data inicial
INSERT INTO rol (nombre) VALUES ('SUPERADMIN');
INSERT INTO rol (nombre) VALUES ('ADMIN');
INSERT INTO rol (nombre) VALUES ('EMPLEADO');

INSERT INTO categoria (nombre) VALUES ('Electronica');
INSERT INTO categoria (nombre) VALUES ('Herramientas');
INSERT INTO categoria (nombre) VALUES ('Papeleria');

INSERT INTO usuario (nombre, email, activo) VALUES ('Julian', 'tristan@gmail.com', 1);
INSERT INTO credenciales (username, password, usuario_fk, rol_fk) VALUES ('julian', '1234', 1, 1);

INSERT INTO usuario (nombre, email, activo) VALUES ('Administrador', 'admin@logitrack.com', 1);
INSERT INTO credenciales (username, password, usuario_fk, rol_fk) VALUES ('admin', '1234', 2, 2);

INSERT INTO usuario (nombre, email, activo) VALUES ('Carlos Lopez', 'carlos@logitrack.com', 1);
INSERT INTO credenciales (username, password, usuario_fk, rol_fk) VALUES ('carlos', '1234', 3, 3);

INSERT INTO producto (nombre, categoria_fk, precio, activo) VALUES ('Monitor 24 pulgadas', 1, 850000.00, 1);
INSERT INTO producto (nombre, categoria_fk, precio, activo) VALUES ('Taladro Industrial', 2, 320000.00, 1);
INSERT INTO producto (nombre, categoria_fk, precio, activo) VALUES ('Resma de Papel', 3, 15000.00, 1);
