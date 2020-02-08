package com.upp.naucnacentrala.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("AUTHOR")
public class Author extends User{

    @ManyToOne
    private Magazine magazine;

}
