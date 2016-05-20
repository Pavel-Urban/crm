package com.itechart.security.web.controller;

import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.User;
import com.itechart.security.service.GroupService;
import com.itechart.security.service.UserService;
import com.itechart.security.model.dto.GroupDto;
import com.itechart.security.web.model.dto.PublicGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import static com.itechart.security.web.model.dto.Converter.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author yauheni.putsykovich
 */
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @RequestMapping("/groups")
    public List<GroupDto> getGroups() {
        return groupService.getGroups();
    }

    @RequestMapping(value = "/groups/{id}", method = GET)
    public GroupDto  get(@PathVariable Long id) {
        return convertGroupWithUsers(groupService.getGroupWithUsers(id));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/groups/public")
    public List<PublicGroupDto> getPublicGroups() {
        return convertToPublicGroups(groupService.getGroups());
    }

    @RequestMapping(value = "/groups", method = POST)
    public Serializable create(@RequestBody GroupDto group) {
        return groupService.create(convert(group));
    }

    @RequestMapping(value = "/groups", method = PUT)
    public void update(@RequestBody GroupDto dto) {
        groupService.update(convert(dto));
    }

    @RequestMapping(value = "/groups/{id}", method = DELETE)
    public void delete(@PathVariable Long id) {
        Group group = groupService.getGroupWithUsers(id);
        Set<User> users = group.getUsers();
        users.forEach(user -> {
            user.removeFromGroup(group);
            userService.updateUser(user);
        });
        groupService.deleteById(id);
    }
}
