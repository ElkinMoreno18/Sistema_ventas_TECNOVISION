package Controller;

import java.sql.*;

public class Connect {

    public Connect() {
    }

    public Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/sistema_de_ventas_tecnovision";
        return DriverManager.getConnection(url, "postgres", "axel0421");
    }

    public int returnCityID(String city) throws Exception {
        int cityID = 0;
        Connection connection = createConnection();
        String SQL_sentence = "select id_ciudad from ciudades where nombre = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, city);
        ResultSet result = exe.executeQuery();
        while (result.next()) {
            cityID = result.getInt("id_ciudad");
        }
        return cityID;
    }

    public String returnCityName(int id) throws Exception {
        String city = "";
        Connection connection = createConnection();
        String SQL_sentence = "select nombre from ciudades where id_ciudad = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, id);
        ResultSet result = exe.executeQuery();
        while (result.next()) {
            city = result.getString("nombre");
        }
        return city;
    }

}
