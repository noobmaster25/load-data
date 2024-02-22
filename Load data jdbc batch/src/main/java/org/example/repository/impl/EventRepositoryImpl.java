package org.example.repository.impl;

import org.example.model.Event;
import org.example.repository.ICrudRepository;
import org.example.repository.DataBaseConexion;
import org.example.util.DateTimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EventRepositoryImpl implements ICrudRepository<Event> {
    private final String insertEventQuery = "INSERT INTO event VALUES(?,?,?,?)";
    String findByIdInQuery = "SELECT * FROM product WHERE idproduct IN ( ";
    private Connection con;


    @Override
    public void saveAll(Set<Event> set) {

        con = DataBaseConexion.getConnection();
        try(PreparedStatement p = con.prepareStatement(insertEventQuery)) {
            con.setAutoCommit(true);
            for(Event event: set) {
                p.setString(1, event.getEventType());
                p.setObject(2, event.getEventTime());
                p.setLong(3, event.getUserId());
                p.setLong(4, event.getProductId());
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
    public Set<Event> findAllByIdIn(List<Long> ids) {
        if (ids.size() < 1)return Collections.emptySet();
            Set<Event> eventSet = new HashSet<>();
            con = DataBaseConexion.getConnection();

            findByIdInQuery = findByIdInQuery + (ids.toString().replace("[","").replace("]",""))+" )";

            try(PreparedStatement p = con.prepareStatement(findByIdInQuery)){
                ResultSet resultSet = p.executeQuery();
                while (resultSet.next()){
                    eventSet.add(new Event(
                            resultSet.getString("event_type"),
                            DateTimeUtil.dateTimeFormat(resultSet.getString("event_time")),
                            resultSet.getLong("user_id"),
                            resultSet.getLong("product_id")
                    ));
                }
            } catch (SQLException e) {
                System.out.println(ids.toString());
                System.out.println(findByIdInQuery);
                throw new RuntimeException(e);
            }

            DataBaseConexion.closeConnection(con);
            return eventSet;

    }
}
