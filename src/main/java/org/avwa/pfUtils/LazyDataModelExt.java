package org.avwa.pfUtils;

import java.util.List;
import java.util.Map;

import org.avwa.jpaUtils.EntitiesService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LazyDataModelExt<T> extends LazyDataModel<T> {

    Logger log = LoggerFactory.getLogger(LazyDataModelExt.class);

    private EntitiesService entitiesService;

    public LazyDataModelExt(EntitiesService entitiesService) {
        this.entitiesService = entitiesService;
    }

    public abstract String getSubQueryFilterBy(Map<String, FilterMeta> filterBy);

    public abstract String getSubQuerySortBy(Map<String, SortMeta> sortBy);

    public abstract String getCountQuery(Map<String, FilterMeta> filterBy);

    public abstract String getLoadQuery(Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return getRowCount();
    }

    @Override
    public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
 
        setRowCount(entitiesService.findCount(getCountQuery(filterBy), filterBy));

        first = recalculateFirst(first, pageSize, getRowCount());

        return entitiesService.findForPaging(getLoadQuery(sortBy, filterBy), first, pageSize, filterBy);
    }
}
