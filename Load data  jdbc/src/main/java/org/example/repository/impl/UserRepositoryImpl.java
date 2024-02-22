package org.example.repository.impl;

import org.example.model.User;
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
import java.util.stream.Collectors;

public class UserRepositoryImpl implements ICrudRepository<User> {
    private final String insertUserQuery = "INSERT INTO user VALUES(?,?)";
    private String findByIdInQuery = "SELECT * FROM user WHERE user_id IN ( ";
    private final int insertLimitSize = 5000;
    private Connection con;
    @Override
    public void saveAll(Set<User> set) {
        con = DataBaseConexion.getConnection();

        try(PreparedStatement p = con.prepareStatement(insertUserQuery)) {
            con.setAutoCommit(false);
            int insertCount = 0;
            for(User user: set) {
                p.setLong(1, user.getUserId());
                p.setString(2, user.getUserSession());
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
    public Set<User> findAllByIdIn(List<Long> ids) {
        if (ids.size() < 1)return Collections.emptySet();
        Set<User> userSet = new HashSet<>();
        con = DataBaseConexion.getConnection();
        try(PreparedStatement p = con.prepareStatement(completeQueryWithList(ids))){
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()){
                userSet.add(new User(resultSet.getLong("user_id"), resultSet.getString("user_session")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataBaseConexion.closeConnection(con);
        return userSet.stream().filter(el -> !userSet.contains(el.getUserId())).collect(Collectors.toSet());
    }
    private String completeQueryWithList(List<Long> ids){
        String formatListQuery = ids.toString().replace("[","").replace("]","");
        return findByIdInQuery.concat(formatListQuery).concat(" )");
    }
}
