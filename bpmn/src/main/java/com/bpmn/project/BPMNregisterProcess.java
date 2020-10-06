package com.bpmn.project;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;
import static org.camunda.bpm.engine.authorization.Resources.FILTER;

@SpringBootApplication
@EnableProcessApplication
public class BPMNregisterProcess extends SpringBootServletInitializer {

    public static void main(String... args) {
        SpringApplication.run(BPMNregisterProcess.class, args);

        ProcessEngine engine = BpmPlatform.getDefaultProcessEngine();
        createDefaultUser(engine);
    }
    
    public static void createDefaultUser(ProcessEngine engine) {
        // and add default user to Camunda to be ready-to-go
        if (engine.getIdentityService().createUserQuery().userId("mati").count() == 0) {
            User user = engine.getIdentityService().newUser("mati");
            user.setFirstName("mati");
            user.setLastName("mati");
            user.setPassword("mati");
            user.setEmail("mati@camunda.org");
            engine.getIdentityService().saveUser(user);

            Group group = engine.getIdentityService().newGroup(Groups.CAMUNDA_ADMIN);
            group.setName("Administrators");
            group.setType(Groups.GROUP_TYPE_SYSTEM);
            engine.getIdentityService().saveGroup(group);

            for (Resource resource : Resources.values()) {
                Authorization auth = engine.getAuthorizationService().createNewAuthorization(AUTH_TYPE_GRANT);
                auth.setGroupId(Groups.CAMUNDA_ADMIN);
                auth.addPermission(ALL);
                auth.setResourceId(ANY);
                auth.setResource(resource);
                engine.getAuthorizationService().saveAuthorization(auth);
            }

            engine.getIdentityService().createMembership("mati", Groups.CAMUNDA_ADMIN);
        }

        // create default "all tasks" filter
        if (engine.getFilterService().createFilterQuery().filterName("all").count() == 0) {

            Map<String, Object> filterProperties = new HashMap<String, Object>();
            filterProperties.put("description", "all");
            filterProperties.put("priority", 10);

            Filter filter = engine.getFilterService().newTaskFilter() //
                    .setName("all") //
                    .setProperties(filterProperties)//
                    .setOwner("mati")//
                    .setQuery(engine.getTaskService().createTaskQuery());
            engine.getFilterService().saveFilter(filter);

            // and authorize demo user for it
            if (engine.getAuthorizationService().createAuthorizationQuery().resourceType(FILTER).resourceId(filter.getId()) //
                    .userIdIn("mati").count() == 0) {
                Authorization managementGroupFilterRead = engine.getAuthorizationService().createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
                managementGroupFilterRead.setResource(FILTER);
                managementGroupFilterRead.setResourceId(filter.getId());
                managementGroupFilterRead.addPermission(ALL);
                managementGroupFilterRead.setUserId("mati");
                engine.getAuthorizationService().saveAuthorization(managementGroupFilterRead);
            }

        }
    }
}
