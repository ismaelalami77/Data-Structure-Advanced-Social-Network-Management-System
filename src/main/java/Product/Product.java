package Product;

public class Product {
    private static int counter = 1;
    private String productId;
    private String name;
    private String category;
    private String status;

    public Product(String name, String category, String status) {
        this.productId = generateProductId();
        this.name = name;
        this.category = category;
        this.status = status;
    }

    private String generateProductId() {
        return String.format("P%03d", counter++);
    }

    public static String getCurrentProductId() {
        return String.format("P%03d", counter);
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
