package com.jee.spring_labs.user.service.impl;

import com.jee.spring_labs.admin.dao.BuildingRepository;
import com.jee.spring_labs.model.Building;
import com.jee.spring_labs.model.User;
import com.jee.spring_labs.owner.dao.ApartmentRepository;
import com.jee.spring_labs.owner.dao.BillRepository;
import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.owner.model.Bill;
import com.jee.spring_labs.tenant.model.Consumption;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.service.EntityRemovalService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EntityRemovalServiceImpl implements EntityRemovalService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private BuildingRepository buildingDao;

    @Autowired
    private ApartmentRepository apartmentDao;

    @Autowired
    private BillRepository billDao;

    @Override
    public boolean removeUser(User user) {
        user.setRemoved(true);
        userDao.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean removeBuildingById(long id, Long ownerId) {
        Building building = (ownerId == null) ? buildingDao.findBuildingByIdAndRemovedIsFalse(id) : buildingDao.findByIdAndOwner_IdAndRemovedIsFalse(id, ownerId);
        if (building == null)
            return false;
        Hibernate.initialize(building.getApartments());
        for (Apartment apartment : building.getApartments()) {
            removeApartment(apartment);
        }
        if (building.getAddress() != null) {
            building.getAddress().setRemoved(true);
        }
        building.setRemoved(true);
        buildingDao.save(building);
        return true;
    }

    @Override
    @Transactional
    public boolean removeApartment(Apartment apartment) {
        Hibernate.initialize(apartment.getBills());
        for (Bill bill : apartment.getBills()) {
            removeBill(bill);
        }
        for (Consumption consumption : apartment.getConsumptions()) {
            consumption.setRemoved(true);
        }
        apartment.setRemoved(true);
        return true;
    }

    @Override
    @Transactional
    public boolean removeApartmentById(long id, long ownerId) {
        Apartment apartment = apartmentDao.findByIdAndOwner_IdAndRemovedIsFalse(id, ownerId);
        Hibernate.initialize(apartment.getBills());
        for (Bill bill : apartment.getBills()) {
            removeBill(bill);
        }
        apartment.setRemoved(true);
        apartmentDao.save(apartment);
        return true;
    }

    @Override
    @Transactional
    public boolean removeBill(Bill bill) {
        Hibernate.initialize(bill.getConsumptions());
        for (Consumption consumption : bill.getConsumptions()) {
            consumption.setRemoved(true);
        }
        bill.setRemoved(true);
        return true;
    }

    @Override
    @Transactional
    public boolean removeBillById(long id, long ownerId) {
        Bill bill = billDao.findByIdAndOwner_IdAndRemovedIsFalse(id, ownerId);
        if (bill == null) return false;
        Hibernate.initialize(bill.getConsumptions());
        for (Consumption consumption : bill.getConsumptions()) {
            consumption.setRemoved(true);
        }
        bill.setRemoved(true);
        billDao.save(bill);
        return true;
    }
}
