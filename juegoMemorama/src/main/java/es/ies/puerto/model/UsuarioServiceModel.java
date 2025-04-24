package es.ies.puerto.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import es.ies.puerto.model.abtrastas.Conexion;

public class UsuarioServiceModel extends Conexion {

    public UsuarioServiceModel() {}

    public UsuarioServiceModel(String unaRutaArchivoBD) throws SQLException {
        super(unaRutaArchivoBD);
    }

    public ArrayList<UsuarioEntity> obtenerUsuarios() throws SQLException {
        String sql = "SELECT * FROM Usuario";
        return obtenerUsuario(sql);
    }

    public ArrayList<UsuarioEntity> obtenerUsuario(String sql) throws SQLException {
        ArrayList<UsuarioEntity> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                usuarios.add(new UsuarioEntity(
                        rs.getString("email"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contrasenia")
                ));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            cerrar();
        }

        return usuarios;
    }

    public int obtenerIdUsuario(String dato) throws SQLException {
        String sql = "SELECT id FROM usuario WHERE email = ? OR nombre_usuario = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, dato);
            stmt.setString(2, dato);
            rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt("id");

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            cerrar();
        }

        return -1;
    }

    public UsuarioEntity obtenerCredencialesUsuario(String nombreOEmail) {
        UsuarioEntity usuario = null;
        Connection conn = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM usuario WHERE email = ? OR nombre_usuario = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombreOEmail);
            ps.setString(2, nombreOEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("id");
                usuario = new UsuarioEntity(rs.getString("email"), rs.getString("nombre_usuario"), rs.getString("contrasenia"));

                // Estadísticas
                sql = "SELECT * FROM estadisticas_usuario WHERE id_usuario = ? AND dificultad = 'facil'";
                PreparedStatement psEst = conn.prepareStatement(sql);
                psEst.setInt(1, idUsuario);
                ResultSet rsEst = psEst.executeQuery();

                if (rsEst.next()) {
                    usuario.setVictoriasNormal(rsEst.getInt("victorias_normal"));
                    usuario.setMejorTiempoNormal(rsEst.getInt("mejor_tiempo_normal"));
                    usuario.setVictoriasContrareloj(rsEst.getInt("victorias_contrareloj"));
                }

                rsEst.close();
                psEst.close();
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { cerrar(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return usuario;
    }

    public boolean agregarUsuario(UsuarioEntity usuario) throws SQLException {
        if (usuario == null) return false;

        String sql = "INSERT INTO usuario (nombre_usuario, email, contrasenia) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getContrasenia());

            return stmt.executeUpdate() > 0;

        } finally {
            if (stmt != null) stmt.close();
            cerrar();
        }
    }

    public boolean editarUsuario(UsuarioEntity usuario) throws SQLException {
        if (usuario == null) return false;

        String sql = "UPDATE usuario SET nombre_usuario = ?, email = ?, contrasenia = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getContrasenia());

            return stmt.executeUpdate() > 0;

        } finally {
            if (stmt != null) stmt.close();
            cerrar();
        }
    }

    public boolean editarUsuario(UsuarioEntity usuario, String emailOriginal) throws SQLException {
        if (usuario == null || emailOriginal == null || emailOriginal.isEmpty()) return false;

        String sql = "UPDATE usuario SET nombre_usuario = ?, email = ?, contrasenia = ? WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getContrasenia());
            stmt.setString(4, emailOriginal);

            return stmt.executeUpdate() > 0;

        } finally {
            if (stmt != null) stmt.close();
            cerrar();
        }
    }

    public boolean eliminarUsuario(String email) throws SQLException {
        if (email == null || email.isEmpty()) return false;

        String sql = "DELETE FROM usuario WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            return stmt.executeUpdate() > 0;

        } finally {
            if (stmt != null) stmt.close();
            cerrar();
        }
    }

    public boolean actualizarEstadisticas(UsuarioEntity usuario) throws SQLException {
        String sql = "UPDATE usuario SET victorias_facil = ?, victorias_normal = ?, victorias_dificil = ?, " +
                     "mejor_tiempo_normal = ?, victorias_contrareloj = ?, derrotas_totales = ? WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuario.getVictoriasFacil());
            stmt.setInt(2, usuario.getVictoriasNormal());
            stmt.setInt(3, usuario.getVictoriasDificil());
            stmt.setInt(4, usuario.getMejorTiempoNormal());
            stmt.setInt(5, usuario.getVictoriasContrareloj());
            stmt.setInt(6, usuario.getDerrotasTotales());
            stmt.setString(7, usuario.getEmail());

            return stmt.executeUpdate() > 0;

        } finally {
            if (stmt != null) stmt.close();
            cerrar();
        }
    }

    public UsuarioEstadisticasEntity obtenerEstadisticasPorDificultad(String dato, String dificultad) throws SQLException {
        int idUsuario = obtenerIdUsuario(dato);
        if (idUsuario == -1) return null;

        String sql = "SELECT * FROM estadisticas_usuario WHERE id_usuario = ? AND dificultad = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setString(2, dificultad);
            rs = stmt.executeQuery();

            if (rs.next()) {
                UsuarioEstadisticasEntity est = new UsuarioEstadisticasEntity();
                est.setDificultad(dificultad);
                est.setVictoriasNormal(rs.getInt("victorias_normal"));
                est.setMejorTiempoNormal(rs.getInt("mejor_tiempo_normal"));
                est.setVictoriasContrareloj(rs.getInt("victorias_contrareloj"));
                return est;
            }

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            cerrar();
        }

        return null;
    }

    public boolean actualizarEstadisticasPorDificultad(String email, UsuarioEstadisticasEntity est) throws SQLException {
        int idUsuario = obtenerIdUsuario(email);
        if (idUsuario == -1) return false;
    
        String sql = "UPDATE estadisticas_usuario SET victorias_normal = ?, mejor_tiempo_normal = ?, victorias_contrareloj = ? WHERE id_usuario = ? AND dificultad = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, est.getVictoriasNormal());
            stmt.setInt(2, est.getMejorTiempoNormal());
            stmt.setInt(3, est.getVictoriasContrareloj());
            stmt.setInt(4, idUsuario);
            stmt.setString(5, est.getDificultad());
    
            return stmt.executeUpdate() > 0;
        }
    }
    
}
