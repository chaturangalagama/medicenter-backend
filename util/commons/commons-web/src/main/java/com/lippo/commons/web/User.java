package com.lippo.commons.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class User {

    private String message;
    private Payload payload;

    public Payload newPayload() {
        return new Payload();
    }

    public class Payload {

        private String userName;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private String status;
        private String address;
        private Date expiryDate;
        private int numberOfLoginAttempts;
        private List<String> roles;
        private Map<String, String> context;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Date getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(Date expiryDate) {
            this.expiryDate = expiryDate;
        }

        public int getNumberOfLoginAttempts() {
            return numberOfLoginAttempts;
        }

        public void setNumberOfLoginAttempts(int numberOfLoginAttempts) {
            this.numberOfLoginAttempts = numberOfLoginAttempts;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public Map<String, String> getContext() {
            return context;
        }

        public void setContext(Map<String, String> context) {
            this.context = context;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
