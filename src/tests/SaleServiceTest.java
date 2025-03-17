package tests;

import services.SaleService;
import models.Sale;
import java.util.List;

public class SaleServiceTest {
    public static void main(String[] args) {
        // Example: Fetch all sales
        List<Sale> sales = SaleService.getAllSales();
        for (Sale sale : sales) {
            System.out.println(sale);
        }
    }
}
