package org.example;

import org.example.model.Category;
import org.example.model.Event;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.DataBaseConexion;
import org.example.util.DateTimeUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.util.*;

public class LoadDataBase {

    private final String insertCategory = "INSERT INTO category VALUES(?,?)";
    private final String insertProduct = "INSERT INTO product VALUES(?,?,?,?)";
    private final String insertUser = "INSERT INTO user VALUES(?,?)";
    private final String insertEvent = "INSERT INTO event VALUES(?,?,?,?)";

    private final int limitInsertion = 6000;
    private final Long dataLength = 60000L;

    public void loadData(String path, String delimiter){

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            for (Long i = dataLength; i <= 0 ; i--) {
                Set<Category> setCategory = new HashSet<>();
                Set<Product> setProduct = new HashSet<>();
                Set<User> setUser = new HashSet<>();
                Set<Event> setEvent = new HashSet<>();

                int contador = 0;
                while(contador != limitInsertion) {
                    String[] params = br.readLine().split(delimiter);

                    LocalDateTime eventTime = DateTimeUtil.dateTimeFormat(params[0]);
                    String eventType = params[1];
                    Long productId = Long.parseLong(params[2]);
                    Long categoryId = Long.parseLong(params[3]);
                    String categoryCode = params[4];
                    String brand = params[5];
                    BigDecimal price = BigDecimal.valueOf(Double.parseDouble(params[6]));
                    Long userId = Long.parseLong(params[7]);
                    String userSession = params[8];

                    setCategory.add(new Category(categoryId,categoryCode));
                    setProduct.add(new Product(productId,brand, price, categoryId));
                    setUser.add(new User(userId, userSession));
                    setEvent.add(new Event(eventType, eventTime,userId,productId));


                    contador++;
                    if (contador == limitInsertion){
                        insertCategories(setCategory);
                        insertProducts(setProduct);
                        insertUsers(setUser);
                        insertEvent(setEvent);
                    }
                }
                setCategory.clear();
                setProduct.clear();
                setEvent.clear();
                setUser.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void insertProducts(Set<Product> setProduct)  {
      Connection con = DataBaseConexion.getConnection();

        try(PreparedStatement p = con.prepareStatement(insertProduct)) {
            con.setAutoCommit(false);
            //false -> deshabilito transactions individuales, para realiza una sola transaction cada 6000
            setProduct.forEach(producto -> {
                try {
                    p.setLong(1,producto.getProductId());
                    p.setString(2,producto.getBrand());
                    p.setBigDecimal(3,producto.getPrice());
                    p.setLong(4,producto.getCategoryId());
                    p.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            //confirmo la transaccion
            con.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
      DataBaseConexion.closeConnection(con);
    }
    private void insertCategories(Set<Category> setCategory){
        Connection con = DataBaseConexion.getConnection();

        try(PreparedStatement p = con.prepareStatement(insertCategory)){
            con.setAutoCommit(false);
            setCategory.forEach(category->{
                try{
                    p.setLong(1,category.getCategoryId());
                    p.setString(2,category.getCategoryCode());
                    p.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                }

            });
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataBaseConexion.closeConnection(con);
    }

    public void insertUsers(Set<User> setUsers)  {
        Connection con = DataBaseConexion.getConnection();

        try(PreparedStatement p = con.prepareStatement(insertUser)) {
            con.setAutoCommit(false);
            setUsers.forEach(user -> {
                try {
                    p.setLong(1,user.getUserId());
                    p.setString(2,user.getUserSession());
                    p.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            con.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataBaseConexion.closeConnection(con);
    }

    public void insertEvent(Set<Event> setEvent)  {

        Connection con = DataBaseConexion.getConnection();

        try(PreparedStatement p = con.prepareStatement(insertEvent)) {
            con.setAutoCommit(false);
            setEvent.forEach(event -> {
                try {
                    p.setString(1,event.getEventType());
                    p.setObject(2,event.getEventTime());
                    p.setLong(3, event.getUserId());
                    p.setLong(4, event.getProductId());
                    p.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            con.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataBaseConexion.closeConnection(con);
    }



}


