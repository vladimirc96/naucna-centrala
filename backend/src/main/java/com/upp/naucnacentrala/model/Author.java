package com.upp.naucnacentrala.model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("AUTHOR")
public class Author extends User{

}
