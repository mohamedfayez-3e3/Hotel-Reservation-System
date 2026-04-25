package models;

import java.time.LocalDate;
import enums.Role;
import exceptions.InvalidDataException;

public abstract class Staff {
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private Role role;
    private int workingHours;

    public Staff() {
    }

    public Staff(String username, String password, LocalDate dateOfBirth, Role role, int workingHours) {
        setUsername(username);
        setPassword(password);
        setDateOfBirth(dateOfBirth);
        setRole(role);
        setWorkingHours(workingHours);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidDataException("Staff username cannot be empty.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidDataException("Staff password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new InvalidDataException("Staff password must be at least 6 characters.");
        }
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new InvalidDataException("Staff date of birth cannot be null.");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidDataException("Staff date of birth cannot be in the future.");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new InvalidDataException("Staff role cannot be null.");
        }
        this.role = role;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        if (workingHours <= 0) {
            throw new InvalidDataException("Working hours must be greater than 0.");
        }
        this.workingHours = workingHours;
    }

    public void printStaffInfo() {
        System.out.println("Staff Information:");
        System.out.println("Username: " + username);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Role: " + role);
        System.out.println("Working Hours: " + workingHours);
    }
}