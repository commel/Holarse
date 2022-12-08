package de.holarse.backend.db;

import de.holarse.backend.types.PasswordType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

@Table(name = "users")
@Entity

public class User extends Base implements Serializable  {
    
    private String login;
    private String email;
    private String slug;
    private Integer drupalId;
    @Enumerated(EnumType.STRING)
    @Type(type = "com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType")
    private PasswordType hashType;
    private String digest;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getDrupalId() {
        return drupalId;
    }

    public void setDrupalId(Integer drupalId) {
        this.drupalId = drupalId;
    }

    public PasswordType getHashType() {
        return hashType;
    }

    public void setHashType(PasswordType hashType) {
        this.hashType = hashType;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
    
}
