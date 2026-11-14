-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema logitrack_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema logitrack_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `logitrack_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `logitrack_db` ;

-- -----------------------------------------------------
-- Table `logitrack_db`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`usuario` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`auditoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`auditoria` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `operacion` ENUM('insert', 'update', 'delete') NOT NULL,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_fk` BIGINT NULL DEFAULT NULL,
  `entidad` VARCHAR(45) NOT NULL,
  `entidad_id` BIGINT NOT NULL,
  `valores_antes` JSON NULL DEFAULT NULL,
  `valores_despues` JSON NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_auditoria_usuario` (`usuario_fk` ASC) VISIBLE,
  CONSTRAINT `fk_auditoria_usuario`
    FOREIGN KEY (`usuario_fk`)
    REFERENCES `logitrack_db`.`usuario` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`bodega`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`bodega` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `ciudad` VARCHAR(45) NOT NULL,
  `direccion` VARCHAR(45) NOT NULL,
  `capacidad` INT NOT NULL,
  `usuario_encargado_fk` BIGINT NOT NULL,
  `activado` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  INDEX `fk_bodega_encargado` (`usuario_encargado_fk` ASC) VISIBLE,
  CONSTRAINT `fk_bodega_encargado`
    FOREIGN KEY (`usuario_encargado_fk`)
    REFERENCES `logitrack_db`.`usuario` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`categoria` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`rol` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`credenciales`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`credenciales` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `usuario_fk` BIGINT NOT NULL,
  `rol_fk` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username` (`username` ASC) VISIBLE,
  UNIQUE INDEX `usuario_fk` (`usuario_fk` ASC) VISIBLE,
  INDEX `fk_credenciales_rol` (`rol_fk` ASC) VISIBLE,
  CONSTRAINT `fk_credenciales_rol`
    FOREIGN KEY (`rol_fk`)
    REFERENCES `logitrack_db`.`rol` (`id`),
  CONSTRAINT `fk_credenciales_usuario`
    FOREIGN KEY (`usuario_fk`)
    REFERENCES `logitrack_db`.`usuario` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`producto` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `categoria_fk` BIGINT NOT NULL,
  `precio` DECIMAL(12,2) NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  INDEX `fk_producto_categoria` (`categoria_fk` ASC) VISIBLE,
  CONSTRAINT `fk_producto_categoria`
    FOREIGN KEY (`categoria_fk`)
    REFERENCES `logitrack_db`.`categoria` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`inventario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`inventario` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `bodega_fk` BIGINT NOT NULL,
  `producto_fk` BIGINT NOT NULL,
  `stock` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_inventario_bodega_producto` (`bodega_fk` ASC, `producto_fk` ASC) VISIBLE,
  INDEX `fk_inventario_producto` (`producto_fk` ASC) VISIBLE,
  CONSTRAINT `fk_inventario_bodega`
    FOREIGN KEY (`bodega_fk`)
    REFERENCES `logitrack_db`.`bodega` (`id`),
  CONSTRAINT `fk_inventario_producto`
    FOREIGN KEY (`producto_fk`)
    REFERENCES `logitrack_db`.`producto` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`movimiento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`movimiento` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tipo_movimiento` ENUM('entrada', 'salida', 'transferencia') NOT NULL,
  `usuario_fk` BIGINT NOT NULL,
  `bodega_origen_fk` BIGINT NULL DEFAULT NULL,
  `bodega_destino_fk` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_movimiento_usuario` (`usuario_fk` ASC) VISIBLE,
  INDEX `fk_movimiento_bodega_origen` (`bodega_origen_fk` ASC) VISIBLE,
  INDEX `fk_movimiento_bodega_destino` (`bodega_destino_fk` ASC) VISIBLE,
  CONSTRAINT `fk_movimiento_bodega_destino`
    FOREIGN KEY (`bodega_destino_fk`)
    REFERENCES `logitrack_db`.`bodega` (`id`),
  CONSTRAINT `fk_movimiento_bodega_origen`
    FOREIGN KEY (`bodega_origen_fk`)
    REFERENCES `logitrack_db`.`bodega` (`id`),
  CONSTRAINT `fk_movimiento_usuario`
    FOREIGN KEY (`usuario_fk`)
    REFERENCES `logitrack_db`.`usuario` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `logitrack_db`.`movimiento_detalle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `logitrack_db`.`movimiento_detalle` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `movimiento_fk` BIGINT NOT NULL,
  `producto_fk` BIGINT NOT NULL,
  `cantidad` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_detalle_movimiento` (`movimiento_fk` ASC) VISIBLE,
  INDEX `fk_detalle_producto` (`producto_fk` ASC) VISIBLE,
  CONSTRAINT `fk_detalle_movimiento`
    FOREIGN KEY (`movimiento_fk`)
    REFERENCES `logitrack_db`.`movimiento` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_detalle_producto`
    FOREIGN KEY (`producto_fk`)
    REFERENCES `logitrack_db`.`producto` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

