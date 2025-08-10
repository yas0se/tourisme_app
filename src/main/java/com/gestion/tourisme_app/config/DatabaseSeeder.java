package com.gestion.tourisme_app.config;

import com.gestion.tourisme_app.entity.Category;
import com.gestion.tourisme_app.entity.Product;
import com.gestion.tourisme_app.repository.CategoryRepository;
import com.gestion.tourisme_app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            seedCategories();
            seedProducts();
        }
    }

    private void seedCategories() {
        List<Category> categories = Arrays.asList(
                createCategory("All", "All products"),
                createCategory("Crafts", "Traditional Moroccan crafts"),
                createCategory("Pottery", "Handmade ceramic items"),
                createCategory("Leather", "Genuine Moroccan leather products"),
                createCategory("Textiles", "Traditional fabrics and textiles"),
                createCategory("Jewelry", "Handcrafted jewelry"),
                createCategory("Spices", "Premium Moroccan spices"),
                createCategory("Home Decor", "Decorative items for home"),
                createCategory("Cosmetics", "Natural beauty products")
        );
        categoryRepository.saveAll(categories);
    }

    private void seedProducts() {
        Category crafts = categoryRepository.findById(2L).orElse(null);
        Category pottery = categoryRepository.findById(3L).orElse(null);
        Category leather = categoryRepository.findById(4L).orElse(null);
        Category textiles = categoryRepository.findById(5L).orElse(null);
        Category jewelry = categoryRepository.findById(6L).orElse(null);

        List<Product> products = Arrays.asList(
                createProduct("Argan Oil", "Pure Moroccan argan oil for skin, hair, and culinary uses. Known for its nourishing and anti-aging properties.",
                        29.99, 24.00, "https://terrebrune.ma/cdn/shop/products/1_df7ce0f3-a311-43d0-8b53-d0e8b95c499c.png?v=1640013302",
                        crafts, Arrays.asList(), Arrays.asList("100ml", "250ml")),

                createProduct("Zaafaran (Saffron)", "Premium hand-harvested saffron threads from the Atlas Mountains, prized for flavor and color.",
                        14.99, 12.00, "https://5.imimg.com/data5/SELLER/Default/2022/1/HD/FJ/HM/111935069/baby-saffron-main.jpg",
                        crafts, Arrays.asList(), Arrays.asList("1g", "5g")),

                createProduct("Hand-Painted Ceramic Plate", "Traditional Moroccan ceramic plate with intricate geometric patterns, perfect for decoration or serving.",
                        39.99, 29.00, "https://i.etsystatic.com/25844243/r/il/0ee5b3/4551523770/il_570xN.4551523770_eodb.jpg",
                        pottery, Arrays.asList("Blue", "Green", "Multi"), Arrays.asList("Medium", "Large")),

                createProduct("Leather Pouf", "Hand-stitched leather pouf ottoman, made from genuine Moroccan leather, ideal for home décor.",
                        89.99, 74.00, "https://mytindy.com/cdn/shop/products/CVuEpxCMc8.jpg?v=1629710322",
                        leather, Arrays.asList("Brown", "Tan", "White"), Arrays.asList("Standard")),

                createProduct("Berber Rug", "Handwoven Berber rug made from natural wool, featuring traditional Amazigh symbols and patterns.",
                        299.99, 249.00, "https://www.e-mosaik.com/cdn/shop/products/dsa_0449_2048x.jpg?v=1517560654",
                        textiles, Arrays.asList("White", "Black", "Red"), Arrays.asList("Small", "Medium", "Large")),

                createProduct("Silver Berber Necklace", "Handcrafted silver necklace with traditional Berber motifs, made by local artisans.",
                        79.99, 64.00, "https://www.moroccancorridor.com/cdn/shop/products/hamza-pendant-zaina-moroccan-jewelry-moroccan-corridorr-517.jpg?v=1623867915",
                        jewelry, Arrays.asList("Silver"), Arrays.asList()),

                createProduct("Handwoven Basket", "Colorful basket made from palm leaves, perfect for home storage or decoration.",
                        24.99, 19.00, "https://www.ikea.com/ma/en/images/products/hoekrubba-basket-with-lid-bamboo__1376758_pe960491_s5.jpg",
                        crafts, Arrays.asList("Natural", "Multi"), Arrays.asList("Small", "Medium")),

                createProduct("Tadelakt Candle Holder", "Polished stone-like candle holder made from traditional Tadelakt plaster, adding a rustic touch.",
                        34.99, 29.00, "https://cdn.myonlinestore.eu/9438a284-6be1-11e9-a722-44a8421b9960/image/cache/full/6f3bfc6d2d61155c70e820d99b88ae9241990835.jpg",
                        pottery, Arrays.asList("Gray", "Beige"), Arrays.asList())
        );

        productRepository.saveAll(products);
    }

    private Category createCategory(String title, String description) {
        Category category = new Category();
        category.setTitle(title);
        category.setDescription(description);
        category.setIsActive(true);
        return category;
    }

    private Product createProduct(String title, String description, double price, double discountedPrice,
                                  String imageUrl, Category category, List<String> colors, List<String> sizes) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setDiscountedPrice(discountedPrice);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setColors(colors);
        product.setSizes(sizes);
        product.setStockQuantity(100);
        product.setIsActive(true);
        product.setIsFeatured(Math.random() > 0.5); // Random featured status
        product.setRating(4.5);
        product.setReviewCount(25);
        return product;
    }
}
