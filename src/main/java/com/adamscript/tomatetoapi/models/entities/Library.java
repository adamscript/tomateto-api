package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "library")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String title;

    private String creator;

    /*@OneToMany
    private ArrayList<Rating> ratings = new ArrayList<>();*/

    private String cover;

    public Library(long id, String title, String creator, String cover){
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.cover = cover;
    }

}
