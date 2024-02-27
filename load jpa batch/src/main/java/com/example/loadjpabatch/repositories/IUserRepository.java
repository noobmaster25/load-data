package com.example.loadjpabatch.repositories;

import com.example.loadjpabatch.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u.userId FROM User as u WHERE u.userId IN :ids")
    List<Long> findByUserIdIn(List<Long> ids);
}
