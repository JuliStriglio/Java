package dao;

import model.Instalacion;
import model.TipoInstalacion;
import utils.ConexionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstalacionDAO {

	 public void agregarInstalacion(Instalacion i) {
	        String sql = "INSERT INTO instalaciones (nombre, tipo, horaApertura, horaCierre, direccion, precioxhora) VALUES (?, ?, ?, ?,?,?)";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            stmt.setString(1, i.getNombre());
	            stmt.setInt(2, i.getTipo().getId());
	            stmt.setTime(3, java.sql.Time.valueOf(i.getHoraApertura())); // LocalTime → SQL Time
	            stmt.setTime(4, java.sql.Time.valueOf(i.getHoraCierre()));
	            stmt.setString(5, i.getDireccion());
	            stmt.setDouble(6, i.getPrecioxhora());
	            stmt.executeUpdate();

	            ResultSet rs = stmt.getGeneratedKeys();
	            if (rs.next()) {
	                i.setId(rs.getInt(1));
	                System.out.println("Instalacion agregada con ID: " + i.getId());
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Buscar por ID
	    public Instalacion obtenerPorId(int id) {
	        String sql = "SELECT * FROM instalaciones WHERE id = ?";
	        Instalacion i = null;

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	            	
	            	TipoInstalacion tipo = new TipoInstalacion(rs.getInt("tipo"));
	                i = new Instalacion(
	                        rs.getInt("id"),
	                        rs.getString("nombre"),
	                        tipo,
	                        rs.getTime("horaApertura").toLocalTime(),
	                        rs.getTime("horaCierre").toLocalTime(),
	                        rs.getString("direccion"),
	                        rs.getDouble("precioxhora")
	                );
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return i;
	    }

	    
	    // List
	    public List<Instalacion> listarInstalacion() {
	        List<Instalacion> lista = new ArrayList<>();
	        String sql = "SELECT * FROM instalaciones";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	             ResultSet rs = stmt.executeQuery()) {

	            while (rs.next()) {
	
	            	TipoInstalacion tipo = new TipoInstalacion (rs.getInt("id"),rs.getString("nombre"));
	                Instalacion i = new Instalacion(
	                        rs.getInt("id"),
	                        rs.getString("nombre"),
	                        tipo,
	                        rs.getTime("horaApertura").toLocalTime(),
	                        rs.getTime("horaCierre").toLocalTime(),
	                        rs.getString("direccion"),
	                        rs.getDouble("precioxhora")
	                );
	                lista.add(i);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return lista;
	    }

	   
	    // Update
	    public void modificarInstalacion(Instalacion i) {
	        String sql = "UPDATE instalaciones SET nombre = ?, tipo = ?, horaApertura = ?, horaCierre = ?, direccion = ?, precioxhora = ? WHERE id = ?";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, i.getNombre());
	            stmt.setInt(2, i.getTipo().getId());
	            stmt.setTime(3, Time.valueOf(i.getHoraApertura()));
	            stmt.setTime(4, Time.valueOf(i.getHoraCierre()));// Convierte LocalTime a java.sql.Time
	            stmt.setString(5, i.getDireccion());
	            stmt.setDouble(6, i.getPrecioxhora());
	            stmt.setInt(7, i.getId());

	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Delete
	    public void eliminarInstalacion(int id) {
	        String sql = "DELETE FROM instalaciones WHERE id = ?";

	        try (Connection conn = ConexionUtil.getConexion();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);
	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}
