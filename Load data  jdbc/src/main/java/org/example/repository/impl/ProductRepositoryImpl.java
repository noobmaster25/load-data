package org.example.repository.impl;

import org.example.model.Product;
import org.example.repository.ICrudRepository;
import org.example.repository.DataBaseConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductRepositoryImpl implements ICrudRepository<Product> {
    private final String insertProductQuery = "INSERT INTO product VALUES(?,?,?,?)";
    private String findByIdInQuery = "SELECT * FROM product WHERE idproduct IN ( ";
    private Connection con;
    private final int insertLimitSize = 5000;

    @Override
    public void saveAll(Set<Product> set) {
        con = DataBaseConexion.getConnection();

        try(PreparedStatement p = con.prepareStatement(insertProductQuery)) {
            con.setAutoCommit(false);
            //false -> deshabilito transactions individuales, para realiza una sola transaction cada 6000
            int insertCount = 0;
            for(Product producto : set) {
                p.setLong(1, producto.getProductId());
                p.setString(2, producto.getBrand());
                p.setBigDecimal(3, producto.getPrice());
                p.setLong(4, producto.getCategoryId());
                p.executeUpdate();
                insertCount++;
                if (insertCount == insertLimitSize){
                    con.commit();
                    insertCount =0;
                }
            }
            if(insertCount>0)con.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataBaseConexion.closeConnection(con);
    }

    @Override
    public Set<Product> findAllByIdIn(List<Long> ids) {
        if (ids.size() < 1)return Collections.emptySet();

        Set<Product> products = new HashSet<>();
        con = DataBaseConexion.getConnection();
        try(PreparedStatement p = con.prepareStatement(completeQueryWithList(ids))){
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()){
                products.add(new Product(
                        resultSet.getLong("idproduct"),
                        resultSet.getString("brand"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getLong("category_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataBaseConexion.closeConnection(con);
        return products;

    }
    private String completeQueryWithList(List<Long> ids){
        String formatListQuery = ids.toString().replace("[","").replace("]","");
        return findByIdInQuery.concat(formatListQuery).concat(" )");
    }
}
