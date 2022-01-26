package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepo extends JpaRepository<Post, Long> {


}
