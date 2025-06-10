package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/reservas_club";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "root";

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de MySQL.");
            e.printStackTrace();
        }

        return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
    }
}