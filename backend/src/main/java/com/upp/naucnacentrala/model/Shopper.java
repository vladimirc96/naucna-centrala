package com.upp.naucnacentrala.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SHOPPER")
public class Shopper extends User{
}
