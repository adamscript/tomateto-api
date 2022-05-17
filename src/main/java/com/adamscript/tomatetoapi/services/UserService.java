package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //fetch user information//
    public Optional<User> list(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return Optional.of(user.get());
        } else {
            System.out.println("User not found");
            return null;
        }
    }

    //register new user
    public Optional<User> insert(User user){
        Optional<User> insertedUser = userRepository.findByUsername(user.getUsername());

        if (insertedUser.isEmpty()) {
            return Optional.of(userRepository.save(user));
        }
        else {
            System.out.println("User already registered!");
            return null;
        }
    }

    //follow a user
    public Optional<User> follow(Long userFollowingId, Long userFollowedId) {
        //validate if user exists
        Optional<User> userFollowing = userRepository.findById(userFollowingId);
        Optional<User> userFollowed = userRepository.findById(userFollowedId);

        if(userFollowingId != userFollowedId && userFollowed.isPresent() && userFollowing.isPresent()){
            userRepository.followUser(userFollowingId, userFollowedId);
            return userRepository.findById(userFollowingId);
        }
        else{
            System.out.println("User does not exists or you are trying to follow yourself");
            return null;
        }
    }
}