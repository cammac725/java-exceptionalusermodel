package com.lambdaschool.usermodel.controllers;

import com.lambdaschool.usermodel.models.ErrorDetail;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.services.RoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * The entry point for clients to access role data
 * <p>
 * Note: we cannot update a role
 * we cannot update a role
 * working with the "non-owner" object in a many to many relationship is messy
 * we will be fixing that!
 */
@RestController
@RequestMapping("/roles")
public class RolesController
{
    /**
     * Using the Role service to process Role data
     */
    @Autowired
    RoleService roleService;

    /**
     * List of all roles
     * <br>Example: <a href="http://localhost:2019/roles/roles">http://localhost:2019/roles/roles</a>
     *
     * @return JSON List of all the roles and their associated users
     * @see RoleService#findAll() RoleService.findAll()
     */
    @ApiOperation(value = "returns all roles",
        response = Role.class,
        responseContainer = "List")
    @GetMapping(value = "/roles",
            produces = "application/json")
    public ResponseEntity<?> listRoles()
    {
        List<Role> allRoles = roleService.findAll();
        return new ResponseEntity<>(allRoles,
                                    HttpStatus.OK);
    }

    /**
     * The Role referenced by the given primary key
     * <br>Example: <a href="http://localhost:2019/roles/role/3">http://localhost:2019/roles/role/3</a>
     *
     * @param roleId The primary key (long) of the role you seek
     * @return JSON object of the role you seek
     * @see RoleService#findRoleById(long) RoleService.findRoleById(long)
     */
    @ApiOperation(value = "retrieve a role based on a role id",
        response = Role.class)
    @ApiResponses(value = {
        @ApiResponse(
            code = 200,
            message = "Role found",
            response = Role.class),
        @ApiResponse(
            code = 404,
            message = "Role not found",
            response = ErrorDetail.class)
    })
    @GetMapping(value = "/role/{roleId}",
        produces = "application/json")
    public ResponseEntity<?> getRoleById(
        @ApiParam(value = "role id",
            required = true,
            example = "3")
        @PathVariable
                Long roleId)
    {
        Role r = roleService.findRoleById(roleId);
        return new ResponseEntity<>(r,
                                    HttpStatus.OK);
    }

    /**
     * The Role with the given name
     * <br>Example: <a href="http://localhost:2019/roles/role/name/data">http://localhost:2019/roles/role/name/data</a>
     *
     * @param roleName The name of the role you seek
     * @return JSON object of the role you seek
     * @see RoleService#findByName(String) RoleService.findByName(String)
     */
    @ApiOperation(value = "retrieve a role based on a role name",
            response = Role.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Role found",
                    response = Role.class),
            @ApiResponse(
                    code = 404,
                    message = "Role not found",
                    response = ErrorDetail.class)
    })
    @GetMapping(value = "/role/name/{roleName}",
            produces = "application/json")
    public ResponseEntity<?> getRoleByName(
            @ApiParam(value = "role name",
                required = true,
                example = "John Doe")
            @PathVariable
                    String roleName)
    {
        Role r = roleService.findByName(roleName);
        return new ResponseEntity<>(r,
                                    HttpStatus.OK);
    }

    /**
     * Given a complete Role object, create a new Role record
     * <br>Example: <a href="http://localhost:2019/roles/role">http://localhost:2019/roles/role</a>
     *
     * @param newRole A complete new Role object
     * @return A location header with the URI to the newly created role and a status of CREATED
     * @see RoleService#save(Role) RoleService.save(Role)
     */
    @PostMapping(value = "/role",
            consumes = "application/json")
    public ResponseEntity<?> addNewRole(
            @Valid
            @RequestBody
                    Role newRole)
    {
        // ids are not recognized by the Post method
        newRole.setRoleid(0);
        newRole = roleService.save(newRole);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newRoleURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{roleid}")
                .buildAndExpand(newRole.getRoleid())
                .toUri();
        responseHeaders.setLocation(newRoleURI);

        return new ResponseEntity<>(null,
                                    responseHeaders,
                                    HttpStatus.CREATED);
    }

    /**
     * The process allows you to update a role name only!
     * <br>Example: <a href="http://localhost:2019/roles/role/3">http://localhost:2019/roles/role/3</a>
     *
     * @param roleid  The primary key (long) of the role you wish to update
     * @param newRole The new name (String) for the role
     * @return Status of OK
     */
    @ApiOperation(value = "updates a role given in the request body",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Role found",
                    response = Void.class),
            @ApiResponse(
                    code = 404,
                    message = "Role not found",
                    response = ErrorDetail.class)
    })
    @PutMapping(value = "/role/{roleid}",
            consumes = {"application/json"})
    public ResponseEntity<?> putUpdateRole(
            @ApiParam(value = "a full role object",
                required = true)
            @Valid
            @RequestBody Role newRole,
            @ApiParam(value = "role id",
                required = true,
                example = "3")
            @PathVariable
                    long roleid)
    {
        newRole = roleService.update(roleid,
                                     newRole);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
