package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.CountryAltNameDao;
import com.itechart.security.business.model.persistent.Country;
import com.itechart.security.business.model.persistent.CountryAltName;
import org.springframework.stereotype.Repository;

/**
 * Created by pavel.urban on 10/6/2016.
 */
@Repository
public class CountryAltNameDaoImpl extends BaseHibernateDao<CountryAltName, Long, PagingFilter> implements CountryAltNameDao{

    @Override
    public CountryAltName getByAltName(String altname) {
        return findOne("from CountryAltName where altname=?", altname);
    }

}
