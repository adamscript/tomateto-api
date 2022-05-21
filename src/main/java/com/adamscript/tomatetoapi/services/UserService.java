package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //fetch user information//
    public Response list(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return new Response(user, ServiceStatus.SUCCESS);
        }
        else if (user.isEmpty()){
            return new Response(null, ServiceStatus.USER_NOT_FOUND);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //register new user
    public Response insert(User user){
        Optional<User> insertedUser = userRepository.findByUsername(user.getUsername());

        if(user.getUsername() == null){
            return new Response(null, ServiceStatus.USERNAME_EMPTY);
        }
        else if(user.getDisplayName() == null){
            return new Response(null, ServiceStatus.DISPLAYNAME_EMPTY);
        }
        else if (insertedUser.isPresent()){
            return new Response(null, ServiceStatus.USERNAME_ALREADY_EXIST);
        }
        else if (insertedUser.isEmpty()) {
            User savedUser = userRepository.save(user);
            return new Response(savedUser, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    public Response edit(User user){
        Optional<User> insertedUser = userRepository.findById(user.getId());

        if(user.getUsername() == null){
            return new Response(null, ServiceStatus.USERNAME_EMPTY);
        }
        else if(user.getDisplayName() == null){
            return new Response(null, ServiceStatus.DISPLAYNAME_EMPTY);
        }
        else if (insertedUser.isEmpty()){
            return new Response(null, ServiceStatus.USER_DOES_NOT_EXIST);
        }
        else if (insertedUser.isPresent()) {
            //maintain user info before saving the update
            user.setDate(insertedUser.get().getDate());
            user.setFollowCount(insertedUser.get().getFollowCount());
            user.setFollow(insertedUser.get().getFollow());
            user.setFollowersCount(insertedUser.get().getFollowersCount());
            user.setFollowers(insertedUser.get().getFollowers());
            user.setPostsCount(insertedUser.get().getPostsCount());
            user.setPost(insertedUser.get().getPost());
            user.setLikedPosts(insertedUser.get().getLikedPosts());
            user.setComment(insertedUser.get().getComment());
            user.setLikedComments(insertedUser.get().getLikedComments());

            User savedUser = userRepository.save(user);
            return new Response(savedUser, ServiceStatus.SUCCESS);
        }
        else {
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //follow a user
    public Response follow(Long userFollowingId, Long userFollowedId) {
        //validate if user exists
        Optional<User> userFollowing = userRepository.findById(userFollowingId);
        Optional<User> userFollowed = userRepository.findById(userFollowedId);

        List<User> userFollow = userRepository.findFollow(userFollowingId, userFollowed);

        if(userFollowingId == userFollowedId){
            return new Response(null, ServiceStatus.FOLLOW_YOURSELF);
        }
        else if (userFollowed.isEmpty()){
            return new Response(null, ServiceStatus.FOLLOW_DOES_NOT_EXIST);
        }
        else if (userFollowing.isEmpty()){
            return new Response(null, ServiceStatus.FOLLOWER_DOES_NOT_EXIST);
        }
        else if(!userFollow.isEmpty()){
            return new Response(null, ServiceStatus.FOLLOWED_ALREADY);
        }
        else if(userFollowingId != userFollowedId && userFollowed.isPresent() && userFollowing.isPresent()){
            userRepository.followUser(userFollowingId, userFollowedId);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    //unfollow a user
    public Response unfollow(Long userFollowingId, Long userFollowedId) {
        Optional<User> userFollowed = userRepository.findById(userFollowedId);

        List<User> userFollow = userRepository.findFollow(userFollowingId, userFollowed);

        if(userFollow.isEmpty()){
            return new Response(null, ServiceStatus.NOT_FOLLOWED);
        }
        else if(!userFollow.isEmpty()){
            userRepository.unfollowUser(userFollowingId, userFollowedId);
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }
}