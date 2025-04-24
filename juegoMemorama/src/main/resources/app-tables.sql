-- Eliminar tablas antiguas si existen
DROP TABLE IF EXISTS estadisticas_usuario;
DROP TABLE IF EXISTS resumen_usuario;
DROP TABLE IF EXISTS usuario;

-- Crear tabla principal de usuarios
CREATE TABLE usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_usuario TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    contrasenia TEXT NOT NULL
);

-- Crear tabla de estadísticas por dificultad y modo (una fila por usuario y dificultad)
CREATE TABLE estadisticas_usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    dificultad TEXT CHECK(dificultad IN ('facil', 'medio', 'dificil')) NOT NULL,

    victorias_normal INTEGER DEFAULT 0,
    mejor_tiempo_normal INTEGER DEFAULT NULL, -- en segundos
    victorias_contrareloj INTEGER DEFAULT 0,

    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- Crear tabla de resumen global del usuario
CREATE TABLE resumen_usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    
    derrotas_totales INTEGER DEFAULT 0,
    mayor_racha INTEGER DEFAULT 0,
    racha_actual INTEGER DEFAULT 0,
    derrotas_consecutivas INTEGER DEFAULT 0,

    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- Insertar usuarios de ejemplo
INSERT INTO usuario (nombre_usuario, email, contrasenia) VALUES
    ('alvaro', 'alvaro@gmail.com', '123'),
    ('anita', 'ana@gmail.com', '098'),
    ('admin', 'admin@admin.com', 'admin');

-- Insertar estadísticas por dificultad para cada usuario
INSERT INTO estadisticas_usuario (id_usuario, dificultad) VALUES
    (1, 'facil'), (1, 'medio'), (1, 'dificil'),
    (2, 'facil'), (2, 'medio'), (2, 'dificil'),
    (3, 'facil'), (3, 'medio'), (3, 'dificil');

-- Insertar resumen global para cada usuario
INSERT INTO resumen_usuario (id_usuario) VALUES
    (1), (2), (3);
