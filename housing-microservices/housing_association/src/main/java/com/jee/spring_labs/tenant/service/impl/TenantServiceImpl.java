package com.jee.spring_labs.tenant.service.impl;

import com.jee.spring_labs.model.Request;
import com.jee.spring_labs.model.User;
import com.jee.spring_labs.owner.dao.ApartmentRepository;
import com.jee.spring_labs.owner.dao.BillRepository;
import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.owner.model.Bill;
import com.jee.spring_labs.tenant.dao.ConsumptionRepository;
import com.jee.spring_labs.tenant.model.Consumption;
import com.jee.spring_labs.tenant.service.TenantService;
import com.jee.spring_labs.user.dao.RequestRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private ApartmentRepository apartmentDao;
    @Autowired
    private ConsumptionRepository consumptionDao;
    @Autowired
    private BillRepository billDao;
    @Autowired
    private RequestRepository requestDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getItems(Class<T> clazz, long tenantId) {
        if (clazz == Apartment.class) {
            return (List<T>) apartmentDao.findAllByTenant_IdAndRemovedIsFalse(tenantId);
        } else if (clazz == Consumption.class) {
            return (List<T>) consumptionDao.findAllByApartmentTenant_IdAndRemovedIsFalse(tenantId);
        } else if (clazz == Bill.class) {
            return (List<T>) billDao.findAllByTenant_IdAndRemovedIsFalse(tenantId);
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public <T> T getItemById(Class<T> clazz, long id, long tenantId) {
        if (clazz == Apartment.class) {
            Apartment apartment = apartmentDao.findByIdAndTenant_IdAndRemovedIsFalse(id, tenantId);
            Hibernate.initialize(apartment.getConsumptions());
            Hibernate.initialize(apartment.getBills());
            return (T) apartment;
        } else if (clazz == Consumption.class) {
            return (T) consumptionDao.findByIdAndApartment_Tenant_IdAndRemovedIsFalse(id, tenantId);
        } else if (clazz == Bill.class) {
            Bill bill = billDao.findByIdAndTenant_IdAndRemovedIsFalse(id, tenantId);
            Hibernate.initialize(bill.getConsumptions());
            return (T) bill;
        }
        return null;
    }

    @Override
    public <T> boolean saveItem(Class<T> clazz, T item, long tenantId) {
        if (clazz == Apartment.class) {
            Apartment apartment = (Apartment) item;
            if (apartment.getTenant().getId() != tenantId) {
                return false;
            }
            //TODO: check
            // apartment.getTenant().addApartment(apartment);
            apartmentDao.save((Apartment) item);
        } else if (clazz == Consumption.class) {
            Consumption consumption = (Consumption) item;
            /*if (consumptionDao.foundByMonthAndApartmentIdAndType(consumption.getDate().getMonth().getValue(), consumption.getApartment().getId(), consumption.getType().toString()) > 0) {
                return false;
            }*/
            consumptionDao.save((Consumption) item);
        } else if (clazz == Bill.class) {
            Bill bill = (Bill) item;
            if (bill.getApartment().getTenant().getId() != tenantId) {
                return false;
            }
            billDao.save((Bill) item);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean leaveApartment(long id, Locale locale, long tenantId) {
        Apartment apartment = apartmentDao.findByIdAndTenant_IdAndRemovedIsFalse(id, tenantId);
        if (apartment == null) {
            return false;
        }
        Hibernate.initialize(apartment.getBills());
        Hibernate.initialize(apartment.getConsumptions());
        if ((!apartment.getBills().isEmpty() && apartment.getBills().stream().anyMatch(bill -> !bill.isAccepted())) ||
                (!apartment.getConsumptions().isEmpty() && apartment.getConsumptions().stream().anyMatch(consumption -> (consumption.getBill() == null || !consumption.getBill().isAccepted())))) {
            return false;
        }
        //TODO: check if correct
        //apartment.setTenant(null);
        User tenant = apartment.getTenant();
        tenant.removeApartment(apartment);
        apartmentDao.save(apartment);

        Request request = new Request();
        Set<User> recipients = new HashSet<>();
        recipients.add(apartment.getBuilding().getOwner());
        request.setSender(tenant); //changed
        request.setRecipients(recipients);
        request.setSubject(messageSource.getMessage("label.request.leave_apartment", new Object[]{apartment.getRoomNumber()}, locale));
        request.setSendingDate(LocalDateTime.now().withNano(0));

        requestDao.save(request);
        return true;
    }

    @Override
    @Transactional
    public List<Apartment> cronGetAllApartments() {
        List<Apartment> apartments = apartmentDao.findAllByRemovedIsFalse();
        for (Apartment apartment : apartments) {
            Hibernate.initialize(apartment.getConsumptions());
        }
        return apartments;
    }

    @Override
    public List<Consumption> filterConsumptions(long apartmentId, String type, String month) {
        boolean allTime = !StringUtils.hasText(month);
        return consumptionDao.filterConsumptions(apartmentId, type, month, allTime);
    }

}
