package com.itechart.security.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itechart.security.business.service.CompanyService;
import com.itechart.security.business.model.dto.company.BusinessSphereDto;
import com.itechart.security.business.model.dto.company.CompanyDto;
import com.itechart.security.business.model.dto.company.CompanyTypeDto;
import com.itechart.security.business.model.dto.company.EmployeeNumberCathegoryDto;
import com.itechart.security.business.model.persistent.company.BusinessSphere;
import com.itechart.security.business.model.persistent.company.CompanyType;
import com.itechart.security.business.model.persistent.company.EmployeeNumberCathegory;
import com.itechart.security.core.acl.AclPermissionEvaluator;
import com.itechart.security.service.AclService;
import com.itechart.security.web.model.dto.CompanyFilterDto;
import com.itechart.security.web.model.dto.DataPageDto;

import static com.itechart.security.web.model.dto.Converter.convert;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@PreAuthorize("hasAnyRole('MANAGER', 'SPECIALIST')")
public class CompanyController {

	@Autowired
	private CompanyService companyService;
	
    @Autowired
    private AclService aclService;

    @Autowired
    private AclPermissionEvaluator aclPermissionEvaluator;
    
    @RequestMapping("/companies")
    public DataPageDto<CompanyDto> getCompanies(CompanyFilterDto filter) {
    	List<CompanyDto> companies = companyService.findCompanies(convert(filter));
    	DataPageDto<CompanyDto> result = new DataPageDto<CompanyDto>();
    	result.setData(companies);
    	result.setTotalCount(companies.size());
    	return result;
    }
    
    @RequestMapping(value ="/companies/{companyId}", method = RequestMethod.GET)
    public CompanyDto get(@PathVariable Long companyId) {
    	return companyService.get(companyId);
    }
    
    @PreAuthorize("hasPermission(#company.getId(), 'sample.Company', 'WRITE')")
    @RequestMapping(value = "/companies", method = PUT)
    public void update(@RequestBody CompanyDto company) {
    	companyService.updateCompany(company);
    }
    
    @RequestMapping(value = "/companies", method = POST)
    public Long create(@RequestBody CompanyDto company) {
    	return companyService.saveCompany(company);
    }
    
    @RequestMapping(value = "/companies/{companyId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long companyId) {
        companyService.deleteById(companyId);
    }
    
    @RequestMapping("/companies/company_types")
    public List<CompanyTypeDto> getCompanyTypes() {
    	return companyService.loadCompanyTypes();
    }
    
    @RequestMapping("/companies/business_spheres")
    public List<BusinessSphereDto> getBusinessSpheres() {
    	return companyService.loadBusinessSpheres();
    }
    
    @RequestMapping("/companies/employee_number_cathegories")
    public List<EmployeeNumberCathegoryDto> getEmployeeNumberCathegories() {
    	return companyService.loadEmployeeNumberCathegories();
    }
}
