package com.itechart.security.business.service.impl;

import java.util.List;

import com.itechart.security.business.model.dto.utils.CompanyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itechart.security.business.dao.CompanyDao;
import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.dto.company.BusinessSphereDto;
import com.itechart.security.business.model.dto.company.CompanyDto;
import com.itechart.security.business.model.dto.company.CompanyTypeDto;
import com.itechart.security.business.model.dto.company.EmployeeNumberCategoryDto;
import com.itechart.security.business.service.CompanyService;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;
import static com.itechart.security.business.model.dto.utils.CompanyConverter.convert;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Override
    @Transactional
    public List<CompanyDto> findCompanies(CompanyFilter filter) {
        return convertCollection(companyDao.find(filter), CompanyConverter::convert);
    }

    @Override
    @Transactional
    public Long saveCompany(CompanyDto company) {
        return companyDao.save(convert(company));
    }

    @Override
    @Transactional
    public CompanyDto get(Long id) {
        return convert(companyDao.get(id));
    }

    @Override
    @Transactional
    public void updateCompany(CompanyDto company) {
        companyDao.update(convert(company));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        companyDao.deleteById(id);
    }

    @Override
    @Transactional
    public int countCompanies(CompanyFilter filter) {
        return companyDao.count(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyTypeDto> loadCompanyTypes() {
        return convertCollection(companyDao.loadCompanyTypes(), CompanyConverter::convert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusinessSphereDto> loadBusinessSpheres() {
        return convertCollection(companyDao.loadBusinessSpheres(), CompanyConverter::convert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeNumberCategoryDto> loadEmployeeNumberCategories() {
        return convertCollection(companyDao.loadEmployeeNumberCategories(), CompanyConverter::convert);
    }

}