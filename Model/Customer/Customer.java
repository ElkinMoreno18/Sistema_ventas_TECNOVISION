package Model.Customer;

import Controller.Connect;
import Model.Administrator.Administrator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Customer {

    private long identificationDocument;
    private String name;
    private String lastName;
    private String password;
    private String city;
    private String email;
    private String domicile;
    private BigDecimal telephone;
    private Administrator administrator;

    public Customer() {
    }

    public Customer(long identificationDocument, String name, String lastName, String password, String city, String email, String domicile, BigDecimal telephone) {
        this.identificationDocument = identificationDocument;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.city = city;
        this.email = email;
        this.domicile = domicile;
        this.telephone = telephone;
    }

    public void addAdministrator(long identificationDocument) {
        this.administrator = new Administrator();
        this.administrator.setIdentificationDocument(identificationDocument);
    }

    public long getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(long identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public BigDecimal getTelephone() {
        return telephone;
    }

    public void setTelephone(BigDecimal telephone) {
        this.telephone = telephone;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public boolean searchCustomer(long identification_document) throws Exception {
        boolean flag = false;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select nombre from cliente where documento_cliente = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setLong(1, identification_document);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            System.out.println(result.getString("nombre"));
            flag = !result.getString("nombre").equals(" ");
        }
        exe.close();
        return flag;
    }

    public Customer consultCustomer(long identification_document) throws Exception {
        Customer customer = new Customer();
        if (searchCustomer(identification_document)) {
            Connect c = new Connect();
            Connection connection = c.createConnection();
            String SQL_sentence = "select * from clientes where documento_cliente = ?";
            PreparedStatement exe = connection.prepareStatement(SQL_sentence);
            exe.setLong(1, identification_document);
            ResultSet resultSet = exe.executeQuery();
            while (resultSet.next()) {
                customer.setIdentificationDocument(resultSet.getInt("documento_cliente"));
                customer.setName(resultSet.getString("nombre"));
                customer.setLastName(resultSet.getString("apellido"));
                customer.setDomicile(resultSet.getString("domicilio"));
                customer.setTelephone(resultSet.getBigDecimal("telefono"));
                customer.setEmail(resultSet.getString("correo_electronico"));
                customer.setPassword(resultSet.getString("contraseña"));
                customer.setCity(c.returnCityName(resultSet.getInt("id_ciudad")));
                customer.getAdministrator().setIdentificationDocument(resultSet.getInt("documento_administrador"));
            }
            exe.close();
        }
        return customer;
    }

    public void saveCustomer(Customer customer) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "insert into cliente values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, (int) customer.getIdentificationDocument());
        exe.setString(2, customer.getName());
        exe.setString(3, customer.getLastName());
        exe.setString(4, customer.getDomicile());
        exe.setBigDecimal(5, customer.getTelephone());
        exe.setString(6, customer.getEmail());
        exe.setString(7, customer.getPassword());
        exe.setInt(8, c.returnCityID(customer.getCity()));
        exe.setLong(9, customer.getAdministrator().getIdentificationDocument());
        exe.executeUpdate();
        exe.close();
    }

    public void updateCustomer(Customer customer, int document) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "update cliente set documento_cliente = ?,nombre = ?,apellido = ?, domicilio = ?, " +
                "telefono = ?,correo_electronico = ?,contrasenia = ?, id_ciudad = ?, documento_administrador = ? where documento_cliente = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, document); // new ID //
        exe.setString(2, customer.getName());
        exe.setString(3, customer.getLastName());
        exe.setString(4, customer.getDomicile());
        exe.setBigDecimal(5, customer.getTelephone());
        exe.setString(6, customer.getEmail());
        exe.setString(7, customer.getPassword());
        exe.setInt(8, c.returnCityID(customer.getCity()));
        exe.setInt(9, (int) customer.getAdministrator().getIdentificationDocument());
        exe.setInt(10, (int) customer.getIdentificationDocument()); // old id//
        exe.executeUpdate();
        exe.close();
    }
}

