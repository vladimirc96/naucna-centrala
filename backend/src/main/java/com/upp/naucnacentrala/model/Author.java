package com.upp.naucnacentrala.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AUTHOR")
public class Author extends User{
}
