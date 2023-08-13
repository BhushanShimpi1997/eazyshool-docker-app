package com.eazybytes.eazyschool.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "contact_msg")
public class Contact extends BaseEntity{
    /*
   * @NotNull: Checks if a given field is not null but allows empty values & zero elements inside collections.
     @NotEmpty: Checks if a given field is not null and its size/length is greater than zero.
     @NotBlank: Checks if a given field is not null and trimmed length is greater than zero.
   * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native",strategy = "native")
    @Column(name = "contact_id")
    private int contactId;

  //  @JsonProperty("person_name")
    @NotBlank(message = "Name must not be blank")
    @Size(min = 3,message = "Name must contains atleast 3 char")
    private String name;

    @NotBlank(message = "Mobile number must not be blank")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits long")
    private String mobileNum;

    @NotBlank(message = "Email Must not blank")
    @Email(message = "please provide a valid email address")
    private String email;

    @NotBlank(message = "Subject must not blank")
    @Size(min = 5,message = "Subject must 5 chars long")
    private String subject;

    @NotBlank(message = "message must not blank")
    @Size(min= 8,message = "message must be 8 chars long")
    private String message;

    private String status;


}
