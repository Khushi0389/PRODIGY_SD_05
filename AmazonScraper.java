import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AmazonScraper {

    public static void main(String[] args) {
        String url = "https://www.amazon.com/s?k=laptop"; // Example URL, replace with desired Amazon search URL
        List<Product> products = scrapeAmazonProducts(url);

        // Save the scraped data to CSV file
        String csvFile = "amazon_products.csv";
        saveToCsv(products, csvFile);

        System.out.println("Scraping complete. Data saved to " + csvFile);
    }

    public static List<Product> scrapeAmazonProducts(String url) {
        List<Product> products = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select("div[data-component-type=s-search-result]");

            for (Element item : items) {
                String name = item.select("span.a-size-medium").text().trim();
                String price = item.select("span.a-offscreen").text().trim();
                String rating = item.select("span.a-icon-alt").text().split(" ")[0];

                products.add(new Product(name, price, rating));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static void saveToCsv(List<Product> products, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("Name,Price,Rating\n");
            for (Product product : products) {
                writer.append(product.getName()).append(",")
                        .append(product.getPrice()).append(",")
                        .append(product.getRating()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Product {
        private String name;
        private String price;
        private String rating;

        public Product(String name, String price, String rating) {
            this.name = name;
            this.price = price;
            this.rating = rating;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getRating() {
            return rating;
        }
    }
}
