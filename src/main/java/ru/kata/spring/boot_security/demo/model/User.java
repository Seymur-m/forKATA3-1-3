package ru.kata.spring.boot_security.demo.model;

import javax.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String username;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Role> roles;

    public User() {}

    public User(String name, String email, List<Role> roles, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public void setUser(User user) {
        this.name = user.name;
        this.email = user.email;
        this.username = user.username;
        this.password = user.password;
        this.roles.get(0).setRoleName((user.getRoles().get(0).getRoleName()));
    }

    public void setUserWithoutRoles(User user) {
        this.name = user.name;
        this.email = user.email;
        this.username = user.username;
        this.password = user.password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void clearAndSetRoles(Role role) {
        roles.clear();
        roles.add(role);
    }

    public void clearAndSetRoles(List<Role> roles) {
        roles.clear();
        this.roles.addAll(roles);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", UserName: " + username + ", Password: " + password;
    }
}
