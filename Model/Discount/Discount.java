package Model.Discount;

import Controller.Connect;
import Model.Category.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Discount {

    private int id;
    private double value;

    public Discount() {
    }

    public Discount(int id, double value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double consultDiscount(int idCategory) throws Exception {
        Category category = new Category();
        double discount = 0;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select descuento from descuentos  where id_categoria = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setLong(1, category.searchCategory(idCategory));
        ResultSet resultSet = exe.executeQuery();
        resultSet.next();
        discount = resultSet.getDouble(1);
        exe.close();
        return discount;
    }

    public void saveDiscount(Discount discount, int categoryId) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "insert into descuento (descuento,id_categoria) values (?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setDouble(1, discount.getValue());
        exe.setInt(2, categoryId);
        exe.executeUpdate();
        exe.close();
    }

    public void updateDiscount(Discount discount, int categoryId) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "update descuento set descuento = ?, id_categoria = ? where id_descuento = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setDouble(1, discount.getValue());
        exe.setDouble(2, categoryId);
        exe.executeUpdate();
        exe.close();
    }
}
