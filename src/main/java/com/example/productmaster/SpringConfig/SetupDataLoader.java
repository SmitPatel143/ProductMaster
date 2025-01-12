package com.example.productmaster.SpringConfig;

import com.example.productmaster.Entity.Privilege;
import com.example.productmaster.Entity.Role;
import com.example.productmaster.Repo.PrivilegeRepo;
import com.example.productmaster.Repo.RoleRepo;
import com.example.productmaster.Repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepo userRepo, RoleRepo roleRepo, PrivilegeRepo privilegeRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.privilegeRepo = privilegeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");
        final Privilege updatePrivilege = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        final Privilege addPrivilege = createPrivilegeIfNotFound("ADD_PRIVILEGE");

        final List<Privilege> adminPrivilege = new ArrayList<>(List.of(readPrivilege, writePrivilege, deletePrivilege, updatePrivilege, addPrivilege));
        final List<Privilege> userPrivilege = new ArrayList<>(List.of(readPrivilege, writePrivilege));

        final Role role = createRoleIfNotFound("ROLE_ADMIN",adminPrivilege);
        final Role role2 = createRoleIfNotFound("ROLE_USER",userPrivilege);

        alreadySetup = true;

    }

    @Transactional
    protected Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepo.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepo.save(privilege);
        }
        return privilege;
    }

    @Transactional
    protected Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepo.findByName(name);
        if (role == null) {
            role = new Role(name, privileges);
        }
        role.setPrivileges(privileges);
        roleRepo.save(role);
        return role;
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
