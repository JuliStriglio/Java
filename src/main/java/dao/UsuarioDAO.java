package dao;

import model.Usuario;
import utils.ConexionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

	 public void agregarUsuario(Usuario u) {
	        String sql = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?)";
	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            stmt.setString(1, u.getNombre());
	            stmt.setString(2, u.getEmail());
	            stmt.setString(3, u.getPassword());
	            stmt.setBoolean(4, u.getRol());
	            stmt.executeUpdate();

	            ResultSet rs = stmt.getGeneratedKeys();
	            if (rs.next()) {
	                u.setId(rs.getInt(1));
	                System.out.println("Usuario agregado con ID: " + u.getId());
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    //Buscar por ID
	    public Usuario obtenerPorId(int id) {
	        String sql = "SELECT * FROM usuarios WHERE id = ?";
	        Usuario u = null;

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                u = new Usuario(
	                        rs.getInt("id"),
	                        rs.getString("nombre"),
	                        rs.getString("email"),
	                        rs.getString("password"),
	                        rs.getBoolean("rol")
	                );
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return u;
	    }

	    // List
	    public List<Usuario> listarUsuarios() {
	        List<Usuario> lista = new ArrayList<>();
	        String sql = "SELECT * FROM usuarios";
	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	             ResultSet rs = stmt.executeQuery()) {

	            while (rs.next()) {
	                Usuario u = new Usuario(
	                        rs.getInt("id"),
	                        rs.getString("nombre"),
	                        rs.getString("email"),
	                        rs.getString("password"),
	                        rs.getBoolean("rol")
	                );
	                lista.add(u);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return lista;
	    }

	    // Update
	    public void modificarUsuario(Usuario u) {
	        String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ?, rol = ? WHERE id = ?";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, u.getNombre());
	            stmt.setString(2, u.getEmail());
	            stmt.setString(3, u.getPassword());
	            stmt.setBoolean(4, u.getRol());
	            stmt.setInt(5, u.getId());

	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Delete
	    public void eliminarUsuario(int id) {
	        String sql = "DELETE FROM usuarios WHERE id = ?";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);
	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public Usuario verificarCredenciales(String email, String password) {
	        Usuario usuario = null;
	        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setString(1, email);
	            ps.setString(2, password);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    
	                    usuario = new Usuario(
	                        rs.getInt("id"),
	                        rs.getString("nombre"),
	                        rs.getString("email"),
	                        rs.getString("password"),
	                        rs.getBoolean("rol")
	                    );
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return usuario;
	    }
}
