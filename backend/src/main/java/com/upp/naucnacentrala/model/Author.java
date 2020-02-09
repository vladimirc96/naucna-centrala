package com.upp.naucnacentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("AUTHOR")
public class Author extends User{

    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Magazine> magazines;

}
