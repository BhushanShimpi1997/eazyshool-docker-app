package com.eazybytes.eazyschool.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private int courseId;

    @NotBlank(message = "Course Name Cann't be Blank !!")
    private String name;

    @NotBlank(message = "fees cann't be blank !!")
    private String fees;

    @ManyToMany(mappedBy = "courses",fetch = FetchType.EAGER,cascade = CascadeType.PERSIST,
                targetEntity = Person.class)
    private Set<Person>persons;
}
