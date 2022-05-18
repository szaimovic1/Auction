package com.ABH.Auction.services;

import com.ABH.Auction.domain.Category;
import com.ABH.Auction.repositories.CategoryRepository;
import com.ABH.Auction.repositories.ProductRepository;
import com.ABH.Auction.responses.CategoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryResponse> getCategories(String type) {
        List<Category> main = categoryRepository.findByCategory(null);
        List<CategoryResponse> response = new ArrayList<>();

        for (Category c : main)
            response.add(aloneCategory(c.getName(), c.getId()));
        if(type.equals("main")) return response;

        setSubcategories(main, response);
        return response;
    }

    public CategoryResponse aloneCategory(String name, Long id) {
        return new CategoryResponse(name, productRepository
                    .getProductsByCategoryId(id, LocalDateTime.now(), null)
                    .size(), new ArrayList<>(), id);
    }

    public void setSubcategories(List<Category> main, List<CategoryResponse> response) {
        int i = 0;
        for (Category c : main) {
            List<Category> subResponse = categoryRepository.findByCategory(c);
            for(Category s : subResponse)
                response.get(i).getSubcategories().add(aloneCategory(s.getName(), s.getId()));
            i++;
        }
    }

    public List<String> getCategoriesNames(List<Long> categories) {
        return categories.stream().map(categoryRepository::getCategoryName).collect(Collectors.toList());
    }

    public Category checkCategory(Long id) {
        Optional<Category> c = categoryRepository.getCategoryById(id);
        return c.orElse(null);
    }
}
