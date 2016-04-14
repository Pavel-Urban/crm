package com.itechart.security.web.controller;

import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.service.ContactService;
import com.itechart.security.core.SecurityUtils;
import com.itechart.security.core.acl.AclPermissionEvaluator;
import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.service.AclService;
import com.itechart.security.service.PrincipalService;
import com.itechart.security.web.model.PrincipalTypes;
import com.itechart.security.web.model.dto.AclEntryDto;
import com.itechart.security.web.model.dto.ContactDto;
import com.itechart.security.web.model.dto.ContactFilterDto;
import com.itechart.security.web.model.dto.DataPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.itechart.security.core.model.acl.Permission.*;
import static com.itechart.security.web.model.dto.Converter.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author yauheni.putsykovich
 */
@RestController
@PreAuthorize("hasAnyRole('MANAGER', 'SPECIALIST')")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private AclService aclService;

    @Autowired
    private AclPermissionEvaluator aclPermissionEvaluator;

    @RequestMapping("/contacts/{contactId}/actions/{value}")
    public boolean isAllowed(@PathVariable Long contactId, @PathVariable String value) {
        Permission permission = Permission.valueOf(value.toUpperCase());
        return aclPermissionEvaluator.hasPermission(SecurityUtils.getAuthentication(), createIdentity(contactId), permission);
    }

    @RequestMapping("/contacts")
    public List<ContactDto> getContacts() {
        return convertContacts(contactService.getContacts());
    }

    @RequestMapping(value = "/contacts/{contactId}", method = RequestMethod.GET)
    public ContactDto get(@PathVariable Long contactId) {
        return convert(contactService.get(contactId));
    }

    @PreAuthorize("hasPermission(#dto.getId(), 'sample.Contact', 'WRITE')")
    @RequestMapping(value = "/contacts", method = PUT)
    public void update(@RequestBody ContactDto dto) {
        contactService.updateContact(covert(dto));
    }

    @RequestMapping("/contacts/find")
    public DataPageDto find(ContactFilterDto filterDto) {
        ContactFilter filter = convert(filterDto);
        DataPageDto<ContactDto> page = new DataPageDto<>();
        page.setData(convertContacts(contactService.findContacts(filter)));
        page.setTotalCount(contactService.countContacts(filter));
        return page;
    }

    @RequestMapping(value = "/contacts", method = POST)
    public Long create(@RequestBody ContactDto dto) {
        Long contactId = contactService.saveContact(covert(dto));
        Long userId = SecurityUtils.getAuthenticatedUserId();
        aclService.createAcl(new ObjectIdentityImpl(contactId, ObjectTypes.CONTACT.getName()), null, userId);
        return contactId;
    }

    @RequestMapping(value = "/contacts/{contactId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void delete(@PathVariable Long contactId) {
        Acl acl = getAcl(contactId);
        aclService.deleteAcl(acl);
        contactService.deleteById(contactId);
    }

    @RequestMapping("/contacts/{contactId}/permissions")
    public List<AclEntryDto> getPermissions(@PathVariable Long contactId) {
        Acl acl = getAcl(contactId);
        Map<Long, Set<Permission>> allPermissions = acl.getPermissions();
        List<Principal> principals = principalService.getByIds(new ArrayList<>(allPermissions.keySet()));
        return principals.stream().map(principal -> {
            AclEntryDto dto = new AclEntryDto();
            dto.setId(principal.getId());
            dto.setName(principal.getName());
            allPermissions.get(principal.getId()).forEach(permission -> {
                dto.setCanRead(permission == READ);
                dto.setCanWrite(permission == WRITE);
                dto.setCanCreate(permission == CREATE);
                dto.setCanDelete(permission == DELETE);
                dto.setCanAdmin(permission == ADMIN);
            });
            if (principal instanceof User) {
                dto.setPrincipalTypeName(PrincipalTypes.USER.getObjectType());
            } else if (principal instanceof Group) {
                dto.setPrincipalTypeName(PrincipalTypes.GROUP.getObjectType());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/contacts/{contactId}/permissions", method = PUT)
    public void createOrUpdatePermissions(@PathVariable Long contactId, @RequestBody List<AclEntryDto> permissions) {
        Acl acl = getAcl(contactId);
        permissions.forEach(permission -> {
            boolean isItNewPrincipal = acl.getPermissions(permission.getId()) == null;
            if (isItNewPrincipal) acl.addPermissions(permission.getId(), Collections.emptySet());

            acl.denyAll(permission.getId());
            if (permission.isCanRead()) acl.addPermission(permission.getId(), READ);
            if (permission.isCanWrite()) acl.addPermission(permission.getId(), WRITE);
            if (permission.isCanCreate()) acl.addPermission(permission.getId(), CREATE);
            if (permission.isCanDelete()) acl.addPermission(permission.getId(), DELETE);
            if (permission.isCanAdmin()) acl.addPermission(permission.getId(), ADMIN);
        });
        aclService.updateAcl(acl);
    }

    @RequestMapping(value = "/contacts/{contactId}/permissions/{principalId}", method = RequestMethod.DELETE)
    public void deletePermission(@PathVariable Long contactId, @PathVariable Long principalId) {
        Acl acl = getAcl(contactId);
        acl.removePrincipal(principalId);
        aclService.updateAcl(acl);
    }

    private Acl getAcl(Long contactId) {
        return aclService.getAcl(createIdentity(contactId));
    }

    private ObjectIdentity createIdentity(Long contactId) {
        return new ObjectIdentityImpl(contactId, ObjectTypes.CONTACT.getName());
    }
}