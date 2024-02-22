package org.example;

import org.example.model.Category;
import org.example.model.Event;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.ICrudRepository;
import org.example.repository.impl.CategoryRepositoryImpl;
import org.example.repository.impl.EventRepositoryImpl;
import org.example.repository.impl.ProductRepositoryImpl;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.util.DateTimeUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collectors;

public class LoadDataBase {
    private ICrudRepository productRepository;
    private ICrudRepository categoryRepository;
    private ICrudRepository userRepository;
    private ICrudRepository eventRepository;

    public LoadDataBase(){
        productRepository = new ProductRepositoryImpl();
        categoryRepository = new CategoryRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        eventRepository = new EventRepositoryImpl();
    }
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

                if (countRead == 20000) {
                    Set<Category> categoriesUniques= categoryRepository.findAllByIdIn(new ArrayList<>(setCategory.stream().map(Category::getCategoryId).collect(Collectors.toList())));
                    setCategory.removeAll(categoriesUniques);
                    Set<Product> productsUniques = productRepository.findAllByIdIn(new ArrayList<>(setProducts.stream().map(Product::getProductId).collect(Collectors.toList())));
                    setProducts.removeAll(productsUniques);
                    Set<User> usersUniques = userRepository.findAllByIdIn(new ArrayList<>(setUser.stream().map(User::getUserId).collect(Collectors.toList())));
                    setUser.removeAll(usersUniques);

                    categoryRepository.saveAll(setCategory);
                    System.out.println("guardado category");
                    productRepository.saveAll(setProducts);
                    System.out.println("guardado products");
                    userRepository.saveAll(setUser);
                    System.out.println("guardado user");
                    eventRepository.saveAll(setEvent);
                    System.out.println("guardado event");

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
                Set<Category> categoriesUniques= categoryRepository.findAllByIdIn(new ArrayList<>(setCategory.stream().map(Category::getCategoryId).collect(Collectors.toList())));
                setCategory.removeAll(categoriesUniques);
                categoryRepository.saveAll(setCategory);
                setCategory.clear();
                categoriesUniques.clear();
            }
            if (setProducts.size()>0){
                Set<Product> productsUniques = productRepository.findAllByIdIn(new ArrayList<>(setProducts.stream().map(Product::getProductId).collect(Collectors.toList())));
                setProducts.removeAll(productsUniques);
                productRepository.saveAll(setProducts);
                setProducts.clear();
                productsUniques.clear();

            }
            if(setUser.size()>0){
                Set<User> usersUniques = userRepository.findAllByIdIn(new ArrayList<>(setUser.stream().map(User::getUserId).collect(Collectors.toList())));
                setUser.removeAll(usersUniques);
                userRepository.saveAll(setUser);
                setUser.clear();
                usersUniques.clear();
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
        return new Product(productId,band,price,categoryId);
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
        return new Event(eventType,eventTime,userId,productId);
    }
}


