package com.jee.spring_labs.admin.controller;

import com.jee.spring_labs.admin.dao.BuildingRepository;
import com.jee.spring_labs.model.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buildings")
/*@CrossOrigin(origins="*")*/
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

    @GetMapping(value = "/{id}", produces = "application/json")
    public Building getBuildingInJson(@PathVariable("id") long id) {
        return buildingDao.findBuildingById(id);
    }

    @GetMapping(value = "/{id}" + XML_SUFFIX, produces = "application/json")
    public Building getBuildingInXml(@PathVariable("id") long id) {
        return buildingDao.findBuildingById(id);
    }
}
