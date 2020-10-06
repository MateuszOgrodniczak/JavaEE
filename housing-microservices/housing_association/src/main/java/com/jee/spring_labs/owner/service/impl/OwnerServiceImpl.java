package com.jee.spring_labs.owner.service.impl;

import com.jee.spring_labs.admin.dao.BuildingRepository;
import com.jee.spring_labs.model.Building;
import com.jee.spring_labs.model.User;
import com.jee.spring_labs.owner.dao.ApartmentRepository;
import com.jee.spring_labs.owner.dao.BillRepository;
import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.owner.model.Bill;
import com.jee.spring_labs.owner.service.OwnerService;
import com.jee.spring_labs.tenant.dao.ConsumptionRepository;
import com.jee.spring_labs.tenant.model.Consumption;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.service.EntityRemovalService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private ApartmentRepository apartmentDao;
    @Autowired
    private BuildingRepository buildingDao;
    @Autowired
    private ConsumptionRepository consumptionDao;
    @Autowired
    private BillRepository billDao;
    @Autowired
    private UserRepository userDao;

    @Autowired
    private EntityRemovalService removalService;

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getItems(Class<T> clazz, String filter, long ownerId) {
        if (clazz == Apartment.class) {
            return (List<T>) ((filter != null) ? apartmentDao.findAllByOwner_IdAndRoomAndRemovedIsFalse(filter, ownerId) : apartmentDao.findAllByOwner_IdAndRemovedIsFalse(ownerId));
        } else if (clazz == Building.class) {
            return (List<T>) ((filter != null) ? buildingDao.findAllByNameStartingWithAndOwner_IdAndRemovedIsFalse(filter, ownerId) : buildingDao.findAllByOwner_IdAndRemovedIsFalse(ownerId));
        } else if (clazz == Consumption.class) {
            return (List<T>) consumptionDao.findAllByOwner_IdAndRemovedIsFalseAndBillIsNull(ownerId);
        } else if (clazz == Bill.class) {
            return (List<T>) billDao.findAllByOwner_IdAndRemovedIsFalse(ownerId);
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public <T> T getItemById(Class<T> clazz, long id, long ownerId) {
        if (clazz == Apartment.class) {
            return (T) apartmentDao.findByIdAndOwner_Id(id, ownerId);
        } else if (clazz == Building.class) {
            Building building = buildingDao.findByIdAndOwner_IdAndRemovedIsFalse(id, ownerId);

            if (building != null) Hibernate.initialize(building.getApartments());
            return (T) building;
        } else if (clazz == Consumption.class) {
            return (T) consumptionDao.findByIdAndOwner_IdAndRemovedIsFalse(id, ownerId);
        } else if (clazz == Bill.class) {
            Bill bill = billDao.findByIdAndOwner_IdAndRemovedIsFalse(id, ownerId);
            Hibernate.initialize(bill.getConsumptions());
            return (T) bill;
        }
        return null;
    }

    @Override
    @Transactional
    public <T> boolean saveItem(Class<T> clazz, T item, long ownerId) {
        if (clazz == Apartment.class) {
            Apartment apartment = (Apartment) item;
            Building building = buildingDao.findBuildingById(apartment.getBuilding().getId());
            if (building == null || building.getOwner().getId() != ownerId) return false;
            //New apartment
            if (apartment.getId() == 0) {
                building.addApartment(apartment);
                apartmentDao.save(apartment);
                buildingDao.save(building);
                return true;
            }
            //Update
            apartmentDao.save(apartment);
        } else if (clazz == Building.class) {
            Building building = (Building) item;
            User owner = building.getOwner();
            if (owner.getId() != ownerId) {
                return false;
            }
            buildingDao.save(building);
            if (building.getId() == 0) {
                owner.setBuilding(building);
                userDao.save(owner);
            }
        } else if (clazz == Bill.class) {
            Bill bill = (Bill) item;
            long ownerId_ = bill.getOwner().getId();
            if (ownerId_ != ownerId) {
                return false;
            }
            Apartment apartment = apartmentDao.findByIdAndOwner_Id(bill.getApartment().getId(), ownerId);
            Hibernate.initialize(apartment.getBills());
            //TODO: apartment.getBills().add(bill);
            apartment.addBill(bill);
            if (bill.getId() == 0) {
                billDao.save(bill);
            }
            apartmentDao.save(apartment);
        }
        return true;
    }

    @Override
    public <T> boolean removeItem(Class<T> clazz, long id, long ownerId) {
        if (clazz == Apartment.class) {
            return removalService.removeApartmentById(id, ownerId);
        } else if (clazz == Building.class) {
            return removalService.removeBuildingById(id, ownerId);
        } else if (clazz == Bill.class) {
            return removalService.removeBillById(id, ownerId);
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean addToBill(long consumptionId, long ownerId) {
        Consumption consumption = consumptionDao.findByIdAndRemovedIsFalse(consumptionId);
        Bill bill = billDao.findActiveBillByApartmentId(consumption.getApartment().getId());
        if (bill == null) {
            bill = new Bill();
            bill.setDateOfCreation(LocalDate.now());
            bill.setApartment(consumption.getApartment());
            bill.setOwner(consumption.getApartment().getBuilding().getOwner());
            bill.setCost(consumption.getCost());
        } else {
            bill.setCost(bill.getCost() + consumption.getCost());
        }
        Hibernate.initialize(bill.getConsumptions());
        if (bill.getConsumptions().contains(consumption)) {
            return false;
        }
        bill.addConsumption(consumption);
        billDao.save(bill);

        return true;
    }

    @Override
    public boolean isRoomNumberTaken(int roomNumber, long buildingId, long apartmentId) {
        return apartmentDao.existsByRoomNumberAndBuilding_IdAndIdNot(roomNumber, buildingId, apartmentId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findUserByUsernameAndRemovedIsFalse(username);
    }
}
