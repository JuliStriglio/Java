package dao;

import model.Reserva;
import model.Instalacion;
import model.Usuario;
import model.EstadoReserva;
import utils.ConexionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

		 public void agregarReserva(Reserva r) {
		        String sql = "INSERT INTO reservas (usuario, instalacion, fecha, horaInicio, horaFin,estado, monto) VALUES (?, ?, ?, ?,?,?,?)";

		        try (Connection conn = ConexionUtil.getConexion();
		             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
		        	
		        	stmt.setInt(1, r.getUsuario().getId());
		        	stmt.setInt(2, r.getInstalacion().getId());
		        	stmt.setDate(3, java.sql.Date.valueOf(r.getFecha()));
		        	stmt.setTime(4, java.sql.Time.valueOf(r.getHoraInicio()));
		        	stmt.setTime(5, java.sql.Time.valueOf(r.getHoraFin()));
		        	stmt.setString(6, r.getEstado().name()); // 👉 Ya trae "PENDIENTE"
		        	stmt.setDouble(7, r.getMonto());
		        	
		            stmt.executeUpdate();

		            ResultSet rs = stmt.getGeneratedKeys();
		            if (rs.next()) {
		                r.setId(rs.getInt(1));
		                System.out.println("Reserva agregada con ID: " + r.getId());
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		 

		    // Buscar por ID
		    public Reserva obtenerPorId(int id) {
		        String sql = "SELECT * FROM reservas WHERE id = ?";
		        Reserva r = null;

		        try (Connection conn = ConexionUtil.getConexion();
		             PreparedStatement stmt = conn.prepareStatement(sql)) {

		            stmt.setInt(1, id);
		            ResultSet rs = stmt.executeQuery();

		            if (rs.next()) {
		            	Usuario usuario = new Usuario();
		            	usuario.setId(rs.getInt("id"));
		            	
		            	Instalacion instalacion = new Instalacion();
		            	instalacion.setId(rs.getInt("id"));
		            	
		            	 EstadoReserva estado = EstadoReserva.valueOf(rs.getString("estado").toUpperCase());
		            	
		                r = new Reserva(
		                        rs.getInt("id"),
		                        usuario,
		                        instalacion,
		                        rs.getDate("fecha").toLocalDate(),
		                        rs.getTime("horaInicio").toLocalTime(),
		                        rs.getTime("horaFin").toLocalTime(),
		                        estado,
		                        rs.getDouble("monto")
		                        
		                );
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		        }

		        return r;
		    }

		    
		    // List
		    public List<Reserva> listarReserva() {
		        List<Reserva> lista = new ArrayList<>();
		        String sql =  "SELECT " +
		                "r.id AS res_id, r.fecha, r.horaInicio, r.horaFin, r.monto, r.estado, " +
		                "u.id AS usu_id, u.nombre AS usu_nombre, " +
		                "i.id AS ins_id, i.nombre AS ins_nombre " +
		                "FROM reservas r " +
		                "JOIN usuarios u ON r.usuario = u.id " +
		                "JOIN instalaciones i ON r.instalacion = i.id";
		                
		       

		        try (Connection conn = ConexionUtil.getConexion();
		             PreparedStatement stmt = conn.prepareStatement(sql);
		             ResultSet rs = stmt.executeQuery()) {

		            while (rs.next()) {
		            	
		                Usuario usuario = new Usuario();
		                usuario.setId(rs.getInt("usu_id"));
		                usuario.setNombre(rs.getString("usu_nombre"));
		                
		                Instalacion instalacion = new Instalacion();
		                instalacion.setId(rs.getInt("ins_id"));
		                instalacion.setNombre(rs.getString("ins_nombre"));
		                
		                EstadoReserva estado = EstadoReserva.valueOf(rs.getString("estado").toUpperCase());
		            	
		            	Reserva r = new Reserva (
		            			rs.getInt("res_id"),
		            			usuario,
		            			instalacion,
		            			rs.getDate("fecha").toLocalDate(),
		            			rs.getTime("horaInicio").toLocalTime(),
		            			rs.getTime("horaFin").toLocalTime(),
		            			estado,
		            			rs.getDouble("monto")
		            					);
		            			
		            	
		                lista.add(r);
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		        }

		        return lista;
		    }

		   
		    // Update
		    public void modificarReserva(Reserva r) {
		        String sql = "UPDATE reservas SET fecha = ?, horaInicio = ?, horaFin = ? WHERE id = ?";

		        try (Connection conn = ConexionUtil.getConexion();
		             PreparedStatement stmt = conn.prepareStatement(sql)) {

		            stmt.setDate(1, Date.valueOf(r.getFecha()));;
		            stmt.setTime(2, Time.valueOf(r.getHoraInicio()));
		            stmt.setTime(3, Time.valueOf(r.getHoraFin()));;
		            stmt.setInt(4, r.getId());

		            stmt.executeUpdate();

		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    // Delete
		    public void eliminarReserva(int id) {
		        String sql = "DELETE FROM reservas WHERE id = ?";

		        try (Connection conn = ConexionUtil.getConexion();
		             PreparedStatement stmt = conn.prepareStatement(sql)) {

		            stmt.setInt(1, id);
		            stmt.executeUpdate();

		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
	}



