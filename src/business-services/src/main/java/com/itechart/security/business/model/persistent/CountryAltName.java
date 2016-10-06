package com.itechart.security.business.model.persistent;

/**
 * Created by pavel.urban on 10/6/2016.
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "country_altname")
public class CountryAltName extends SecuredEntity{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String altname;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Override
    public Long getId() {
        return id;
    }

}
