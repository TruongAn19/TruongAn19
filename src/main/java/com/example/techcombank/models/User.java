package com.example.techcombank.models;



import com.example.techcombank.validator.constraint_group.AdvanceInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.util.Set;


@Entity
@Data
@Table (name = "users")

public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
//    @JsonIgnore
    private Long id;
    @NotBlank(groups = AdvanceInfo.class)
    private String userName;
    @NotBlank(groups = AdvanceInfo.class)
    private String password;
    @NotBlank(groups = AdvanceInfo.class)
    private String firstName;
    @NotBlank(groups = AdvanceInfo.class)
    private String lastName;
    @Lob
    private byte[] image;
    private Set<String> roles;

    public User() {
    }

    public User(String userName, String password, String firstName, String lastName, byte[] image, Set<String> roles) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.roles = roles;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", image='" + image + '\'' +
                ", roles=" + roles +
                '}';
    }

}
