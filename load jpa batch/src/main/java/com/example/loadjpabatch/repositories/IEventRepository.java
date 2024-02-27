package com.example.loadjpabatch.repositories;

import com.example.loadjpabatch.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends JpaRepository<Event,Long> {
}
