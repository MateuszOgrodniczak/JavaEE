package com.jee.spring_labs.admin.service.impl;

import com.jee.spring_labs.admin.dao.BuildingRepository;
import com.jee.spring_labs.admin.service.AdminService;
import com.jee.spring_labs.model.Building;
import com.jee.spring_labs.model.Request;
import com.jee.spring_labs.model.User;
import com.jee.spring_labs.user.dao.RequestRepository;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.service.EntityRemovalService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private RequestRepository requestDao;

    @Autowired
    private BuildingRepository buildingDao;

    @Autowired
    private EntityRemovalService removalService;

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getItems(Class<T> clazz, String filter, Boolean removed) {
        if (clazz == User.class) {
            if (removed == null) {
                return (List<T>) ((filter != null) ? userDao.filterUsersByUsername(filter) : userDao.findAll());
            } else {
                return (List<T>) ((filter != null) ? userDao.filterUsersByUsernameAndRemoved(filter, removed) : userDao.filterUsersByRemoved(removed));
            }
        }
        if (clazz == Building.class) {
            if (removed == null) {
                return (List<T>) ((filter != null) ? buildingDao.findAllByNameStartingWith(filter) : buildingDao.findAll());
            } else {
                return (List<T>) ((filter != null) ? buildingDao.findAllByNameStartingWithAndRemoved(filter, removed) : buildingDao.findAllByRemoved(removed));
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public <T> T getItemById(Class<T> clazz, long id) {
        if (clazz == User.class) {
            return (T) userDao.findUserById(id);
        }
        if (clazz == Building.class) {
            Building building = buildingDao.findBuildingById(id);
            Hibernate.initialize(building.getApartments());
            return (T) building;
        }
        if (clazz == Request.class) {
            return (T) requestDao.findRequestById(id);
        }
        return null;
    }

    @Override
    public <T> boolean saveItem(T item) {
        if (item.getClass() == User.class) {
            userDao.save((User) item);
        } else if (item.getClass() == Building.class) {
            Building b = (Building) item;
            try {
                buildingDao.save(b);
            } catch(DataIntegrityViolationException e) {
                return false;
            }
        } else if (item.getClass() == Request.class) {
            requestDao.save((Request) item);
        }
        return true;
    }

    @Override
    @Transactional
    public <T> boolean removeItem(Class<T> clazz, long id) {
        if (clazz == User.class) {
            User user = userDao.findUserById(id);
            if (user == null)
                return false;
            return removalService.removeUser(user);
        } else if (clazz == Building.class) {
            return removalService.removeBuildingById(id, null);
        } else if (clazz == Request.class) {
            Request request = requestDao.findRequestById(id);
            if (request == null)
                return false;
            requestDao.delete(request);
        }
        return true;
    }

    @Override
    public <T> boolean activateItem(Class<T> clazz, long id) {
        if (clazz == User.class) {
            User user = userDao.findUserById(id);
            if (user == null)
                return false;
            user.setRemoved(false);
            userDao.save(user);
        } else if (clazz == Building.class) {
            Building b = buildingDao.findBuildingById(id);
            if (b == null) return false;
            b.setRemoved(false);
            buildingDao.save(b);
        } else if (clazz == Request.class) {
            Request request = requestDao.findRequestById(id);
            if (request == null)
                return false;
            request.setRemoved(false);
            requestDao.save(request);
        }
        return true;
    }

    @Override
    public User getUserByUserName(String username) {
        return userDao.findUserByUsernameAndRemovedIsFalse(username);
    }

}
