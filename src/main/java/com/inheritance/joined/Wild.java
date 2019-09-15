package com.inheritance.joined;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "wild")
@PrimaryKeyJoinColumn(name = "wild_id")
public class Wild extends Animal {
}
