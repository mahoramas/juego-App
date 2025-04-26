package es.ies.puerto.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import es.ies.puerto.model.abtrastas.Conexion;

public class UsuarioServiceModel extends Conexion  {

    /**
     * Constructor vacio
     */
    public UsuarioServiceModel() {
    }

    /**
     * Constructor con la ruta de la base de datos
     * @param unaRutaArchivoBD path de la base de datos
     * @throws SQLException
     */
    public UsuarioServiceModel(String unaRutaArchivoBD) throws SQLException {
        super(unaRutaArchivoBD);
    }

    /**
     * Metodo para obtener a un usuario
     * @return un usuario
     * @throws SQLException
     */
    public ArrayList<UsuarioEntity> obtenerUsuarios() throws SQLException {
        String sql = "SELECT * FROM Usuario";
        return obtenerUsuario(sql);
    }

    /**
     * Metodo para obtener una lista de usuarios
     * @param sql consulta de la base de datos
     * @return una lista de usuarios
     * @throws SQLException
     */
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
                        rs.getString("contrasenia")));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            cerrar();
        }

        return usuarios;
    }

    /**
     * Metodo para obtener el id de un usuario
     * @param dato usuario del que queremos el id
     * @return
     * @throws SQLException
     */
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

            if (rs.next())
                return rs.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Metodo para obtener todos los datos de un usuario
     * @param nombreOEmail nombre o email del usuario
     * @return un usuario
     */
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
                usuario = new UsuarioEntity(
                    rs.getString("email"),
                    rs.getString("nombre_usuario"),
                    rs.getString("contrasenia")
                );
    
                int mejorTiempoGeneral = 0;
                int victoriasContrarrelojTotal = 0;
    
                String[] dificultades = { "facil", "medio", "dificil" };
                for (String dificultad : dificultades) {
                    String sqlEst = "SELECT * FROM estadisticas_usuario WHERE id_usuario = ? AND dificultad = ?";
                    PreparedStatement psEst = conn.prepareStatement(sqlEst);
                    psEst.setInt(1, idUsuario);
                    psEst.setString(2, dificultad);
                    ResultSet rsEst = psEst.executeQuery();
    
                    if (rsEst.next()) {
                        int victorias = rsEst.getInt("victorias_normal");
                        int mejorTiempo = rsEst.getInt("mejor_tiempo_normal");
                        int victoriasContrarreloj = rsEst.getInt("victorias_contrareloj");
    
                        switch (dificultad) {
                            case "facil":
                                usuario.setVictoriasFacil(victorias);
                                break;
                            case "medio":
                                usuario.setVictoriasMedio(victorias);
                                break;
                            case "dificil":
                                usuario.setVictoriasDificil(victorias);
                                break;
                        }
    
                        if (mejorTiempoGeneral == 0 || (mejorTiempo > 0 && mejorTiempo < mejorTiempoGeneral)) {
                            mejorTiempoGeneral = mejorTiempo;
                        }
    
                        victoriasContrarrelojTotal += victoriasContrarreloj;
                    }
    
                    rsEst.close();
                    psEst.close();
                }
    
                usuario.setMejorTiempoNormal(mejorTiempoGeneral);
                usuario.setVictoriasContrareloj(victoriasContrarrelojTotal);
    
                String sqlResumen = "SELECT * FROM resumen_usuario WHERE id_usuario = ?";
                PreparedStatement psResumen = conn.prepareStatement(sqlResumen);
                psResumen.setInt(1, idUsuario);
                ResultSet rsResumen = psResumen.executeQuery();
    
                if (rsResumen.next()) {
                    usuario.setDerrotasTotales(rsResumen.getInt("derrotas_totales"));
                    usuario.setMayorRacha(rsResumen.getInt("mayor_racha"));
                    usuario.setRachaVictoria(rsResumen.getInt("racha_actual"));
                    usuario.setRachaDerrota(rsResumen.getInt("derrotas_consecutivas"));
                }
    
                rsResumen.close();
                psResumen.close();
            }
    
            rs.close();
            ps.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                cerrar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return usuario;
    }
    
    
    /**
     * Metodo que agrega un usuario a la base de datos
     * @param usuario usuario al que queremos agregar
     * @return true o false de la insercion
     * @throws SQLException
     */
    public boolean agregarUsuario(UsuarioEntity usuario) throws SQLException {
        if (usuario == null)
            return false;

        String sqlInsertUsuario = "INSERT INTO usuario (nombre_usuario, email, contrasenia) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtEstadisticas = null;
        PreparedStatement stmtResumen = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sqlInsertUsuario);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getContrasenia());
            stmt.executeUpdate();

            int idUsuario = obtenerIdUsuario(usuario.getEmail());

            String sqlEstadisticas = "INSERT INTO estadisticas_usuario (id_usuario, dificultad, victorias_normal, mejor_tiempo_normal, victorias_contrareloj) VALUES (?, ?, 0, 0, 0)";

            try {
                stmtEstadisticas = conn.prepareStatement(sqlEstadisticas);
            } catch (SQLException e) {
                System.err.println("¡Fallo preparando stmtEstadisticas!");
                e.printStackTrace();
                return false;
            }
            for (String dificultad : new String[] { "facil", "medio", "dificil" }) {
                stmtEstadisticas.setInt(1, idUsuario);
                stmtEstadisticas.setString(2, dificultad);
                stmtEstadisticas.executeUpdate();
            }

            String sqlResumen = "INSERT INTO resumen_usuario (id_usuario, derrotas_totales, mayor_racha, racha_actual, derrotas_consecutivas) VALUES (?, 0, 0, 0, 0)";
            stmtResumen = conn.prepareStatement(sqlResumen);
            stmtResumen.setInt(1, idUsuario);
            stmtResumen.executeUpdate();

            return true;

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (stmtEstadisticas != null) {
                stmtEstadisticas.close();
            }
            if (stmtResumen != null) {
                stmtResumen.close();
            }
            cerrar();

        }
    }

    /**
     * Metodo para editar los valores de un usuario
     * @param usuario usuario al que queremos editar
     * @param emailOriginal email del usuario
     * @return true o false de la edicion
     * @throws SQLException
     */
    public boolean editarUsuario(UsuarioEntity usuario, String emailOriginal) throws SQLException {
        if (usuario == null || emailOriginal == null || emailOriginal.isEmpty())
            return false;

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
            if (stmt != null)
                stmt.close();
            cerrar();
        }
    }

    /**
     * Metodo para eliminar un usuario de la base de datos
     * @param email email del usuario
     * @return true o false de la eliminacion
     * @throws SQLException
     */
    public boolean eliminarUsuario(String email) throws SQLException {
        if (email == null || email.isEmpty())
            return false;

        String sql = "DELETE FROM usuario WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            return stmt.executeUpdate() > 0;

        } finally {
            if (stmt != null)
                stmt.close();
            cerrar();
        }
    }

    /**
     * Metodo que actualiza las estadisticas en la base de datos
     * @param usuario usuario al que le editamos las estadisticas
     * @param dificultad dificultad para editar las estadisticas
     * @return
     * @throws SQLException
     */
    public boolean actualizarEstadisticas(UsuarioEntity usuario, String dificultad) throws SQLException {
        String sql = "UPDATE estadisticas_usuario SET  victorias_normal = ? " +
                ", mejor_tiempo_normal = ?, victorias_contrareloj = ? WHERE id_usuario = ? and dificultad = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql2 = "SELECT id FROM usuario WHERE nombre_usuario = ?";
        stmt = conn.prepareStatement(sql2);
        stmt.setString(1, usuario.getNombre());

        int id = 0;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql2);
            ResultSet rsEst = stmt.executeQuery();
            if (rsEst.next()) {
                id = rsEst.getInt("id");
            }

        } finally {
            if (stmt != null)
                stmt.close();
            cerrar();
        }
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuario.getVictoriasTotal());
            stmt.setInt(2, usuario.getMejorTiempoNormal());
            stmt.setInt(3, usuario.getVictoriasContrareloj());
            stmt.setInt(4, id);
            stmt.setString(5, dificultad);

            return stmt.executeUpdate() > 0;

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            cerrar();
        }
    }

    /**
     * Metodo para obtener las estadisticas de un usuario por dificultad
     * @param usuario usuario del que necesitamos los datos
     * @param dificultad dificultad de la cual queremos hacer la busqueda
     * @return las estadisticas del usuario
     * @throws SQLException
     */
    public UsuarioEstadisticasEntity obtenerEstadisticasPorDificultad(String usuario, String dificultad)
            throws SQLException {
        int idUsuario = obtenerIdUsuario(usuario);
        if (idUsuario == -1)
            return null;

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * metodo para actualizar las estadisticas de un usuario por la dificultad
     * @param email email del usuario
     * @param est estadisticas del usuario
     * @return true o false de la actualizacion
     * @throws SQLException
     */
    public boolean actualizarEstadisticasPorDificultad(String email, UsuarioEstadisticasEntity est)
            throws SQLException {
        int idUsuario = obtenerIdUsuario(email);
        if (idUsuario == -1)
            return false;

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

    /**
     * Actualiza las estadisticas de la tabla resumen_Usuario
     * @param email email del usuario
     * @param usuario usuario al que queremos actualizar
     * @return true o false de la actualizacion
     * @throws SQLException
     */
    public boolean actualizarResumenUsuario(String email, UsuarioEntity usuario) throws SQLException {
        int idUsuario = obtenerIdUsuario(email);
        if (idUsuario == -1)
            return false;

        String sql = "UPDATE resumen_usuario SET derrotas_totales = ?, mayor_racha = ?, racha_actual = ?, derrotas_consecutivas = ? WHERE id_usuario = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuario.getDerrotasTotales());
            stmt.setInt(2, usuario.getMayorRacha());
            stmt.setInt(3, usuario.getRachaVictoria());
            stmt.setInt(4, usuario.getRachaDerrota());
            stmt.setInt(5, idUsuario);

            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
