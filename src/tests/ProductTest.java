package tests;

import services.ProductService;

public class ProductTest {
    public static void main(String[] args) {
        // Add Product
        ProductService.addProduct("iPhone 13", "Mobile", 79999.0, 10);
        ProductService.addProduct("Samsung Charger", "Accessory", 1500.0, 25);

        // Display All Products
        System.out.println("All Products:");
        ProductService.getAllProducts().forEach(product -> 
            System.out.println(product.getId() + " - " + product.getName() + " - ₹" + product.getPrice())
        );

        // Update Product
        ProductService.updateProduct(1, "iPhone 13 Pro", "Mobile", 89999.0, 8);

        // Delete Product
        ProductService.deleteProduct(2);
    }
}
