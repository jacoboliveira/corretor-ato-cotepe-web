/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ato.cotepe.hibernate;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author jacoboliveira
 */
public interface IGenericDao<E, ID extends Serializable> {

    public E findById(ID id, boolean lock) throws Exception;

    public E findById(ID id) throws Exception;

    public List<E> findAll() throws Exception;

    public E save(E entity) throws Exception;

    public E update(E entity) throws Exception;

    public List<E> updateAll(E... entitys) throws Exception;

    public List<E> updateAll(List<E> entitys) throws Exception;

    public List<E> saveAll(E... entitys) throws Exception;

    public List<E> saveAll(List<E> entitys) throws Exception;

    public void delete(E entity) throws Exception;

    public void delete(ID id) throws Exception;

    public List<ID> deleteAll(ID... ids) throws Exception;

    public List<E> deleteAll(E... entitys) throws Exception;
        
    public Query createQuery(String hql) throws Exception; 
    
    public Query createNamedQuery(String namedQuery);
    
    public EntityManager getEntittyManager();
}
