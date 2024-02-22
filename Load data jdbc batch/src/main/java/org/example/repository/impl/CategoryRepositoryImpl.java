package org.example.repository.impl;

import org.example.model.Category;
import org.example.repository.ICrudRepository;
import org.example.repository.DataBaseConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryRepositoryImpl  implements ICrudRepository<Category> {
    private String findByIdInQuery = "SELECT * FROM category WHERE category_id IN ( ";
    private final String insertCategoryQuery = "INSERT INTO category VALUES(?,?)";
    private final int insertLimitSize = 5000;
    private Connection con;

    @Override
    public void saveAll(Set<Category> set) {
        con = DataBaseConexion.getConnection();

        try(PreparedStatement p = con.prepareStatement(insertCategoryQuery)){
            con.setAutoCommit(false);
            for (Category category: set) {
                p.setLong(1,category.getCategoryId());
                p.setString(2,category.getCategoryCode());
                p.addBatch();
            }

           p.executeBatch();
//            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataBaseConexion.closeConnection(con);
    }

    @Override
    public Set<Category> findAllByIdIn(List<Long> ids) {
        if (ids.size() < 1)return Collections.emptySet();


        Set<Category> categorySet = new HashSet<>();
        con = DataBaseConexion.getConnection();
        try(PreparedStatement p = con.prepareStatement(completeQueryWithList(ids))){
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()){
                categorySet.add(
                        new Category(resultSet.getLong("category_id")
                        ,resultSet.getString("category_code")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataBaseConexion.closeConnection(con);
        return categorySet;
    }
    private String completeQueryWithList(List<Long> ids){
        String formatListQuery = ids.toString().replace("[","").replace("]","");
        return findByIdInQuery.concat(formatListQuery).concat(" )");
    }
}
