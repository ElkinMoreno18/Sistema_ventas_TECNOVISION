package Model.Order;

import Controller.Connect;
import Model.Product.Product;
import Model.Service.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class OrderDetail {

    private int quantity;
    private double totalValue;
    private Product product;
    private Service service;

    public OrderDetail() {
        this.product = new Product();
    }

    public OrderDetail(int quantity, double totalValue) {
        this.quantity = quantity;
        this.totalValue = totalValue;
        this.product = new Product();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void addService() {
        this.service = new Service();
    }

    public LinkedList<OrderDetail> consultOrderDetail(int id) throws Exception {
        LinkedList<OrderDetail> list = new LinkedList<>();
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select * from detalle_pedido where pedidos_numero_de_pedido = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, id);
        ResultSet resultSet = exe.executeQuery();
        while (resultSet.next()) {
            OrderDetail order = new OrderDetail();
            order.addService();
            order.getProduct().setId(resultSet.getInt("productos_id_producto"));
            order.getService().setId(resultSet.getInt("id_servicio"));
            order.setQuantity(resultSet.getInt("cantidad"));
            order.setTotalValue(resultSet.getDouble("valor"));
            list.add(order);
        }
        exe.close();
        return list;
    }


    public void saveOrderDetail(OrderDetail orderDetail, Connection connection, int last_value) throws Exception {
        String SQL_sentence = "insert into detalle_pedido values(?,?,?,?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, orderDetail.getProduct().getId());
        exe.setInt(2, last_value);
        exe.setInt(3, orderDetail.getService().getId());
        exe.setInt(4, orderDetail.getQuantity());
        exe.setInt(5, (int) orderDetail.getTotalValue());
        exe.executeUpdate();
    }
}


