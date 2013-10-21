/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ato.cotepe.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author jacoboliveira
 */
public class GenericDao<E , ID extends Serializable> implements IGenericDao<E,ID> {
    private Class<E> persistentClass;
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("corretor-ato-cotepe-pu");
    private EntityManager entityManager;
    
    public GenericDao() {
        entityManager = entityManagerFactory.createEntityManager();
        this.persistentClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

//    public void init(EntityManager entityManager) {
//        this.entityManager = entityManager;
//        this.persistentClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//    }

    @Override
    public E findById(ID id, boolean lock) throws Exception {
        E object;
        if (lock) {
            object = (E) entityManager.find(persistentClass, id);
        } else {
            object = (E) entityManager.getReference(persistentClass, id);
        }
        return object;
    }

    @Override
    public E findById(ID id) throws Exception {
        if (entityManager.isOpen()) {
            E e = (E) entityManager.find(persistentClass, id);
            return e;
        }
        return null;
    }

    @Override
    public List<E> findAll() throws Exception {
        List<E> lista = createQuery("select entity from " + persistentClass.getSimpleName() + " entity").getResultList();
        return lista;
    }

    @Override
    public E save(E entity) throws Exception {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public E update(E entity) throws Exception {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public List<E> updateAll(E... entitys) throws Exception {
        for (E entity : entitys) {
            entity = entityManager.merge(entity);
        }
        return Arrays.asList(entitys);
    }

    @Override
    public List<E> updateAll(List<E> entitys) throws Exception {
        for (E entity : entitys) {
            entity = entityManager.merge(entity);
        }
        return entitys;
    }

    @Override
    public List<E> saveAll(E... entitys) throws Exception {
        for (E entity : entitys) {
            entityManager.persist(entity);
        }

        return Arrays.asList(entitys);
    }

    @Override
    public List<E> saveAll(List<E> entitys) throws Exception {
        for (E entity : entitys) {
            entityManager.persist(entity);
        }

        return entitys;
    }

    @Override
    public void delete(E entity) throws Exception {
        entityManager.remove(entity);
    }

    @Override
    public void delete(ID id) throws Exception {
        entityManager.remove(entityManager.find(persistentClass, id));
    }

    @Override
    public List<E> deleteAll(E... entitys) throws Exception {
        for (E entity : entitys) {
            entityManager.remove(entity);
        }
        return Arrays.asList(entitys);
    }

    @Override
    public List<ID> deleteAll(ID... ids) throws Exception {
        for (ID id : ids) {
            entityManager.remove(entityManager.find(persistentClass, id));
        }
        return Arrays.asList(ids);
    }
  
    
    @Override
    public Query createNamedQuery(String namedQuery) {
        return entityManager.createNamedQuery(namedQuery);
    }
    
    @Override
     public Query createQuery(String hql) throws Exception {
        return entityManager.createQuery(hql);
    }

    @Override
    public EntityManager getEntittyManager() {
        return entityManager;
    }
}
