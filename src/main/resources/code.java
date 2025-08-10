// pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    <groupId>com.banani</groupId>
    <artifactId>tourism-backend</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>tourism-backend</name>
<description>Backend API for Morocco Tourism E-commerce App</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.11.5</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.11.5</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>

// src/main/resources/application.yml
server:
port: 8080
servlet:
context-path: /api/v1

spring:
application:
name: banani-tourism-backend

datasource:
url: jdbc:mysql://localhost:3306/banani_tourism?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
username: ${DB_USERNAME:root}
password: ${DB_PASSWORD:password}
driver-class-name: com.mysql.cj.jdbc.Driver

jpa:
hibernate:
ddl-auto: update
show-sql: false
properties:
hibernate:
dialect: org.hibernate.dialect.MySQL8Dialect
format_sql: true

mail:
host: smtp.gmail.com
port: 587
username: ${EMAIL_USERNAME:your-email@gmail.com}
password: ${EMAIL_PASSWORD:your-app-password}
properties:
mail:
smtp:
auth: true
starttls:
enable: true

cache:
type: simple

jwt:
secret: ${JWT_SECRET:bW9yb2Njb1RvdXJpc21BcHBTZWNyZXRLZXlGb3JKV1RUb2tlblNpZ25pbmc=}
expiration: 86400000 # 24 hours

app:
cors:
allowed-origins: "http://localhost:3000,https://yourdomain.com"
upload:
path: ${UPLOAD_PATH:./uploads}

// src/main/java/com/banani/tourism/TourismApplication.java
package com.banani.tourism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TourismApplication {
    public static void main(String[] args) {
        SpringApplication.run(TourismApplication.class, args);
    }
}

// src/main/java/com/banani/tourism/entity/Category.java
package com.banani.tourism.entity;

import jakarta.persistence.*;
        import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    @Column(nullable = false)
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}

// src/main/java/com/banani/tourism/entity/Product.java
package com.banani.tourism.entity;

import jakarta.persistence.*;
        import jakarta.validation.constraints.*;
        import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Product description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Discounted price must be 0 or greater")
    @Column(name = "discounted_price", precision = 10, scale = 2)
    private BigDecimal discountedPrice = BigDecimal.ZERO;

    @Column(name = "image_url")
    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "color")
    private List<String> colors;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    private List<String> sizes;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private BigDecimal rating = BigDecimal.ZERO;

    @Min(value = 0, message = "Review count cannot be negative")
    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}

// src/main/java/com/banani/tourism/entity/User.java
package com.banani.tourism.entity;

import jakarta.persistence.*;
        import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    private String password;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expires")
    private LocalDateTime passwordResetExpires;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    public enum Role {
        USER, ADMIN, MODERATOR
    }
}

// src/main/java/com/banani/tourism/entity/CartItem.java
package com.banani.tourism.entity;

import jakarta.persistence.*;
        import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "selected_color")
    private String selectedColor;

    @Column(name = "selected_size")
    private String selectedSize;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}

// src/main/java/com/banani/tourism/entity/Order.java
package com.banani.tourism.entity;

import jakarta.persistence.*;
        import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotBlank(message = "Shipping address is required")
    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required")
    @Column(name = "payment_method")
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
}

// src/main/java/com/banani/tourism/entity/OrderItem.java
package com.banani.tourism.entity;

import jakarta.persistence.*;
        import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "selected_color")
    private String selectedColor;

    @Column(name = "selected_size")
    private String selectedSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

// src/main/java/com/banani/tourism/dto/CategoryDto.java
package com.banani.tourism.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

// src/main/java/com/banani/tourism/dto/ProductDto.java
package com.banani.tourism.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private String imageUrl;
    private List<String> colors;
    private List<String> sizes;
    private Integer stockQuantity;
    private Boolean isActive;
    private Boolean isFeatured;
    private BigDecimal rating;
    private Integer reviewCount;
    private LocalDateTime createdDate;
    private CategoryDto category;
    private boolean isFavorite;
}

// src/main/java/com/banani/tourism/dto/CartItemDto.java
package com.banani.tourism.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private String selectedColor;
    private String selectedSize;
    private LocalDateTime createdDate;
    private ProductDto product;
}

// src/main/java/com/banani/tourism/dto/CreateOrderDto.java
package com.banani.tourism.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class CreateOrderDto {
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String notes;
    private List<Long> cartItemIds;
}

// src/main/java/com/banani/tourism/dto/OrderDto.java
package com.banani.tourism.dto;

import com.banani.tourism.entity.Order;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private Order.OrderStatus orderStatus;
    private Order.PaymentStatus paymentStatus;
    private String notes;
    private LocalDateTime createdDate;
    private List<OrderItemDto> orderItems;
}

// src/main/java/com/banani/tourism/dto/OrderItemDto.java
package com.banani.tourism.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private Integer quantity;
    private BigDecimal price;
    private String selectedColor;
    private String selectedSize;
    private ProductDto product;
}

// src/main/java/com/banani/tourism/repository/CategoryRepository.java
package com.banani.tourism.repository;

import com.banani.tourism.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActiveTrueOrderByTitleAsc();

    @Query("SELECT c FROM Category c WHERE c.isActive = true AND c.title LIKE %:title%")
    List<Category> findByTitleContainingAndIsActive(String title);
}

// src/main/java/com/banani/tourism/repository/ProductRepository.java
package com.banani.tourism.repository;

import com.banani.tourism.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByIsActiveTrueOrderByCreatedDateDesc(Pageable pageable);

    Page<Product> findByCategoryIdAndIsActiveTrueOrderByCreatedDateDesc(Long categoryId, Pageable pageable);

    List<Product> findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedDateDesc();

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);
}

// src/main/java/com/banani/tourism/repository/UserRepository.java
package com.banani.tourism.repository;

import com.banani.tourism.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailVerificationToken(String token);
    Optional<User> findByPasswordResetToken(String token);
    boolean existsByEmail(String email);
}

// src/main/java/com/banani/tourism/repository/CartItemRepository.java
package com.banani.tourism.repository;

import com.banani.tourism.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserIdOrderByCreatedDateDesc(Long userId);

    Optional<CartItem> findByUserIdAndProductIdAndSelectedColorAndSelectedSize(
            Long userId, Long productId, String selectedColor, String selectedSize);

    void deleteByUserId(Long userId);
}

// src/main/java/com/banani/tourism/repository/OrderRepository.java
package com.banani.tourism.repository;

import com.banani.tourism.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserIdOrderByCreatedDateDesc(Long userId, Pageable pageable);
    Order findByOrderNumber(String orderNumber);
}

// src/main/java/com/banani/tourism/service/CategoryService.java
package com.banani.tourism.service;

import com.banani.tourism.dto.CategoryDto;
import com.banani.tourism.entity.Category;
import com.banani.tourism.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Cacheable("categories")
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findByIsActiveTrueOrderByTitleAsc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return convertToDto(category);
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        dto.setDescription(category.getDescription());
        dto.setImageUrl(category.getImageUrl());
        dto.setIsActive(category.getIsActive());
        dto.setCreatedDate(category.getCreatedDate());
        dto.setUpdatedDate(category.getUpdatedDate());
        return dto;
    }
}

// src/main/java/com/banani/tourism/service/ProductService.java
package com.banani.tourism.service;

import com.banani.tourism.dto.ProductDto;
import com.banani.tourism.dto.CategoryDto;
import com.banani.tourism.entity.Product;
import com.banani.tourism.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrueOrderByCreatedDateDesc(pageable)
                .map(this::convertToDto);
    }

    public Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndIsActiveTrueOrderByCreatedDateDesc(categoryId, pageable)
                .map(this::convertToDto);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDto(product);
    }

    public List<ProductDto> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedDateDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<ProductDto> searchProducts(String query, Pageable pageable) {
        return productRepository.searchProducts(query, pageable)
                .map(this::convertToDto);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setColors(product.getColors());
        dto.setSizes(product.getSizes());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setRating(product.getRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setCreatedDate(product.getCreatedDate());
        dto.setFavorite(false); // This would be set based on user preferences

        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setTitle(product.getCategory().getTitle());
            dto.setCategory(categoryDto);
        }

        return dto;
    }
}

// src/main/java/com/banani/tourism/controller/CategoryController.java
package com.banani.tourism.controller;

import com.banani.tourism.dto.CategoryDto;
import com.banani.tourism.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
}

// src/main/java/com/banani/tourism/controller/ProductController.java
package com.banani.tourism.controller;

import com.banani.tourism.dto.ProductDto;
import com.banani.tourism.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDto>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductDto>> getFeaturedProducts() {
        List<ProductDto> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.searchProducts(query, pageable);
        return ResponseEntity.ok(products);
    }
}

// src/main/java/com/banani/tourism/service/CartService.java
package com.banani.tourism.service;

import com.banani.tourism.dto.CartItemDto;
import com.banani.tourism.dto.ProductDto;
import com.banani.tourism.dto.CategoryDto;
import com.banani.tourism.entity.CartItem;
import com.banani.tourism.entity.Product;
import com.banani.tourism.entity.User;
import com.banani.tourism.repository.CartItemRepository;
import com.banani.tourism.repository.ProductRepository;
import com.banani.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<CartItemDto> getCartItems(Long userId) {
        return cartItemRepository.findByUserIdOrderByCreatedDateDesc(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addToCart(Long userId, Long productId, int quantity, String selectedColor, String selectedSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository
                .findByUserIdAndProductIdAndSelectedColorAndSelectedSize(userId, productId, selectedColor, selectedSize);

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Add new item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSelectedColor(selectedColor);
            cartItem.setSelectedSize(selectedSize);
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void removeFromCart(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void updateCartItemQuantity(Long itemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setSelectedColor(cartItem.getSelectedColor());
        dto.setSelectedSize(cartItem.getSelectedSize());
        dto.setCreatedDate(cartItem.getCreatedDate());

        // Convert product
        Product product = cartItem.getProduct();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setDiscountedPrice(product.getDiscountedPrice());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setColors(product.getColors());
        productDto.setSizes(product.getSizes());

        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setTitle(product.getCategory().getTitle());
            productDto.setCategory(categoryDto);
        }

        dto.setProduct(productDto);
        return dto;
    }
}

// src/main/java/com/banani/tourism/controller/CartController.java
package com.banani.tourism.controller;

import com.banani.tourism.dto.CartItemDto;
import com.banani.tourism.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        // For now, using a mock user ID. In real implementation, extract from JWT token
        Long userId = 1L; // This should be extracted from authenticated user
        List<CartItemDto> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        // For now, using a mock user ID
        Long userId = 1L;
        Long productId = Long.valueOf(request.get("productId").toString());
        int quantity = (Integer) request.get("quantity");
        String selectedColor = (String) request.get("selectedColor");
        String selectedSize = (String) request.get("selectedSize");

        cartService.addToCart(userId, productId, quantity, selectedColor, selectedSize);
        return ResponseEntity.ok().body(Map.of("message", "Item added to cart successfully"));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long itemId) {
        cartService.removeFromCart(itemId);
        return ResponseEntity.ok().body(Map.of("message", "Item removed from cart"));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> request) {
        int quantity = (Integer) request.get("quantity");
        cartService.updateCartItemQuantity(itemId, quantity);
        return ResponseEntity.ok().body(Map.of("message", "Cart item updated"));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = 1L; // Mock user ID
        cartService.clearCart(userId);
        return ResponseEntity.ok().body(Map.of("message", "Cart cleared"));
    }
}
/// /////////////////////////////////////////////////////////////////////////
// src/main/java/com/banani/tourism/service/OrderService.java
package com.banani.tourism.service;

import com.banani.tourism.dto.*;
        import com.banani.tourism.entity.*;
        import com.banani.tourism.repository.*;
        import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public String createOrder(Long userId, CreateOrderDto createOrderDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserIdOrderByCreatedDateDesc(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setShippingAddress(createOrderDto.getShippingAddress());
        order.setPaymentMethod(createOrderDto.getPaymentMethod());
        order.setNotes(createOrderDto.getNotes());
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);

        // Calculate total amount and create order items
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSelectedColor(cartItem.getSelectedColor());
            orderItem.setSelectedSize(cartItem.getSelectedSize());

            // Use discounted price if available, otherwise regular price
            BigDecimal itemPrice = cartItem.getProduct().getDiscountedPrice().compareTo(BigDecimal.ZERO) > 0
                    ? cartItem.getProduct().getDiscountedPrice()
                    : cartItem.getProduct().getPrice();

            orderItem.setPrice(itemPrice);
            totalAmount = totalAmount.add(itemPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order
        cartItemRepository.deleteByUserId(userId);

        return savedOrder.getOrderNumber();
    }

    public Page<OrderDto> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByCreatedDateDesc(userId, pageable)
                .map(this::convertToDto);
    }

    public OrderDto getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        return convertToDto(order);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ORD-" + timestamp + "-" + (int)(Math.random() * 1000);
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setNotes(order.getNotes());
        dto.setCreatedDate(order.getCreatedDate());

        if (order.getOrderItems() != null) {
            List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                    .map(this::convertOrderItemToDto)
                    .collect(Collectors.toList());
            dto.setOrderItems(orderItemDtos);
        }

        return dto;
    }

    private OrderItemDto convertOrderItemToDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setSelectedColor(orderItem.getSelectedColor());
        dto.setSelectedSize(orderItem.getSelectedSize());

        // Convert product
        Product product = orderItem.getProduct();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setDiscountedPrice(product.getDiscountedPrice());
        productDto.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setTitle(product.getCategory().getTitle());
            productDto.setCategory(categoryDto);
        }

        dto.setProduct(productDto);
        return dto;
    }
}

// src/main/java/com/banani/tourism/controller/OrderController.java
package com.banani.tourism.controller;

import com.banani.tourism.dto.CreateOrderDto;
import com.banani.tourism.dto.OrderDto;
import com.banani.tourism.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

        import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody CreateOrderDto createOrderDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Mock user ID for now
        Long userId = 1L;
        String orderNumber = orderService.createOrder(userId, createOrderDto);
        return ResponseEntity.ok().body(Map.of(
                "message", "Order created successfully",
                "orderNumber", orderNumber
        ));
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getUserOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = 1L; // Mock user ID
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orders = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByNumber(@PathVariable String orderNumber) {
        OrderDto order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
}

// src/main/java/com/banani/tourism/config/SecurityConfig.java
package com.banani.tourism.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/**").permitAll() // Allow all API calls for now
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// src/main/java/com/banani/tourism/config/DatabaseSeeder.java
package com.banani.tourism.config;

import com.banani.tourism.entity.Category;
import com.banani.tourism.entity.Product;
import com.banani.tourism.repository.CategoryRepository;
import com.banani.tourism.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
                        new BigDecimal("29.99"), new BigDecimal("24.00"), "https://terrebrune.ma/cdn/shop/products/1_df7ce0f3-a311-43d0-8b53-d0e8b95c499c.png?v=1640013302",
                        crafts, Arrays.asList(), Arrays.asList("100ml", "250ml")),

                createProduct("Zaafaran (Saffron)", "Premium hand-harvested saffron threads from the Atlas Mountains, prized for flavor and color.",
                        new BigDecimal("14.99"), new BigDecimal("12.00"), "https://5.imimg.com/data5/SELLER/Default/2022/1/HD/FJ/HM/111935069/baby-saffron-main.jpg",
                        crafts, Arrays.asList(), Arrays.asList("1g", "5g")),

                createProduct("Hand-Painted Ceramic Plate", "Traditional Moroccan ceramic plate with intricate geometric patterns, perfect for decoration or serving.",
                        new BigDecimal("39.99"), new BigDecimal("29.00"), "https://i.etsystatic.com/25844243/r/il/0ee5b3/4551523770/il_570xN.4551523770_eodb.jpg",
                        pottery, Arrays.asList("Blue", "Green", "Multi"), Arrays.asList("Medium", "Large")),

                createProduct("Leather Pouf", "Hand-stitched leather pouf ottoman, made from genuine Moroccan leather, ideal for home décor.",
                        new BigDecimal("89.99"), new BigDecimal("74.00"), "https://mytindy.com/cdn/shop/products/CVuEpxCMc8.jpg?v=1629710322",
                        leather, Arrays.asList("Brown", "Tan", "White"), Arrays.asList("Standard")),

                createProduct("Berber Rug", "Handwoven Berber rug made from natural wool, featuring traditional Amazigh symbols and patterns.",
                        new BigDecimal("299.99"), new BigDecimal("249.00"), "https://www.e-mosaik.com/cdn/shop/products/dsa_0449_2048x.jpg?v=1517560654",
                        textiles, Arrays.asList("White", "Black", "Red"), Arrays.asList("Small", "Medium", "Large")),

                createProduct("Silver Berber Necklace", "Handcrafted silver necklace with traditional Berber motifs, made by local artisans.",
                        new BigDecimal("79.99"), new BigDecimal("64.00"), "https://www.moroccancorridor.com/cdn/shop/products/hamza-pendant-zaina-moroccan-jewelry-moroccan-corridorr-517.jpg?v=1623867915",
                        jewelry, Arrays.asList("Silver"), Arrays.asList()),

                createProduct("Handwoven Basket", "Colorful basket made from palm leaves, perfect for home storage or decoration.",
                        new BigDecimal("24.99"), new BigDecimal("19.00"), "https://www.ikea.com/ma/en/images/products/hoekrubba-basket-with-lid-bamboo__1376758_pe960491_s5.jpg",
                        crafts, Arrays.asList("Natural", "Multi"), Arrays.asList("Small", "Medium")),

                createProduct("Tadelakt Candle Holder", "Polished stone-like candle holder made from traditional Tadelakt plaster, adding a rustic touch.",
                        new BigDecimal("34.99"), new BigDecimal("29.00"), "https://cdn.myonlinestore.eu/9438a284-6be1-11e9-a722-44a8421b9960/image/cache/full/6f3bfc6d2d61155c70e820d99b88ae9241990835.jpg",
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

    private Product createProduct(String title, String description, BigDecimal price, BigDecimal discountedPrice,
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
        product.setRating(new BigDecimal("4.5"));
        product.setReviewCount(25);
        return product;
    }
}

// src/main/java/com/banani/tourism/exception/GlobalExceptionHandler.java
package com.banani.tourism.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

// src/main/java/com/banani/tourism/dto/AddToCartDto.java
package com.banani.tourism.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartDto {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String selectedColor;
    private String selectedSize;
}

// README.md
# Morocco Tourism E-commerce Backend API

This is a Spring Boot backend API for the Morocco Tourism E-commerce mobile application built with Flutter.

        ## Features

- **Product Management**: CRUD operations for products with categories, images, colors, sizes
- **Shopping Cart**: