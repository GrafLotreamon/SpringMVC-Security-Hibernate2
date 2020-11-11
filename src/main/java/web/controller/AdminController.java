package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin")
    public String welcome() {
        return "redirect:/admin/all";
    }

    @GetMapping(value = "admin/all")
    public String allUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "allUsersPage";
    }

    @GetMapping(value = "admin/add")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "addUser";
    }

    @PostMapping(value = "admin/add")
    public String postAddUser(@ModelAttribute("user") User user,
                              @ModelAttribute("roleAdmin") String roleAdmin) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ROLE_USER"));
        if (roleAdmin.contains("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        user.setRoles(roles);
        userService.addUser(user);

        return "redirect:/admin";
    }


    @GetMapping(value = "admin/edit/{id}")
    public String editUser(ModelMap model, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        Set<Role> roles = user.getRoles();
        for (Role role: roles) {
            if (role.equals(roleService.getRoleByName("ROLE_ADMIN"))) {
                model.addAttribute("role", true);
            }
        }
        model.addAttribute("user", user);
        return "editUser";
    }
    @PostMapping(value = "admin/edit")
    public String postEditUser(@ModelAttribute("user") User user,
                               @ModelAttribute("roleAdmin") String roleAdmin) {

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ROLE_USER"));
        if (roleAdmin .contains("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        user.setRoles(roles);
        userService.editUser(user);
        return "redirect:/admin";
    }





//    //TODO переделать на post
//    @GetMapping("admin/delete/")
//    public String deleteUserById(@RequestParam("id") Long id) {
//        userService.deleteUser(id);
//        return "redirect:/admin";
//    }

    @RequestMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);

        return "redirect:/admin";
    }
}
