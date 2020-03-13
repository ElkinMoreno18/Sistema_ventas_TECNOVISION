package Model.Provider;

import Controller.Connect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Provider {

    private int nit;
    private String name;
    private BigDecimal telephone;
    private String email;
    private String city;

    public Provider() {
    }

    public Provider(int nit, String name, BigDecimal telephone, String email, String city) {
        this.nit = nit;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.city = city;
    }

    public int getNit() {
        return nit;
    }

    public void setNit(int nit) {
        this.nit = nit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTelephone() {
        return telephone;
    }

    public void setTelephone(BigDecimal telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean searchProvider(int nit) throws Exception {
        boolean flag = false;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select nit from proveedores where nit = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setLong(1, nit);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            flag = result.getInt("nit") != 0;
        }
        exe.close();
        return flag;
    }

    public Provider consultProvider(int nit) throws Exception {
        Provider provider = new Provider();
        if (searchProvider(nit)) {
            Connect c = new Connect();
            Connection connection = c.createConnection();
            String SQL_sentence = "select * from proveedores where nit = ?";
            PreparedStatement exe = connection.prepareStatement(SQL_sentence);
            exe.setInt(1, nit);
            ResultSet result = exe.executeQuery();
            if (result.next()) {
                provider.setNit(result.getInt("nit"));
                provider.setName(result.getString("nombre"));
                provider.setTelephone(result.getBigDecimal("telefono"));
                provider.setEmail(result.getString("correo_electronico"));
                provider.setCity(c.returnCityName(result.getInt("id_ciudad")));
            }
            exe.close();
        }
        return provider;
    }

    public void saveProvider(Provider provider) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "insert into proveedor values(?,?,?,?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, provider.getNit());
        exe.setString(2, provider.getName());
        exe.setBigDecimal(3, provider.getTelephone());
        exe.setString(4, provider.getEmail());
        exe.setInt(5, c.returnCityID(provider.getCity().toUpperCase()));
        exe.executeUpdate();
        exe.close();
    }

    public void updateProvider(Provider provider, int newNit) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "update proveedor set nit = ?, nombre = ?, telefono = ?, " +
                "correo_electronico = ?, id_ciudad = ? where nit = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, newNit);
        exe.setString(2, provider.getName());
        exe.setBigDecimal(3, provider.getTelephone());
        exe.setString(4, provider.getEmail());
        exe.setInt(5, c.returnCityID(provider.getCity().toUpperCase()));
        exe.setInt(6, provider.getNit());
        exe.executeUpdate();
        exe.close();
    }
}

