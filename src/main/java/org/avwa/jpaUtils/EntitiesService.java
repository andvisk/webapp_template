package org.avwa.jpaUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.avwa.entities.EntityBase;
import org.avwa.utils.AnnotationsUtils;
import org.primefaces.model.FilterMeta;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class EntitiesService {

    @PersistenceContext
    private EntityManager em;

    public <T> List<T> findAll(Class<T> clazz) {
        List<T> retList = em.createQuery("select a from " + AnnotationsUtils.getEntityName(clazz) + " a")
                .getResultList();
        retList = detach(retList);
        return retList;
    }

    public <T> void deleteById(Class<T> clazz, long id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        String jpqlQuery = "delete from " + AnnotationsUtils.getEntityName(clazz) + " a where a.id = :id";
        executeUpdate(jpqlQuery, parameters);
    }

    public <T> void executeUpdate(String jpqlQuery, Map<String, Object> parameters) {
        Query query = em.createQuery(jpqlQuery);
        query = setParameters(query, parameters);
        query.executeUpdate();
    }

    public <T> T refresh(T obj) {
        T freshObj = find((Class<T>) obj.getClass(), ((EntityBase) obj).getId());
        return detach(freshObj);
    }

    public <T> List<T> findForPaging(String jpqlQuery, int from, int count, Map<String, FilterMeta> filterBy) {
        Query query = em.createQuery(jpqlQuery);
        query = setParametersFilterMeta(query, filterBy);
        query.setFirstResult(from);
        query.setMaxResults(count);
        List<T> retList = query.getResultList();
        retList.stream().forEach(p -> detach(p));
        return retList;
    }

    public int findCount(String jpqlQuery, Map<String, FilterMeta> filterBy) {
        Query query = em.createQuery(jpqlQuery);
        query = setParametersFilterMeta(query, filterBy);
        return ((Number) query.getSingleResult()).intValue();
    }

    public <T> T find(Class<T> clazz, Long id) {
        T ret = em.find(clazz, id);
        detach(ret);
        return ret;
    }

    public <T> T find(String jpqlQuery, Map<String, Object> parameters) {
        Query query = em.createQuery(jpqlQuery);
        query = setParameters(query, parameters);
        try {
            T ret = (T) query.getSingleResult();
            detach(ret);
            return ret;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public <T> List<T> findList(String jpqlQuery, Map<String, Object> parameters) {
        Query query = em.createQuery(jpqlQuery);
        query = setParameters(query, parameters);
        try {
            List<T> ret = (List<T>) query.getResultList();
            return detach(ret);
        } catch (NoResultException nre) {
            return null;
        }
    }

    public <T> T merge(T entity) {
        T ret = em.merge(entity);
        em.flush();
        detach(ret);
        return ret;
    }

    public <T> List<T> detach(List<T> list) {
        list.stream().forEach(p -> detach(p));
        return list;
    }

    public <T> T detach(T entity) {
        em.detach(entity);
        return entity;
    }

    private Query setParametersFilterMeta(Query query, Map<String, FilterMeta> filterBy) {
        for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet()) {
            query.setParameter(entry.getValue().getField(), entry.getValue().getFilterValue());
        }
        return query;
    }

    private Query setParameters(Query query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

}
