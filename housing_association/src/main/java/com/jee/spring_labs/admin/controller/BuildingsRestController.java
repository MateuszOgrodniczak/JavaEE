package com.jee.spring_labs.admin.controller;

import com.jee.spring_labs.admin.dao.BuildingRepository;
import com.jee.spring_labs.model.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/buildings")
public class BuildingsRestController {

    private static final String XML_SUFFIX = ".xml";

    @Autowired
    private BuildingRepository buildingDao;

    @GetMapping(produces = "application/json")
    public List<Building> getBuildingsInJson() {
        return buildingDao.findAll();
    }

    @GetMapping(value = XML_SUFFIX, produces = "application/xml")
    public List<Building> getBuildingsInXml() {
        return buildingDao.findAll();
    }

    @GetMapping(value = "/{name}", produces = "application/json")
    public Building getBuildingInJson(@PathVariable("name") String name) {
        return buildingDao.findByName(name);
    }

    @GetMapping(value = "/{name}" + XML_SUFFIX, produces = "application/json")
    public Building getBuildingInXml(@PathVariable("name") String name) {
        return buildingDao.findByName(name);
    }
}
