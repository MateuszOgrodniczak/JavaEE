package com.jee.spring_labs.tenant.dao;

import com.jee.spring_labs.tenant.model.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    Consumption findByIdAndRemovedIsFalse(long id);

    Consumption findByIdAndApartment_Tenant_IdAndRemovedIsFalse(long id, long tenantId);

    @Query("SELECT c FROM Consumption c WHERE c.removed = false AND c.id = :id AND c.apartment.building.owner.id = :ownerId")
    Consumption findByIdAndOwner_IdAndRemovedIsFalse(@Param("id") long id, @Param("ownerId") long ownerId);

    @Query("SELECT c FROM Consumption c WHERE c.removed = false AND c.apartment.building.owner.id = :ownerId AND c.bill = null")
    List<Consumption> findAllByOwner_IdAndRemovedIsFalseAndBillIsNull(@Param("ownerId") long ownerId);

    @Query(value = "SELECT * FROM consumption WHERE removed = false AND fk_apartment = :apartmentId AND type LIKE :type% AND (to_char(date, 'YYYY-MM') = :month OR :allTime = true)", nativeQuery = true)
    List<Consumption> filterConsumptions(@Param("apartmentId") long apartmentId, @Param("type") String type, @Param("month") String month, @Param("allTime") boolean allTime);

    List<Consumption> findAllByApartmentTenant_IdAndRemovedIsFalse(long tenantId);

    @Query(value = "SELECT COUNT(*) FROM consumption WHERE EXTRACT(MONTH FROM date) = :month AND fk_apartment = :apartmentId AND \"type\" = :type", nativeQuery = true)
    int foundByMonthAndApartmentIdAndType(@Param("month") int month, @Param("apartmentId") long apartmentId, @Param("type") String consumptionType);
}
