package web.repository;

import web.model.Role;

import java.util.List;

public interface RoleRepo {
    Role getRoleByName(String name);

    Role getRoleById(long id);

    List<Role> allRoles();

    Role getDefaultRole();
}
