package com.adamscript.tomatetoapi.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "libraryId")
    private Library libraryId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User userId;

    private Integer rating;

    public Rating(Long id, Library libraryId, User userId, Integer rating){
        this.id = id;
        this.libraryId = libraryId;
        this.userId = userId;
        this.rating = rating;
    }

}
