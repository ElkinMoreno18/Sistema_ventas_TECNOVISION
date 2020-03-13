package Model.Service;

import Controller.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Service {

    private int id;
    private String name;
    private String description;

    public Service() {
    }

    public Service(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean searchService(int idService) throws Exception {
        boolean flag = false;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select nombre from servicios where id_servicio = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, idService);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            System.out.println(result.getString("nombre"));
            flag = !result.getString("nombre").equals(" ");
        }
        exe.close();
        return flag;
    }

    public Service consultService(int idService) throws Exception {
        Service service = new Service();
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select nombre,descripcion from servicios where id_servicio = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setLong(1, idService);
        ResultSet resultSet = exe.executeQuery();
        resultSet.next();
        service.setName(resultSet.getString("nombre"));
        service.setDescription(resultSet.getString("descripcion"));
        exe.close();
        return service;
    }

    public int returnNameService(String service) throws Exception {
        int idService = 0;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select id_servicio from servicios where nombre = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, service);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            idService = result.getInt("id_servicio");
        }
        exe.close();
        return idService;
    }

    public void saveService(Service service) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "insert into servicios (nombre,descripcion) values (?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, service.getName());
        exe.setString(2, service.getDescription());
        exe.executeUpdate();
        exe.close();
    }

    public void updateService(Service service) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "update servicios set nombre = ?,descripcion = ? where id_servicio = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, service.getName());
        exe.setString(2, service.getDescription());
        exe.executeUpdate();
        exe.close();
    }
}

