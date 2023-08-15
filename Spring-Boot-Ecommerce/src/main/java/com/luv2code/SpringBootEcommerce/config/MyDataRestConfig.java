package com.luv2code.SpringBootEcommerce.config;

import com.luv2code.SpringBootEcommerce.Entity.Country;
import com.luv2code.SpringBootEcommerce.Entity.Product;
import com.luv2code.SpringBootEcommerce.Entity.ProductCategory;
import com.luv2code.SpringBootEcommerce.Entity.State;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {


//        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);


        HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.DELETE ,HttpMethod.PUT};
        //disable Http methods PUT, POST, DELETE FOR ProductCategory class
        disableHttpMethods(ProductCategory.class,config, theUnsupportedActions);
        //disable Http methods PUT, POST, DELETE FOR Product class
        disableHttpMethods(Product.class,config, theUnsupportedActions);
        //disable Http methods PUT, POST, DELETE FOR State class
        disableHttpMethods(State.class,config, theUnsupportedActions);
        //disable Http methods PUT, POST, DELETE FOR Country class
        disableHttpMethods(Country.class,config, theUnsupportedActions);

        //call an external helper method
        exposeId(config);
    }

    private static void disableHttpMethods(Class theClass,RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeId(RepositoryRestConfiguration config) {
        //expose entity ids

        //get a list of all entity class classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create list of all these entity types
        List<Class> entityClasses = new ArrayList<>();

        //get the entity types for all entities
        for (EntityType entityType: entities){
            entityClasses.add(entityType.getJavaType());
        }

        //expose the entity ids for the array entity/domain types
        Class[] domainTypes = entityClasses.toArray(entityClasses.toArray(new Class[0]));
        config.exposeIdsFor(domainTypes);

    }

}
