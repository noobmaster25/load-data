package org.example.repository;

import java.util.List;
import java.util.Set;

public interface ICrudRepository<T> {
    void saveAll(Set<T> set);
    Set<T> findAllByIdIn(List<Long> ids);
}
