package de.holarse.backend.db;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "user_slugs")
@Entity
public class UserSlug extends TimestampedBase implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userid", nullable=false, referencedColumnName = "id")
    private User user;
    
    private String name;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
