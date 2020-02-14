package com.upp.naucnacentrala.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User{
}
