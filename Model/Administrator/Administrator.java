package Model.Administrator;


import Controller.Connect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Administrator {

    private String city;
    private String password;
    private String email;
    private long identificationDocument;
    private String domicile;
    private String name;
    private BigDecimal telephone;

    // Metodos Constructor
    public Administrator() {
    }

    public Administrator(String city, String password, String email, long identificationDocument, String domicile, String name, BigDecimal telephone) {
        this.city = city;
        this.password = password;
        this.email = email;
        this.identificationDocument = identificationDocument;
        this.domicile = domicile;
        this.name = name;
        this.telephone = telephone;
    }

    // Getters and Setters //'
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(long identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
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

// Inicio Metodos //

    public boolean searchAdministrator(long identificationDocument) throws Exception {
        boolean flag = false;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select nombre from administradores where documento_administrador = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setLong(1, identificationDocument);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            flag = !result.getString("nombre").equals(" ");
        }
        exe.close();
        return flag;
    }

    public Administrator consultAdministrator(long identificationDocument) throws Exception {
        Administrator admin = new Administrator();
        if (searchAdministrator(identificationDocument)) {
            Connect c = new Connect();
            Connection connection = c.createConnection();
            String SQL_sentence = "select * from administradores where documento_administrador = ?";
            PreparedStatement exe = connection.prepareStatement(SQL_sentence);
            exe.setLong(1, identificationDocument);
            ResultSet resultSet = exe.executeQuery();
            while (resultSet.next()) {
                admin.setIdentificationDocument(resultSet.getLong("documento_administrador"));
                admin.setName(resultSet.getString("nombre"));
                admin.setPassword(resultSet.getString("contrasenia"));
                admin.setDomicile(resultSet.getString("domicilio"));
                admin.setTelephone(resultSet.getBigDecimal("telefono"));
                admin.setTelephone(resultSet.getBigDecimal("correo_electronico"));
                admin.setCity(c.returnCityName(resultSet.getInt("id_ciudad")));
            }
            exe.close();
        }
        return admin;
    }

    public void saveAdministrator(Administrator admin) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "insert into administradores values (?,?,?,?,?,?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, (int) admin.getIdentificationDocument());
        exe.setString(2, admin.getName());
        exe.setString(3, admin.getPassword());
        exe.setString(4, admin.getDomicile());
        exe.setBigDecimal(5, admin.getTelephone());
        exe.setString(6, admin.getEmail());
        exe.setInt(7, c.returnCityID(admin.getCity()));
        exe.executeUpdate();
    }

    public void updateAdministrator(Administrator admin, long identificationDocument) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "update administradores set documento_administrador=?, nombre = ?, constrasenia = ?," +
                " domicilio= ?,telefono= ?,correo_electronico= ?,id_ciudad= ? where documento_administrador = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, (int) admin.getIdentificationDocument());
        exe.setString(2, admin.getName());
        exe.setString(3, admin.getPassword());
        exe.setString(4, admin.getDomicile());
        exe.setInt(5, c.returnCityID(admin.getCity()));
        exe.setBigDecimal(6, admin.getTelephone());
        exe.setString(7, admin.getEmail());
        exe.setInt(8, (int) identificationDocument);
        exe.executeUpdate();
        exe.close();
    }
}
