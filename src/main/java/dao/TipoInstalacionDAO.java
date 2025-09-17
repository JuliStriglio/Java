package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.TipoInstalacion;
import utils.ConexionUtil;

public class TipoInstalacionDAO {

	 public void agregarTipoInstalacion(TipoInstalacion t) {
	        String sql = "INSERT INTO tipoInstalaciones (nombre, descripcion) VALUES (?, ?)";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            stmt.setString(1, t.getNombre());
	            stmt.setString(2, t.getDescripcion());

	            stmt.executeUpdate();

	            ResultSet rs = stmt.getGeneratedKeys();
	            if (rs.next()) {
	                t.setId(rs.getInt(1));
	                System.out.println("Tipo instalacion agregado con ID: " + t.getId());
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Buscar por ID
	    public static TipoInstalacion obtenerPorId(int id) {
	        String sql = "SELECT * FROM tipoInstalaciones WHERE id = ?";
	        TipoInstalacion t = null;

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                t = new TipoInstalacion(
	                        rs.getInt("id"),
	                        rs.getString("nombre"),
	                        rs.getString("descripcion")
	                );
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return t;
	    }

	    
	    // List
	    public List<TipoInstalacion> listarTipo() {
	        List<TipoInstalacion> lista = new ArrayList<>();
	        String sql = "SELECT * FROM tipoInstalaciones";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	             ResultSet rs = stmt.executeQuery()) {

	            while (rs.next()) {
	            	TipoInstalacion t = new TipoInstalacion(
	                        rs.getInt("id"),
	                        rs.getString("nombre"),
	                        rs.getString("descripcion")

	                );
	                lista.add(t);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return lista;
	    }

	   
	    // Update
	    public static void modificarTipoInstalacion(TipoInstalacion t) {
	        String sql = "UPDATE tipoInstalaciones SET nombre = ?, descripcion = ? WHERE id = ?";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, t.getNombre());
	            stmt.setString(2, t.getDescripcion());
	            stmt.setInt(3, t.getId());

	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Delete
	    public void eliminarTipoInstalacion(int id) {
	        String sql = "DELETE FROM tipoInstalaciones WHERE id = ?";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);
	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}
