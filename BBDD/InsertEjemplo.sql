SELECT * FROM usuarios;

INSERT INTO usuarios (mail, rol) VALUES ('paco@gmail.com', 'ROLE_CUSTOMER');


SELECT * FROM pisos;
INSERT INTO pisos (num_piso, nombre) VALUES (1, 'Juventudes');

SELECT * FROM salas;
INSERT INTO salas (maximo_personas, id_piso) VALUES (10, 1);


INSERT INTO reservas_salas (id_usuario, id_sala, fecha_reserva, fecha_fin_reserva) VALUES (1, 1, '2026-03-18 16:45:47', '2026-03-18 19:45:47');
SELECT * FROM reservas_salas;

SELECT * FROM autores;
INSERT INTO autores (nombre, fecha_nacimiento, nacionalidad) VALUES ('Paco Jones', DATE(now()), 'España');


SELECT * FROM estanterias;
INSERT INTO estanterias (categoria, id_piso) VALUES ('Aventura', 1);

SELECT * FROM baldas;
INSERT INTO baldas (id_estanteria) VALUES (1);

SELECT * FROM libros;
INSERT INTO libros(titulo, ISBN, id_autor, id_balda) VALUES ('Las mañanitas del rey', '1234567890987', 1, 1);

SELECT * FROM prestamo_libros;
INSERT INTO prestamo_libros (id_usuario, id_libro, fecha_prestamo) VALUES (1, 1, DATE(now()));


