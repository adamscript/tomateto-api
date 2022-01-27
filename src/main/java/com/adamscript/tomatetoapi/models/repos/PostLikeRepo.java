package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.entities.PostLike;
import org.springframework.data.repository.CrudRepository;

public interface PostLikeRepo extends CrudRepository<PostLike, Long> {
}
