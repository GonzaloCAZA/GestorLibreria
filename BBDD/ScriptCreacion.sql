DROP DATABASE IF EXISTS Libreria;
CREATE DATABASE Libreria;
USE Libreria;

-- Crear usuario (solo local) solo la primera vez
-- CREATE USER 'user_libreria_programa'@'localhost' IDENTIFIED BY 'EjemploPWD123';

-- Dar permisos SOLO sobre esta BBDD
GRANT SELECT, INSERT, UPDATE, DELETE
ON Libreria.* TO 'user_libreria_programa'@'localhost';

-- Aplicar cambios
FLUSH PRIVILEGES;

CREATE TABLE usuarios(
	id INT AUTO_INCREMENT PRIMARY KEY,
    mail VARCHAR(64) NOT NULL UNIQUE,
    rol ENUM('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_DEV') NOT NULL,
    moroso BOOLEAN NOT NULL DEFAULT FALSE,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE pisos(
	id INT AUTO_INCREMENT PRIMARY KEY,
    num_piso INT NOT NULL,
    nombre VARCHAR(64) NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE estanterias(
	id INT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(64) NOT NULL,
    id_piso INT NOT NULL,
    FOREIGN KEY (id_piso) REFERENCES pisos(id),
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE baldas(
	id INT AUTO_INCREMENT PRIMARY KEY,
    id_estanteria INT NOT NULL,
    FOREIGN KEY (id_estanteria) REFERENCES estanterias(id),
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE autores(
	id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(124) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    nacionalidad VARCHAR(64) NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE libros(
	id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(64) NOT NULL,
    ISBN VARCHAR(13) NOT NULL UNIQUE,
    id_autor INT NOT NULL,
    id_balda INT NOT NULL,    
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_autor) REFERENCES autores (id),
    FOREIGN KEY (id_balda) REFERENCES baldas (id)
);

CREATE TABLE salas(
	id INT AUTO_INCREMENT PRIMARY KEY,    
    maximo_personas INT NOT NULL,
    id_piso INT NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_piso) REFERENCES pisos (id)
);

CREATE TABLE prestamo_libros(
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    id_usuario INT NOT NULL,
    id_libro INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_prevista DATE GENERATED ALWAYS AS(date_add(fecha_prestamo, INTERVAL 1 MONTH)) STORED,
    fecha_devolucion_real DATE NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios (id),
    FOREIGN KEY (id_libro) REFERENCES libros (id)
);

CREATE TABLE reservas_salas(
	id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_sala INT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    fecha_fin_reserva DATETIME NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios (id),
    FOREIGN KEY (id_sala) REFERENCES salas (id)
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
        WHERE NEW.id_sala = id_sala
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
        SET MESSAGE_TEXT = 'Ese libro ya esta presato, no se encuentra disponible';
	END IF;
END$$

DELIMITER ;



-- INDICES
-- 1. Para comprobar si un libro está alquilado
CREATE INDEX idx_prestamo_libro_activo
ON prestamo_libros (id_libro, fecha_devolucion_real);

-- 2. Para detectar morosos (fechas)
CREATE INDEX idx_prestamo_moro
ON prestamo_libros (fecha_devolucion_prevista, fecha_devolucion_real);