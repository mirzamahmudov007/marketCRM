package uz.gayratjon.minimarketcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.gayratjon.minimarketcrm.exp.ResourceNotFoundException;
import uz.gayratjon.minimarketcrm.model.Product;
import uz.gayratjon.minimarketcrm.model.User;
import uz.gayratjon.minimarketcrm.model.Category;
import uz.gayratjon.minimarketcrm.dto.ProductDTO;
import uz.gayratjon.minimarketcrm.reposiroty.CategoryRepository;
import uz.gayratjon.minimarketcrm.reposiroty.ProductRepository;
import uz.gayratjon.minimarketcrm.reposiroty.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ProductDTO> getAllProductsDTO(Long userId) {
        List<Product> products = productRepository.findByUserId(userId);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductByIdDTO(Long id, Long userId) {
        Product product = getProductById(id, userId);
        return convertToDTO(product);
    }

    public ProductDTO createProductDTO(ProductDTO productDTO, Long userId) {
        Product product = convertToEntity(productDTO);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        product.setUser(user);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    public ProductDTO updateProductDTO(Long id, ProductDTO productDTO, Long userId) {
        Product existingProduct = getProductById(id, userId);
        updateProductFromDTO(existingProduct, productDTO);
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(Long id, Long userId) {
        Product product = getProductById(id, userId);
        productRepository.delete(product);
    }

    public List<ProductDTO> getLowStockProductsDTO(Long userId) {
        List<Product> lowStockProducts = productRepository.findLowStockProducts(userId);
        return lowStockProducts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBarcode(product.getBarcode());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategory(product.getCategory().getName());
        }
        dto.setPurchasePrice(product.getPurchasePrice());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setQuantity(product.getQuantity());
        dto.setUnit(product.getUnit());
        dto.setMinQuantity(product.getMinQuantity());
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setBarcode(dto.getBarcode());
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setQuantity(dto.getQuantity());
        product.setUnit(dto.getUnit());
        product.setMinQuantity(dto.getMinQuantity());
        product.setImageUrl(dto.getImageUrl());
        return product;
    }

    private void updateProductFromDTO(Product product, ProductDTO dto) {
        product.setName(dto.getName());
        product.setBarcode(dto.getBarcode());
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setQuantity(dto.getQuantity());
        product.setUnit(dto.getUnit());
        product.setMinQuantity(dto.getMinQuantity());
        product.setImageUrl(dto.getImageUrl());
    }

    public Product getProductById(Long id, Long userId) {
        return productRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Transactional
    public void updateProductQuantity(Long productId, int quantityChange, Long userId) {
        Product product = productRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int newQuantity = product.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        product.setQuantity(newQuantity);
        productRepository.save(product);
    }
}

