package com.example.letsplay.services;

import com.example.letsplay.models.Product;
import com.example.letsplay.repository.ProductRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    // 1. GET ALL
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // 2. GET BY ID
    public Optional<Product> getProductById(String id) {
        return productRepo.findById(id);
    }

    // 3. CREATE (POST)
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    // 4. FULL UPDATE (PUT)
    public Product updateProduct(String id, Product productDetails) {
        return productRepo.findById(id).map(existingProduct -> {
            existingProduct.setName(productDetails.getName());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setUserId(productDetails.getUserId());
            return productRepo.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // 5. PARTIAL UPDATE (PATCH)
    public Product patchProduct(String id, Product partialProduct) {
        return productRepo.findById(id).map(existingProduct -> {
            if (partialProduct.getName() != null) {
                existingProduct.setName(partialProduct.getName());
            }
            if (partialProduct.getDescription() != null) {
                existingProduct.setDescription(partialProduct.getDescription());
            }
            if (partialProduct.getPrice() != null) {
                existingProduct.setPrice(partialProduct.getPrice());
            }
            if (partialProduct.getUserId() != null) {
                existingProduct.setUserId(partialProduct.getUserId());
            }
            return productRepo.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // 6. DELETE
    public boolean deleteProduct(String id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
            return true;
        }
        return false;
    }
    public Product updateProductWithOwnershipCheck(String id, Product productDetails) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = (String) auth.getCredentials();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Reject if user is not admin and does not own the product
        if (!isAdmin && !existingProduct.getUserId().equals(currentUserId)) {
            throw new RuntimeException("Unauthorized: You can only manage your own products");
        }

        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        return productRepo.save(existingProduct);
    }
}