package com.jee.spring_labs.admin.dao;

import com.jee.spring_labs.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    Building findByName(String name);

    Building findBuildingById(long id);

    Building findBuildingByIdAndRemovedIsFalse(long id);

    Building findByIdAndOwner_IdAndRemovedIsFalse(long id, long owner_id);

    List<Building> findAllByNameStartingWith(String name);

    List<Building> findAllByRemoved(boolean removed);

    List<Building> findAllByNameStartingWithAndOwner_IdAndRemovedIsFalse(String name, long ownerId);

    List<Building> findAllByOwner_IdAndRemovedIsFalse(long ownerId);

    List<Building> findAllByNameStartingWithAndRemoved(String name, boolean removed);
}
