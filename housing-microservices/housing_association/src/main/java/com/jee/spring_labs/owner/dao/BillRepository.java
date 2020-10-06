package com.jee.spring_labs.owner.dao;

import com.jee.spring_labs.owner.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface BillRepository extends JpaRepository<Bill, Long> {

    Bill findByIdAndOwner_IdAndRemovedIsFalse(long id, long ownerId);

    Bill findByIdAndRemovedIsFalse(long id);

    @Query("SELECT b FROM Bill b WHERE b.removed = false AND b.dateOfPayment = NULL AND b.apartment.id = :apartmentId")
    Bill findActiveBillByApartmentId(@Param("apartmentId") long apartmentId);

    Bill findByIdAndOwner_IdAndRemovedIsFalseAndAcceptedIsTrue(long id, long ownerId);

    @Query("SELECT b FROM Bill b WHERE b.removed = false AND b.id = :id AND b.apartment.tenant.id = :tenantId")
    Bill findByIdAndTenant_IdAndRemovedIsFalse(@Param("id") long id, @Param("tenantId") long tenantId);

    List<Bill> findAllByOwner_IdAndRemovedIsFalse(long ownerId);

    @Query("SELECT b FROM Bill b WHERE b.removed = false AND b.apartment.tenant.id = :tenantId")
    List<Bill> findAllByTenant_IdAndRemovedIsFalse(@Param("tenantId") long tenantId);
}
