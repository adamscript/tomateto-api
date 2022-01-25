package com.adamscript.tomatetoapi.models.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "library_tbl")
public class Library {

    @Id
    private String libraryId;

    @NotNull
    private String title;

    private String creator;

    @OneToMany
    private ArrayList<Rating> ratings = new ArrayList<>();

    private String cover;

    public Library(String title, String creator, String cover){
        this.libraryId = UUID.randomUUID().toString();
        this.title = title;
        this.creator = creator;
        this.cover = cover;
    }

}
