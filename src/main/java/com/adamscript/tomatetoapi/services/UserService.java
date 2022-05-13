package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //fetch post content data//
    public Optional<User> list(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return Optional.of(user.get());
        } else {
            System.out.println("User not found");
            return null;
        }
    }
}