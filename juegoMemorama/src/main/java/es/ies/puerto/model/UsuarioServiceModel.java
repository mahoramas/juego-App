package es.ies.puerto.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import es.ies.puerto.model.abtrastas.Conexion;

public class UsuarioServiceModel extends Conexion {

    /**
     * * Constructor vacio
     */
    public UsuarioServiceModel() {
    }
    
    /**
     * * Constructor con path de conexion
     * @param unaRutaArchivoBD ruta de la bbdd
     * @throws SQLException error controlado
     */
    public UsuarioServiceModel(String unaRutaArchivoBD) throws SQLException {
        super(unaRutaArchivoBD);
    }
    
    /**
     * * Funcion que devuelve la conexion a la bbdd
     * @return ArrayList<UsuarioEntity> lista de usuarios
     * @throws SQLException error controlado
     */
    public ArrayList<UsuarioEntity> obtenerUsuarios() throws SQLException {
        String sql = "SELECT * FROM Usuario";
        return obtenerUsuario(sql);
    }

    public ArrayList<UsuarioEntity> obtenerUsuario(String sql) throws SQLException {
        ArrayList<UsuarioEntity> usuarios = new ArrayList<UsuarioEntity>();
        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                String nombreStr = resultado.getString("nombre_usuario");
                String contraseniaStr = resultado.getString("contrasenia");
                String emailStr = resultado.getString("email");
                UsuarioEntity usuarioModel = new UsuarioEntity(emailStr, nombreStr, contraseniaStr);
                usuarios.add(usuarioModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return usuarios;
    }

    /**
     * * Metodo que obtiene las credenciales del usuario
     * @param dato 
     * @return UsuarioEntity usuario con las credenciales
     */
    public UsuarioEntity obtenerCredencialesUsuario(String dato) {
        try {
            String sql = "SELECT * FROM usuario WHERE email = ? OR nombre_usuario = ?";
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setString(1, dato);
            stmt.setString(2, dato);
    
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UsuarioEntity usuario = new UsuarioEntity(
                    rs.getString("email"),
                    rs.getString("nombre_usuario"),
                    rs.getString("contrasenia")
                );
    
                // Nuevas estadísticas
                usuario.setVictoriasFacil(rs.getInt("victorias_facil"));
                usuario.setVictoriasNormal(rs.getInt("victorias_normal"));
                usuario.setVictoriasDificil(rs.getInt("victorias_dificil"));
                usuario.setMejorTiempoNormal(rs.getInt("mejor_tiempo_normal"));
                usuario.setVictoriasContrareloj(rs.getInt("victorias_contrareloj"));
                usuario.setDerrotasTotales(rs.getInt("derrotas_totales"));
    
                return usuario;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                cerrar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    

    /**
     * * Metodo que obtiene las credenciales del usuario
     * @param usuario 
     * @return boolean true si se ha podido agregar el usuario
     * @throws SQLException error controlado
     */
    public boolean agregarUsuario(UsuarioEntity usuario) throws SQLException {
        if (usuario == null) {
            return false;
        }
        ArrayList<UsuarioEntity> usuarios = obtenerUsuarios();
        String sql = "INSERT  INTO usuario (nombre_usuario,email,contrasenia) VALUES ('" + usuario.getNombre() + "', '"
                + usuario.getEmail() + "', '" + usuario.getContrasenia() + "')";

        if (usuarios.contains(usuario)) {
            return false;
        }

        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            sentencia.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return true;
    }

    /**
     * * Metodo que edita el usuario
     * @param usuario
     * @return boolean true si se ha podido editar el usuario
     * @throws SQLException error controlado
     */
    public boolean editarUsuario(UsuarioEntity usuario) throws SQLException {
        if (usuario == null) {
            return false;
        }

        String sql = "UPDATE usuario SET nombre_usuario = '" + usuario.getNombre() + 
                     "', email = '" + usuario.getEmail() + 
                     "', contrasenia = '" + usuario.getContrasenia() + "'";

        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            sentencia.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }
    
    /**
     * * Metodo que edita el usuario
     * @param usuario 
     * @param emailOriginal 
     * @return boolean true si se ha podido editar el usuario
     * @throws SQLException error controlado
     */
    public boolean editarUsuario(UsuarioEntity usuario, String emailOriginal) throws SQLException {
        if (usuario == null || emailOriginal == null || emailOriginal.isEmpty()) {
            return false;
        }

        // Actualizar el registro basado en el email original
        String sql = "UPDATE usuario SET nombre_usuario = '" + usuario.getNombre() + 
                     "', email = '" + usuario.getEmail() + 
                     "', contrasenia = '" + usuario.getContrasenia() + 
                     "' WHERE email = '" + emailOriginal + "'";

        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            int filasActualizadas = sentencia.executeUpdate();
            return filasActualizadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }

    /**
     * * Metodo que elimina el usuario
     * @param email
     * @return boolean true si se ha podido eliminar el usuario
     * @throws SQLException error controlado
     */
    public boolean eliminarUsuario(String email) throws SQLException {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String sql = "DELETE FROM usuario WHERE email = '" + email + "'";

        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            int filasEliminadas = sentencia.executeUpdate();
            return filasEliminadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }


    /**
     * * Metodo que actualiza las estadisticas del usuario
     * @param usuario
     * @return boolean true si se ha podido actualizar el usuario
     * @throws SQLException error controlado
     */
    public boolean actualizarEstadisticas(UsuarioEntity usuario) throws SQLException {
        String sql = "UPDATE usuario SET " +
                     "victorias_facil = ?, " +
                     "victorias_normal = ?, " +
                     "victorias_dificil = ?, " +
                     "mejor_tiempo_normal = ?, " +
                     "victorias_contrareloj = ?, " +
                     "derrotas_totales = ? " +
                     "WHERE email = ?";
    
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, usuario.getVictoriasFacil());
            stmt.setInt(2, usuario.getVictoriasNormal());
            stmt.setInt(3, usuario.getVictoriasDificil());
            stmt.setInt(4, usuario.getMejorTiempoNormal());
            stmt.setInt(5, usuario.getVictoriasContrareloj());
            stmt.setInt(6, usuario.getDerrotasTotales());
            stmt.setString(7, usuario.getEmail());
    
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                cerrar(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

}
