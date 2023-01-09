package io.weather.weather_by_ip;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public class WeatherAppRepository {

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mariadb";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void cacheGeoLocation(GeoLocation location) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO geolocations (latitude, longitude, city, country) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, location.getLatitude());
            stmt.setDouble(2, location.getLongitude());
            stmt.setString(3, location.getCity());
            stmt.setString(4, location.getCountry());
            stmt.executeUpdate();
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}


