package com.jee.spring_labs.owner.dao;

import com.jee.spring_labs.owner.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    Apartment findByIdAndRemovedIsFalse(long id);

    Apartment findByIdAndTenant_IdAndRemovedIsFalse(long id, long tenantId);

    @Query("SELECT a FROM Apartment a, Bill b, Consumption c WHERE a.id = :id AND a.tenant.id = :tenantId AND (b.apartment = NULL OR (b.apartment.id = :id AND b.accepted = true)) " +
            "AND (c.apartment = NULL OR (c.apartment.id = :id AND c.bill != NULL AND c.bill.accepted = true))")
    Apartment findApartmentWithNoBillsOrConsumptions(@Param("id") long id, @Param("tenantId") long tenantId);

    List<Apartment> findAllByTenant_IdAndRemovedIsFalse(long tenantId);

    List<Apartment> findAllByRemovedIsFalse();

    @Query("SELECT a FROM Apartment a WHERE a.id = :id AND a.removed = false AND a.building.owner.id = :ownerId")
    Apartment findByIdAndOwner_IdAndRemovedIsFalse(@Param("id") long id, @Param("ownerId") long ownerId);

    @Query("SELECT a FROM Apartment a WHERE a.id = :id AND a.building.owner.id = :ownerId")
    Apartment findByIdAndOwner_Id(@Param("id") long id, @Param("ownerId") long ownerId);

    @Query("SELECT a FROM Apartment a WHERE a.building.owner.id = :ownerId AND a.removed = false AND a.roomNumber LIKE " + ":room%")
    List<Apartment> findAllByOwner_IdAndRoomAndRemovedIsFalse(@Param("room") String roomNumber, @Param("ownerId") long ownerId);

    @Query("SELECT a FROM Apartment a WHERE a.building.owner.id = :ownerId AND a.removed = false")
    List<Apartment> findAllByOwner_IdAndRemovedIsFalse(@Param("ownerId") long ownerId);

    boolean existsByRoomNumberAndBuilding_IdAndIdNot(int roomNumber, long buildingId, long apartmentId);
}
