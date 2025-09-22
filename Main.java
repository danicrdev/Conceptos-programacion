import java.io.*;
import java.util.*;

public class Main {

    // Maps for products and vendors
    private static Map<String, Integer> productPrices = new HashMap<>();
    private static Map<String, String> vendorNames = new HashMap<>();
    private static Map<String, Long> vendorTotals = new HashMap<>();

    public static void main(String[] args) {
        try {
            // Load products and vendors
            loadProducts("products.txt");
            loadVendors("vendors.txt");

            // Process each vendor's sales file
            for (String vendorId : vendorNames.keySet()) {
                String filename = "sales_" + vendorId + ".txt";
                File salesFile = new File(filename);
                if (salesFile.exists()) {
                    processSalesFile(salesFile, vendorId);
                }
            }

            // Generate preliminary report
            generateSalesReport("sales_report.txt");

            System.out.println("✅ Preliminary report generated successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Loads product information into a map (ProductID -> Price).
     */
    private static void loadProducts(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String productId = parts[0];
                    int price = Integer.parseInt(parts[2]);
                    productPrices.put(productId, price);
                }
            }
        }
    }

    /**
     * Loads vendor information into a map (VendorID -> FullName).
     */
    private static void loadVendors(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int counter = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String vendorId = "Vendor" + counter++;
                    String fullName = parts[2] + " " + parts[3];
                    vendorNames.put(vendorId, fullName);
                    vendorTotals.put(vendorId, 0L);
                }
            }
        }
    }

    /**
     * Processes a sales file and updates the vendor's total sales.
     */
    private static void processSalesFile(File salesFile, String vendorId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(salesFile))) {
            String header = reader.readLine(); // Skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String productId = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    int price = productPrices.getOrDefault(productId, 0);
                    long subtotal = (long) price * quantity;
                    vendorTotals.put(vendorId, vendorTotals.get(vendorId) + subtotal);
                }
            }
        }
    }

    /**
     * Generates a preliminary sales report file.
     */
    private static void generateSalesReport(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String vendorId : vendorNames.keySet()) {
                String fullName = vendorNames.get(vendorId);
                long total = vendorTotals.get(vendorId);
                writer.write(fullName + ";" + total);
                writer.newLine();
            }
        }
    }
}
