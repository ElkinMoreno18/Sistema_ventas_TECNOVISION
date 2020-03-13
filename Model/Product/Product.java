package Model.Product;

// nuevo comentario//

import Controller.Connect;
import Model.Administrator.Administrator;
import Model.Brand.Brand;
import Model.Category.Category;
import Model.Provider.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Product {


    private String description;
    private int id;
    private String name;
    private String imagePath;
    private int stock;
    private int unitValue;
    private Administrator administrator;
    private Category category;
    private Brand brand;
    private Provider provider;

    public Product() {
        this.administrator = new Administrator();
        this.category = new Category();
        this.brand = new Brand();
        this.provider = new Provider();
    }

    public Product(String description, int id, String name, String imagePath, int stock, int unitValue) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.stock = stock;
        this.unitValue = unitValue;
        this.administrator = new Administrator();
        this.category = new Category();
        this.brand = new Brand();
        this.provider = new Provider();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(int unitValue) {
        this.unitValue = unitValue;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public boolean searchProduct(int id) throws Exception {
        boolean flag = false;
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "select id_producto from Productos where id_producto = ?";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, id);
        ResultSet result = exe.executeQuery();
        if (result.next()) {
            flag = result.getInt("id_producto") != 0;
        }
        exe.close();
        return flag;
    }

    public Product consultProduct(int id) throws Exception {
        Product product = new Product();
        if (searchProduct(id)) {
            Connect c = new Connect();
            Connection connection = c.createConnection();
            String SQL_sentence = "select * from Productos where id_producto = ?";
            PreparedStatement exe = connection.prepareStatement(SQL_sentence);
            exe.setInt(1, id);
            ResultSet result = exe.executeQuery();
            if (result.next()) {
                product.setId(result.getInt("id_producto"));
                product.setName(result.getString("nombre"));
                product.setImagePath(result.getString("ruta_imagen"));
                product.setStock(result.getInt("stock"));
                product.setDescription(result.getString("descripcion"));
                product.setUnitValue(result.getInt("valor"));
                product.getBrand().setId(result.getInt("id_marca"));
                product.getProvider().setNit((result.getInt("nit_proveedor")));
                product.getCategory().setId(result.getInt("id_categoria"));
                product.getAdministrator().setIdentificationDocument((result.getInt("documento_administrador")));
            }
            exe.close();
        }
        return product;
    }

    public void saveProduct(Product product) throws Exception {
        int brandId = product.getBrand().searchBrand(product.getBrand().getBrand());
        int nit = product.getProvider().getNit();
        int categoryId = product.getCategory().searchCategory(product.getCategory().getId());
        long adminID = product.getAdministrator().getIdentificationDocument();
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String SQL_sentence = "insert into Productos" + " values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement exe = connection.prepareStatement(SQL_sentence);
        exe.setInt(1, product.getId());
        exe.setString(2, product.getName());
        exe.setString(3, product.getImagePath());
        exe.setInt(4, product.getStock());
        exe.setString(5, product.getDescription());
        exe.setInt(6, product.getUnitValue());
        exe.setInt(7, brandId);
        exe.setInt(8, nit);
        exe.setInt(9, categoryId);
        exe.setInt(10, (int) adminID);
        exe.executeUpdate();
        exe.close();
    }

    public void updateProduct(Product product, int newID) throws Exception {
        int brandID = product.getBrand().searchBrand(product.getBrand().getBrand());
        Connect c = new Connect();
        Connection connection = c.createConnection();
        String sentence = "update Productos set id_producto = ?, nombre = ?, ruta_imagen = ?, " +
                "stock = ?, descripcion = ?, valor = ?, id_marca = ?, nit_proveedor = ?, " +
                "id_categoria = ?, documento_administrador = ? where id_producto = ?";
        PreparedStatement exe = connection.prepareStatement(sentence);
        exe.setInt(1, newID);
        exe.setString(2, product.getName());
        exe.setString(3, product.getImagePath());
        exe.setInt(4, product.getStock());
        exe.setString(5, product.getDescription());
        exe.setInt(6, product.getUnitValue());
        exe.setInt(9, brandID);
        exe.setInt(10, product.getProvider().getNit());
        exe.setInt(7, product.getCategory().getId());
        exe.setInt(8, (int) product.getAdministrator().getIdentificationDocument());
        exe.setInt(11, product.getId());
        exe.executeUpdate();
        exe.close();
    }
}
