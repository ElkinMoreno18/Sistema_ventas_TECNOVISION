package Model.Order;


import Controller.Connect;
import Model.Customer.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;

public class Order {

    private Date dispatch_date;
    private Date delivery_date;
    private int id;
    private String paymentMethod;
    private int total_cost;
    private LinkedList<OrderDetail> order_detail_list;
    private Customer customer;
    private boolean active;

    public Order() {
        this.order_detail_list = new LinkedList<>();
        this.customer = new Customer();
    }

    public Order(Date dispatch_date, Date delivery_date, int id, String paymentMethod, int total_cost, boolean active) {
        this.dispatch_date = dispatch_date;
        this.delivery_date = delivery_date;
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.total_cost = total_cost;
        this.order_detail_list = new LinkedList<>();
        this.customer = new Customer();
        this.active = active;
    }

    public Date getDispatch_date() {
        return dispatch_date;
    }

    public void setDispatch_date(Date dispatch_date) {
        this.dispatch_date = dispatch_date;
    }


    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(int total_cost) {
        this.total_cost = total_cost;
    }

    public LinkedList<OrderDetail> getOrder_detail_list() {
        return order_detail_list;
    }

    public void setOrder_detail_list(LinkedList<OrderDetail> order_detail_list) {
        this.order_detail_list = order_detail_list;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int returnPaymentMethodId(String paymentMethod) throws Exception {
        int id = 0;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select id_forma_de_pago from formas_de_pago where nombre = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setString(1, paymentMethod);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            id = result.getInt("id_forma_de_pago");
        }
        return id;
    }

    public String payment(int id) throws Exception {
        String method = "";
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select nombre from formas_de_pago  where id_forma_de_pago = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, id);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            method = result.getString(1);
        }
        return method;
    }

    public double tempValue(LinkedList<OrderDetail> list) {
        double sum = 0;
        for (OrderDetail od : list) {
            sum += od.getTotalValue();
        }
        return sum;
    }

    public void addProductToList(OrderDetail order_detail) {
        this.order_detail_list.add(order_detail);
    }


    public int orderLastValue() throws Exception {
        int orderID = 0;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select last_Value from pedidos_numero_de_pedido_seq";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        ResultSet sequenceID = exe.executeQuery();
        if (sequenceID.next()) {
            orderID = sequenceID.getInt(1);
        }
        return orderID;
    }

    public String[] getBasicOrderInfo(long customerID) throws Exception {
        int orderID = 0;
        String[] info = new String[7];
        orderID = orderLastValue();
        info[0] = String.valueOf(orderID + 1);
        info[1] = String.valueOf(LocalDate.now());
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence2 = "select c.nombre,apellido,domicilio,teléfono,ct.nombre " +
                "from cliente cl, ciudad ct " +
                "where cl.idciudad = ct.idciudad " +
                "and cl.documento_cliente = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence2);
        exe.setInt(1, (int) customerID);
        ResultSet customerInfo = exe.executeQuery();
        if (customerInfo.next()) {
            info[2] = customerInfo.getString(1);
            info[3] = customerInfo.getString(2);
            info[4] = customerInfo.getString(3);
            info[5] = customerInfo.getString(4);
            info[6] = customerInfo.getString(5);
        }
        exe.close();
        return info;
    }

    public boolean searchOrder(int id) throws Exception {
        boolean flag = false;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select numero_de_pedido from pedidos where numero_de_pedido = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, id);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            flag = result.getInt("numero_de_pedido") != 0;
        }
        return flag;
    }

    public Order consultOrder(int id) throws Exception {
        Order order = new Order();
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select * from pedidos where numero_de_pedido = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, id);
        ResultSet result = exe.executeQuery();
        while (result.next()) {
            order.setId(result.getInt("numero_de_pedido"));
            order.getCustomer().setIdentificationDocument((result.getInt("documento_cliente")));
            order.setPaymentMethod(payment(result.getInt("id_forma_de_pago")));
            order.setDelivery_date(result.getDate("fecha_de_entrega"));
            order.setDispatch_date(result.getDate("fecha_de_expedicion"));
            order.setTotal_cost(result.getInt("valor_total"));
            order.setActive(result.getBoolean("activo"));
        }
        // adding order details //
        OrderDetail or = new OrderDetail();
        order.setOrder_detail_list(or.consultOrderDetail(order.getId()));
        return order;
    }

    public void saveOrder(Order order) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        connection.setAutoCommit(false); // start transaction //
        String SQL_sentence = "insert into pedidos(documento_cliente,id_forma_de_pago,fecha_de_entrega,fecha_de_expedicion,valor_total) " +
                "values(?,?,?,?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, (int) order.getCustomer().getIdentificationDocument());
        exe.setInt(2, returnPaymentMethodId(order.getPaymentMethod()));
        exe.setDate(3, order.getDelivery_date());
        exe.setDate(4, order.getDispatch_date());
        exe.setInt(5, (int) tempValue(order.getOrder_detail_list()));
        exe.executeUpdate();
        System.out.println("agregado");
        /// Adding order details //
        LinkedList<OrderDetail> list = order.getOrder_detail_list();
        for (OrderDetail orderDetail : list) {
            orderDetail.saveOrderDetail(orderDetail, connection, orderLastValue()); // calling method //
        }
        connection.commit();
        exe.close();
    }


    public void updateOrder(Order order, boolean active) throws Exception {
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "update pedidos set activo = ? where numero_de_pedido = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setBoolean(1, active);
        exe.setInt(2, order.getId());
        exe.executeUpdate();
        exe.close();
    }

}


