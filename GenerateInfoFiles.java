import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {

    private static final String[] FIRST_NAMES = {"Carlos", "Ana", "Pedro", "Laura", "Juan", "Maria", "Sofia", "Andres"};
    private static final String[] LAST_NAMES = {"Gomez", "Rodriguez", "Martinez", "Perez", "Hernandez", "Diaz"};
    private static final String[] PRODUCT_NAMES = {"Phone", "Laptop", "Headphones", "Keyboard", "Mouse", "Printer", "Tablet", "Monitor"};
    private static final String[] DOC_TYPES = {"CC", "TI", "CE"};

    private static final Random random = new Random();

    public static void main(String[] args) {
        try {
            createSalesManInfoFile(5);
            createProductsFile(8);
            for (int i = 1; i <= 5; i++) {
                createSalesMenFile(5, "Vendor" + i, 1000 + i);
            }
            System.out.println("✅ Files generated successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error generating files: " + e.getMessage());
        }
    }

    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendors.txt"))) {
            for (int i = 0; i < salesmanCount; i++) {
                String docType = DOC_TYPES[random.nextInt(DOC_TYPES.length)];
                long docNumber = 10000000 + random.nextInt(90000000);
                String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
                String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                writer.write(docType + ";" + docNumber + ";" + firstName + ";" + lastName);
                writer.newLine();
            }
        }
    }

    public static void createProductsFile(int productsCount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            for (int i = 1; i <= productsCount; i++) {
                String name = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];
                int price = 5000 + random.nextInt(50000);
                writer.write("P" + i + ";" + name + ";" + price);
                writer.newLine();
            }
        }
    }

    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales_" + name + ".txt"))) {
            writer.write("CC;" + id);
            writer.newLine();
            for (int i = 0; i < randomSalesCount; i++) {
                String productId = "P" + (1 + random.nextInt(8));
                int quantity = 1 + random.nextInt(10);
                writer.write(productId + ";" + quantity);
                writer.newLine();
            }
        }
    }
}
