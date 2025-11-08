INSERT INTO cursos (nombre, creditos) VALUES ('Programmer', 5);
INSERT INTO cursos (nombre, creditos) VALUES ('Developer', 5);
INSERT INTO cursos (nombre, creditos) VALUES ('Expert', 5);

INSERT INTO `users` (username, password, enabled) VALUES ('sergio', '$2a$10$vDqGjz5qKIITfDbGTHu41OoOGoG6bsY5TrqG7Qd64EJ.77J5Df8uK', 1);
INSERT INTO `users` (username, password, enabled) VALUES ('admin', '$2a$10$901c975e1LvP2.tfyMrouOqKaH2nxAHrWPAJn9RpuGeDOkCRYNqKC', 1);

INSERT INTO `authorities` (authorities.user_id, authority) VALUES (1, 'ROLE_USER');
INSERT INTO `authorities` (authorities.user_id, authority) VALUES (2, 'ROLE_ADMIN');
