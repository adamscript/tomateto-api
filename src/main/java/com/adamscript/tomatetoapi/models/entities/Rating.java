package com.adamscript.tomatetoapi.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "rating_tbl")
public class Rating {

    @Id
    private String ratingId;

    @OneToOne
    private Library libraryId;

    @OneToOne
    private User userId;

    private Integer rating;

    public Rating(Library libraryId, User userId, Integer rating){
        this.ratingId = UUID.randomUUID().toString();
        this.libraryId = libraryId;
        this.userId = userId;
        this.rating = rating;
    }

}
