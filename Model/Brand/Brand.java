package Model.Brand;

import Controller.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Brand {

    private int id;
    private String brand;

    public Brand() {

    }

    public Brand(int id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int searchBrand(String brand) throws Exception {
        int id = 0;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select id_marca from Marcas where marca = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, brand);
        ResultSet result = exe.executeQuery();
        while (result.next()) {
            id = result.getInt("id_marca");
        }
        return id;
    }

    public void saveBrand(String brand) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "insert into marcas(marca) values(?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, brand);
        exe.executeUpdate();
        exe.close();
    }

    public void updateBrand(String brand, String newBrand) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "update Marcas set marca = ?" +
                " where marca = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, brand);
        exe.setString(2, newBrand);
        exe.executeQuery();
        exe.close();
    }

}
