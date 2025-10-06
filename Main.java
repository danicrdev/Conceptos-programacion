import java.io.*;
import java.util.*;

public class Main {

    private static Map<String, Integer> productPrices = new HashMap<>();
    private static Map<String, String> productNames = new HashMap<>();
    private static Map<String, String> vendorNames = new HashMap<>();
    private static Map<String, Long> vendorTotals = new HashMap<>();
    private static Map<String, Integer> productQuantities = new HashMap<>();

    public static void main(String[] args) {
        try {
            loadProducts("products.txt");
            loadVendors("vendors.txt");

            // Process sales for each vendor
            for (String vendorId : vendorNames.keySet()) {
                File salesFile = new File("sales_" + vendorId + ".txt");
                if (salesFile.exists()) {
                    processSalesFile(salesFile, vendorId);
                }
            }

            generateSalesReport("sales_report.txt");
            generateProductsReport("products_report.txt");

            System.out.println("✅ Reports generated successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error processing files: " + e.getMessage());
        }
    }

    private static void loadProducts(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String id = parts[0];
                    String name = parts[1];
                    int price = Integer.parseInt(parts[2]);
                    productPrices.put(id, price);
                    productNames.put(id, name);
                    productQuantities.put(id, 0);
                }
            }
        }
    }

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

    private static void processSalesFile(File salesFile, String vendorId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(salesFile))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String productId = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    int price = productPrices.getOrDefault(productId, 0);

                    long subtotal = (long) price * quantity;
                    vendorTotals.put(vendorId, vendorTotals.get(vendorId) + subtotal);
                    productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);
                }
            }
        }
    }

    private static void generateSalesReport(String filename) throws IOException {
        List<Map.Entry<String, Long>> sortedVendors = new ArrayList<>(vendorTotals.entrySet());
        sortedVendors.sort((a, b) -> Long.compare(b.getValue(), a.getValue())); // descending

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Long> entry : sortedVendors) {
                String name = vendorNames.get(entry.getKey());
                writer.write(name + ";" + entry.getValue());
                writer.newLine();
            }
        }
    }

    private static void generateProductsReport(String filename) throws IOException {
        List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productQuantities.entrySet());
        sortedProducts.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // descending

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Integer> entry : sortedProducts) {
                String id = entry.getKey();
                String name = productNames.get(id);
                int price = productPrices.get(id);
                writer.write(name + ";" + entry.getValue() + ";" + price);
                writer.newLine();
            }
        }
    }
}
