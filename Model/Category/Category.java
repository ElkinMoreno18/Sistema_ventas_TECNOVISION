package Model.Category;

import Controller.Connect;
import Model.Discount.Discount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Category {

    private int id;
    private String name;
    private Discount discount;

    public Category() {
    }

    public void addDiscount() {
        this.discount = new Discount();
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
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

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public int searchCategory(int id) throws Exception {
        int id1 = 0;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select id_categoria from categorias where id_categoria = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, id);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            id1 = result.getInt("id_categoria");
        }
        exe.close();
        return id1;
    }

    public void saveCategory(Category category) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "insert into categorias (categoria,id_descuento) values (?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, category.getName());
        exe.setDouble(2, category.getDiscount().getId());
        exe.executeUpdate();
        exe.close();
    }

    public void updateCategory(Category category, String newName) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "update categorias set categoria = ?,id_descuento=? where categoria = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, newName);
        exe.setDouble(2, category.getDiscount().getValue());
        exe.setString(3, category.getName());
        exe.executeUpdate();
        exe.close();
    }
}
