package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.model.persistent.Country;
import com.itechart.security.business.model.persistent.CountryAltName;

/**
 * Created by pavel.urban on 10/6/2016.
 */
public interface CountryAltNameDao extends BaseDao<CountryAltName, Long, PagingFilter> {

    CountryAltName getByAltName(String altname);

}
