package com.example.revamp.Service;


import com.example.revamp.Model.Role;
import com.example.revamp.Model.User;

import java.util.List;


public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String role);
    User getUser(String username);
    List<User> getUsers();
}
