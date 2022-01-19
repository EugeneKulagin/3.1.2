package ru.kata.spring.boot_security.demo.initialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entityes.Role;
import ru.kata.spring.boot_security.demo.entityes.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;

@Slf4j
@Component
@Transactional
public class DBInitialization {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DBInitialization(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        log.info("Начало инициализации базы данных...");

        if (roleService.findRoleByName("ROLE_ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleService.addRole(adminRole);
        }

        if (roleService.findRoleByName("ROLE_USER") == null) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleService.addRole(userRole);
        }

        if (userService.findUserByUsername("admin") == null) {
            User admin = new User();
            admin.setUserName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRoles(Collections.singleton(roleService.findRoleByName("ROLE_ADMIN")));
            userService.save(admin);
        }

        if (userService.findUserByUsername("user") == null) {
            User user = new User();
            user.setUserName("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.setRoles(Collections.singleton(roleService.findRoleByName("ROLE_USER")));
            userService.save(user);
        }

        log.info("База данных успешно инициализирована!");
    }
}