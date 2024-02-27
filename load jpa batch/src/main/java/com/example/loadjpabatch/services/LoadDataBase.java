package com.example.loadjpabatch.services;

import com.example.loadjpabatch.entities.Category;
import com.example.loadjpabatch.entities.Event;
import com.example.loadjpabatch.entities.Product;
import com.example.loadjpabatch.entities.User;
import com.example.loadjpabatch.repositories.ICategoryRepository;
import com.example.loadjpabatch.repositories.IEventRepository;
import com.example.loadjpabatch.repositories.IProductRepository;
import com.example.loadjpabatch.repositories.IUserRepository;
import com.example.loadjpabatch.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Component
public class LoadDataBase {
    private  final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;
    private final IEventRepository eventRepository;


    public void loadData(String path, String delimiter) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            Set<Product> setProducts = new HashSet<>();
            Set<Category> setCategory = new HashSet<>();
            Set<User> setUser = new HashSet<>();
            Set<Event> setEvent = new HashSet<>();

            br.readLine();
            int countRead = 0;
            while(br.readLine()!= null){
                String[] rows = br.readLine().split(delimiter);

                setProducts.add(this.buildProductWithParams(rows));
                setCategory.add(this.buildCategoryWithParams(rows));
                setUser.add(this.buildUserWithParams(rows));
                setEvent.add(this.buildEventWithParams(rows));
                countRead++;

                if (countRead == 5000) {

                    List<Long> idsExistCategory = categoryRepository.findByCategoryIdIn(new ArrayList<>(setCategory.stream().map(Category::getCategoryId).collect(Collectors.toList())));
                    Set<Category> categoriesUniques = setCategory.stream().filter(category-> !idsExistCategory.contains(category.getCategoryId())).collect(Collectors.toSet());

                    List<Long> idsExistProduct = productRepository.findByProductIdIn(new ArrayList<>(setProducts.stream().map(Product::getProductId).collect(Collectors.toList())));
                    Set<Product> productsUniques = setProducts.stream().filter(product-> !idsExistProduct.contains(product.getProductId())).collect(Collectors.toSet());

                    List<Long> idsExistUser = userRepository.findByUserIdIn(new ArrayList<>(setUser.stream().map(User::getUserId).collect(Collectors.toList())));
                    Set<User> usersUniques = setUser.stream().filter(user-> !idsExistUser.contains(user.getUserId())).collect(Collectors.toSet());


                    categoryRepository.saveAll(setCategory);
                    productRepository.saveAll(setProducts);
                    userRepository.saveAll(setUser);
                    eventRepository.saveAll(setEvent);

                    setProducts.clear();
                    setCategory.clear();
                    setUser.clear();
                    setEvent.clear();
                    categoriesUniques.clear();
                    productsUniques.clear();
                    usersUniques.clear();
                    countRead = 0;
                    System.out.println("llego a 5000");
                    }

            }
            if(setCategory.size()>0){
                List<Long> idsExistCategory = categoryRepository.findByCategoryIdIn(new ArrayList<>(setCategory.stream().map(Category::getCategoryId).collect(Collectors.toList())));
                Set<Category> categoriesUniques = setCategory.stream().filter(category-> !idsExistCategory.contains(category.getCategoryId())).collect(Collectors.toSet());

                categoryRepository.saveAll(categoriesUniques);
                setCategory.clear();
                categoriesUniques.clear();
            }
            if (setProducts.size()>0){

                List<Long> idsExistProduct = productRepository.findByProductIdIn(new ArrayList<>(setProducts.stream().map(Product::getProductId).collect(Collectors.toList())));
                Set<Product> productsUniques = setProducts.stream().filter(product-> !idsExistProduct.contains(product.getProductId())).collect(Collectors.toSet());

                productRepository.saveAll(productsUniques);
                setProducts.clear();
                productsUniques.clear();

            }
            if(setUser.size()>0){
                List<Long> idsExistUser = userRepository.findByUserIdIn(new ArrayList<>(setUser.stream().map(User::getUserId).collect(Collectors.toList())));
                Set<User> usersUniques = setUser.stream().filter(user-> !idsExistUser.contains(user.getUserId())).collect(Collectors.toSet());
                userRepository.saveAll(usersUniques);
                setUser.clear();
                usersUniques.clear();
            }
            if(setEvent.size()>0){
                eventRepository.saveAll(setEvent);
                setEvent.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Category buildCategoryWithParams(String[]rows){
        Long categoryId = Long.parseLong(rows[3]);
        String categoryCode = rows[4];
        return new Category(categoryId,categoryCode);
    }
    private Product buildProductWithParams(String[]rows){
        Long productId = Long.parseLong(rows[2]);
        String band = rows[5];
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(rows[6]));
        Long categoryId = Long.parseLong(rows[3]);
        return new Product(productId,band,price,this.buildCategoryWithParams(rows));
    }
    private User buildUserWithParams(String[]rows){
        Long userId = Long.parseLong(rows[7]);
        String userSession = rows[8];
        return new User(userId,userSession);
    }
    private Event buildEventWithParams(String[]rows){
        LocalDateTime eventTime = DateTimeUtil.dateTimeFormat(rows[0]);
        String eventType = rows[1];
        Long userId = Long.parseLong(rows[7]);
        Long productId = Long.parseLong(rows[2]);
        return new Event(eventType,eventTime,this.buildUserWithParams(rows),this.buildProductWithParams(rows));
    }
}


