DROP DATABASE IF EXISTS libreria;
CREATE DATABASE libreria
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE libreria;

-- Crear usuario (solo local) solo la primera vez
-- CREATE USER 'user_libreria_programa'@'localhost' IDENTIFIED BY 'EjemploPWD123';

-- Dar permisos SOLO sobre esta BBDD
GRANT SELECT, INSERT, UPDATE, DELETE
ON libreria.* TO 'user_libreria_programa'@'localhost';

FLUSH PRIVILEGES;


CREATE TABLE usuarios(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mail VARCHAR(254) NOT NULL,
    pwd VARCHAR(255) NOT NULL,
    rol ENUM('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_DEV') NOT NULL,
    moroso BOOLEAN NOT NULL DEFAULT FALSE,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_usuario_mail UNIQUE (mail)
);

CREATE TABLE pisos(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    num_piso INT NOT NULL,
    nombre VARCHAR(64) NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_piso_num UNIQUE (num_piso),
    CONSTRAINT chk_piso_num CHECK (num_piso >= 0)
);

CREATE TABLE estanterias(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(64) NOT NULL,
    id_piso BIGINT NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_estanteria_piso FOREIGN KEY (id_piso) REFERENCES pisos(id)
);

CREATE TABLE baldas(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL,
    id_estanteria BIGINT NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_balda_estanteria FOREIGN KEY (id_estanteria) REFERENCES estanterias(id),
    CONSTRAINT uq_balda_numero_estanteria UNIQUE (id_estanteria, numero),
    CONSTRAINT chk_balda_numero CHECK (numero > 0)
);

CREATE TABLE autores(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(124) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    nacionalidad VARCHAR(64) NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_autor_identidad UNIQUE (nombre, fecha_nacimiento)
);

CREATE TABLE libros(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(128) NOT NULL,
    isbn CHAR(13) NOT NULL,
    id_autor BIGINT NOT NULL,
    id_balda BIGINT NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_libro_isbn UNIQUE (isbn),
    CONSTRAINT fk_libro_autor FOREIGN KEY (id_autor) REFERENCES autores(id),
    CONSTRAINT fk_libro_balda FOREIGN KEY (id_balda) REFERENCES baldas(id)
);

CREATE TABLE salas(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(64) NOT NULL,
    maximo_personas INT NOT NULL,
    id_piso BIGINT NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_sala_nombre UNIQUE (nombre),
    CONSTRAINT fk_sala_piso FOREIGN KEY (id_piso) REFERENCES pisos(id),
    CONSTRAINT chk_sala_max_personas CHECK (maximo_personas > 0)
);

CREATE TABLE prestamo_libros(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_libro BIGINT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_prevista DATE GENERATED ALWAYS AS (
        DATE_ADD(fecha_prestamo, INTERVAL 1 MONTH)
    ) STORED,
    fecha_devolucion_real DATE NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_prestamo_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (id_libro) REFERENCES libros(id)
);

CREATE TABLE reservas_salas(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_sala BIGINT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    fecha_fin_reserva DATETIME NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    CONSTRAINT fk_reserva_sala FOREIGN KEY (id_sala) REFERENCES salas(id),
    CONSTRAINT chk_reserva_fechas CHECK (fecha_fin_reserva > fecha_reserva)
);

-- TRIGGERS

DELIMITER $$

CREATE TRIGGER trg_antes_insertar_reserva_sala
BEFORE INSERT ON reservas_salas
FOR EACH ROW
BEGIN
    IF EXISTS(
        SELECT 1
        FROM reservas_salas
        WHERE id_sala = NEW.id_sala
          AND NEW.fecha_reserva < fecha_fin_reserva
          AND NEW.fecha_fin_reserva > fecha_reserva
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ya se ha reservado la sala a esa hora';
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_antes_actualizar_reserva_sala
BEFORE UPDATE ON reservas_salas
FOR EACH ROW
BEGIN
    IF EXISTS(
        SELECT 1
        FROM reservas_salas
        WHERE id_sala = NEW.id_sala
          AND id <> NEW.id
          AND NEW.fecha_reserva < fecha_fin_reserva
          AND NEW.fecha_fin_reserva > fecha_reserva
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ya se ha reservado la sala a esa hora';
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_antes_insertar_prestamo_libros
BEFORE INSERT ON prestamo_libros
FOR EACH ROW
BEGIN
    IF EXISTS(
        SELECT 1
        FROM prestamo_libros
        WHERE id_libro = NEW.id_libro
          AND fecha_devolucion_real IS NULL
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ese libro ya esta prestado, no se encuentra disponible';
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_antes_actualizar_prestamo_libros
BEFORE UPDATE ON prestamo_libros
FOR EACH ROW
BEGIN
    IF EXISTS(
        SELECT 1
        FROM prestamo_libros
        WHERE id_libro = NEW.id_libro
          AND id <> NEW.id
          AND fecha_devolucion_real IS NULL
          AND NEW.fecha_devolucion_real IS NULL
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ese libro ya esta prestado, no se encuentra disponible';
    END IF;
END$$

DELIMITER ;

-- INDICES

CREATE INDEX idx_prestamo_libro_activo
ON prestamo_libros (id_libro, fecha_devolucion_real);

CREATE INDEX idx_prestamo_moroso
ON prestamo_libros (fecha_devolucion_prevista, fecha_devolucion_real);

CREATE INDEX idx_reserva_sala_fechas
ON reservas_salas (id_sala, fecha_reserva, fecha_fin_reserva);