package org.avwa.controllers;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import org.avwa.jpaUtils.EntitiesService;
import org.avwa.pfUtils.LazyDataModelExt;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;

import jakarta.inject.Inject;

public abstract class BaseController<T> implements Serializable {

    @Inject
    EntitiesService entService;

    @Inject
    Logger log;

    private LazyDataModelExt<T> model;

    public T createNewObject(Class<T> clazz) {
        try {
            Constructor<T> ct = clazz.getDeclaredConstructor(new Class[0]);
            return (T) ct.newInstance();
        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
    }

    public void setModel(LazyDataModelExt<T> model) {
        this.model = model;
    }

    public LazyDataModel<T> getModel() {
        return model;
    }

    public abstract T getObject();
}
