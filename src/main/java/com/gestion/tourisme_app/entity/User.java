package com.gestion.tourisme_app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
public class User {
    @Id
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String profileImageUrl;
    private Role role = Role.USER;
    private Boolean isActive = true;
    private Boolean isEmailVerified = false;
    private String emailVerificationToken;
    private String passwordResetToken;
    private LocalDateTime passwordResetExpires;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    public User() {
        super();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    public User(Long id, String fullName, String email, String password, String phoneNumber, String profileImageUrl, Role role, Boolean isActive, Boolean isEmailVerified, String emailVerificationToken, String passwordResetToken, LocalDateTime passwordResetExpires, LocalDateTime createdDate, LocalDateTime updatedDate, List<CartItem> cartItems, List<Order> orders) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.isActive = isActive;
        this.isEmailVerified = isEmailVerified;
        this.emailVerificationToken = emailVerificationToken;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetExpires = passwordResetExpires;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.cartItems = cartItems;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetExpires() {
        return passwordResetExpires;
    }

    public void setPasswordResetExpires(LocalDateTime passwordResetExpires) {
        this.passwordResetExpires = passwordResetExpires;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public enum Role {
        USER, ADMIN
    }
}
