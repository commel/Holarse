package de.holarse.backend.db;

import java.time.OffsetDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

@Entity(name = "users")
public class User extends Base {
    
    @Column(unique = true)
    private String login;
    
    @Enumerated(EnumType.STRING)
    private PasswordType passwordType;
    
    private String digest;
    
    private String email;
    
    @Column(columnDefinition = "boolean default false")
    private boolean locked;

    @Column(columnDefinition = "boolean default false")
    private boolean verified;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    
    private String verificationKey;
    private OffsetDateTime verificationValidUntil;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public PasswordType getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(PasswordType passwordType) {
        this.passwordType = passwordType;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerificationKey() {
        return verificationKey;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }

    public OffsetDateTime getVerificationValidUntil() {
        return verificationValidUntil;
    }

    public void setVerificationValidUntil(OffsetDateTime verificationValidUntil) {
        this.verificationValidUntil = verificationValidUntil;
    }
    
    @Override
    public String toString() {
        return "User{" + "login=" + login + ", passwordType=" + passwordType + ", digest=" + digest + ", locked=" + locked + ", roles=" + roles + '}';
    }
    
}
