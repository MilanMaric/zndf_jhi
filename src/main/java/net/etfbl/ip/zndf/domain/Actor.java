/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Where;

/**
 *
 * @author milan
 */
@Entity(name = "jhi_actor")
@Where(clause = "active=1")
public class Actor extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "born_date", nullable = true)
    LocalDate bornDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "actor")
    @JsonIgnore
    Set<ActorRoles> actorRoles = new HashSet<>();

    @Column(name = "active")
    @JsonIgnore
    Boolean active = true;

    public Set<ActorRoles> getActorRoles() {
        return actorRoles;
    }

    public void setActorRoles(Set<ActorRoles> actorRoles) {
        this.actorRoles = actorRoles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getBornDate() {
        return bornDate;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    @Override
    public String toString() {
        return "Actor{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", bornDate=" + bornDate + '}';
    }

}
