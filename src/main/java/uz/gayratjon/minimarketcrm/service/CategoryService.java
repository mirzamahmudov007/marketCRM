package uz.gayratjon.minimarketcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gayratjon.minimarketcrm.exp.ResourceNotFoundException;
import uz.gayratjon.minimarketcrm.model.Category;
import uz.gayratjon.minimarketcrm.model.User;
import uz.gayratjon.minimarketcrm.dto.CategoryDTO;
import uz.gayratjon.minimarketcrm.reposiroty.CategoryRepository;
import uz.gayratjon.minimarketcrm.reposiroty.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Category> getAllCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Category getCategoryById(Long id, Long userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public List<Category> getAllPublicCategories() {
        return categoryRepository.findByIsPublic(true);
    }

    public Category createCategory(Category category, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public CategoryDTO createCategoryDTO(CategoryDTO categoryDTO, Long userId) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setPublic(categoryDTO.isPublic());

        if (categoryDTO.getParentId() != null) {
            Category parent = categoryRepository.findByIdAndUserId(categoryDTO.getParentId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
            category.setParent(parent);
        }

        Category savedCategory = createCategory(category, userId);
        return convertToDTO(savedCategory);
    }

    public CategoryDTO updateCategoryDTO(Long id, CategoryDTO categoryDTO, Long userId) {
        Category existingCategory = getCategoryById(id, userId);
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setPublic(categoryDTO.isPublic());

        if (categoryDTO.getParentId() != null) {
            Category parent = categoryRepository.findByIdAndUserId(categoryDTO.getParentId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
            existingCategory.setParent(parent);
        } else {
            existingCategory.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return convertToDTO(updatedCategory);
    }

    public void deleteCategory(Long id, Long userId) {
        Category category = getCategoryById(id, userId);
        categoryRepository.delete(category);
    }

    public List<Category> getRootCategories(Long userId) {
        return categoryRepository.findByUserIdAndParentIsNull(userId);
    }

    public CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setPublic(category.isPublic());
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }
        return dto;
    }

    public List<CategoryDTO> getAllCategoriesDTO(Long userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);
        return categories.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryByIdDTO(Long id, Long userId) {
        Category category = getCategoryById(id, userId);
        return convertToDTO(category);
    }

    public List<CategoryDTO> getRootCategoriesDTO(Long userId) {
        List<Category> rootCategories = getRootCategories(userId);
        return rootCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getAllPublicCategoriesDTO() {
        List<Category> publicCategories = getAllPublicCategories();
        return publicCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}

