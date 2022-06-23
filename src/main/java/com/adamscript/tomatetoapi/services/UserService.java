package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.dto.*;
import com.adamscript.tomatetoapi.models.entities.Comment;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.CommentRepository;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    private SearchService searchService;

    //fetch user information//
    public Response list(String id) {
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
    public Response insert(User user, Principal principal){
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
            user.setId(principal.getName());
            User savedUser = userRepository.save(user);

            return new Response(savedUser, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    public Response edit(User user, Principal principal){
        Optional<User> insertedUser = userRepository.findById(principal.getName());

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
            user.setId(principal.getName());
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
    public Response follow(String userFollowedId, Principal principal) {
        String userFollowingId = principal.getName();

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
    public Response unfollow(String userFollowedId, Principal principal) {
        String userFollowingId = principal.getName();

        Optional<User> userFollowed = userRepository.findById(userFollowedId);

        List<User> userFollow = userRepository.findFollow(userFollowingId, userFollowed);

        if(userFollow.isEmpty()){
            return new Response(null, ServiceStatus.NOT_FOLLOWED);
        }
        else if(!userFollow.isEmpty()){
            userRepository.unfollowUser(userFollowingId, userFollowedId);
            setFollowCount(userRepository.findById(userFollowingId).get());
            return new Response(null, ServiceStatus.SUCCESS);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    private void setFollowCount(User user){
        user.setFollowCount(userRepository.findFollows(user).size());
        user.setFollowersCount(userRepository.findFollowers(user).size());
        userRepository.save(user);
    }

    //----------------------------------------------------------------//

    public Response listFollows(String id, Principal principal){
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            List<FeedUserDTO> userFollows = userRepository.findFollows(user.get());
            setPrincipalPropertiesOnFeedUser(userFollows, principal);

            return new Response(userFollows, ServiceStatus.SUCCESS);
        }
        else if(user.isEmpty()){
            return new Response(null, ServiceStatus.USER_DOES_NOT_EXIST);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }

    }

    public Response listFollowers(String id, Principal principal){
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            List<FeedUserDTO> userFollowers = userRepository.findFollowers(user.get());
            setPrincipalPropertiesOnFeedUser(userFollowers, principal);

            return new Response(userFollowers, ServiceStatus.SUCCESS);
        }
        else if(user.isEmpty()){
            return new Response(null, ServiceStatus.USER_DOES_NOT_EXIST);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    public Response listNonFollows(Principal principal){
        Optional<User> user = userRepository.findById(principal.getName());

        if(user.isPresent()){
            List<FeedUserDTO> userFollows = userRepository.findNonFollows(user);
            setPrincipalPropertiesOnFeedUser(userFollows, principal);

            return new Response(userFollows, ServiceStatus.SUCCESS);
        }
        else if(user.isEmpty()){
            return new Response(null, ServiceStatus.USER_DOES_NOT_EXIST);
        }
        else{
            return new Response(null, ServiceStatus.ERROR);
        }
    }

    public Response listAll(Principal principal){
        List<FeedUserDTO> allUsers = userRepository.findAllUsers();
        setPrincipalPropertiesOnFeedUser(allUsers, principal);

        return new Response(allUsers, ServiceStatus.SUCCESS);
    }

    public Response listProfile(String username, Principal principal){
        Optional<UserDetailDTO> user = userRepository.findProfileByUsername(username);
        setPrincipalPropertiesOnUser(user.get(), principal);

        return new Response(user, ServiceStatus.SUCCESS);
    }

    public Response listProfilePost(String id, Principal principal){
        User user = userRepository.findById(id).get();
        List<FeedPostDTO> feedPosts = userRepository.findPostByUser(user, Sort.by(Sort.Direction.DESC, "date"));
        setPrincipalPropertiesOnFeedPost(feedPosts, principal);

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listProfileComment(String id, Principal principal){
        User user = userRepository.findById(id).get();
        List<FeedCommentDTO> feedComments = userRepository.findCommentByUser(user, Sort.by(Sort.Direction.DESC, "date"));
        setPrincipalPropertiesOnFeedComment(feedComments, principal);

        return new Response(feedComments, ServiceStatus.SUCCESS);
    }

    public Response listProfileLiked(String id, Principal principal){
        User user = userRepository.findById(id).get();
        List<FeedPostDTO> feedPosts = userRepository.findLikedByUser(user, Sort.by(Sort.Direction.DESC, "date"));
        setPrincipalPropertiesOnFeedPost(feedPosts, principal);

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listByKeyword(String query, Principal principal){
        List<FeedUserDTO> feedUsers = searchService.getUserByKeyword(query);

        setPrincipalPropertiesOnFeedUser(feedUsers, principal);

        return new Response(feedUsers, ServiceStatus.SUCCESS);
    }

    private void setPrincipalPropertiesOnUser(UserDetailDTO user, Principal principal){
        if(principal != null){
            List<User> checkFollow = userRepository.findFollow(principal.getName(), userRepository.findById(user.getId()));

            if(user.getId().equals(principal.getName())){
                user.setMine(true);
            }
            else{
                user.setMine(false);
            }

            if(!checkFollow.isEmpty()){
                user.setIsFollowed(true);
            }
            else{
                user.setIsFollowed(false);
            }
        }
    }

    private void setPrincipalPropertiesOnFeedUser(List<FeedUserDTO> feedUser, Principal principal){
        if(principal != null){
            //check if post is liked by current user
            for(int i = 0; i < feedUser.size(); i++){
                if(feedUser.get(i).getId().equals(principal.getName())){
                    feedUser.get(i).setMine(true);
                }
                else{
                    feedUser.get(i).setMine(false);
                }
            }

            //check if post is liked by current user
            for(int i = 0; i < feedUser.size(); i++){
                List<User> checkFollow = userRepository.findFollow(principal.getName(), userRepository.findById(feedUser.get(i).getId()));

                if(!checkFollow.isEmpty()){
                    feedUser.get(i).setIsFollowed(true);
                }
                else{
                    feedUser.get(i).setIsFollowed(false);
                }
            }
        }
    }

    private void setPrincipalPropertiesOnFeedPost(List<FeedPostDTO> feedPost, Principal principal){
        if(principal != null){
            Optional<User> user = userRepository.findById(principal.getName());

            //check if post is liked by current user
            for(int i = 0; i < feedPost.size(); i++){
                if(feedPost.get(i).getUser().get("id").equals(principal.getName())){
                    feedPost.get(i).setMine(true);
                }
                else{
                    feedPost.get(i).setMine(false);
                }
            }

            //check if post is liked by current user
            for(int i = 0; i < feedPost.size(); i++){
                List<Post> checkLike = postRepository.findLike(feedPost.get(i).getId(), user);

                if(!checkLike.isEmpty()){
                    feedPost.get(i).setLiked(true);
                }
                else{
                    feedPost.get(i).setLiked(false);
                }
            }
        }
    }

    private void setPrincipalPropertiesOnFeedComment(List<FeedCommentDTO> feedComment, Principal principal){
        if(principal != null){
            Optional<User> user = userRepository.findById(principal.getName());

            //check if post is liked by current user
            for(int i = 0; i < feedComment.size(); i++){
                if(feedComment.get(i).getUser().get("id").equals(principal.getName())){
                    feedComment.get(i).setMine(true);
                }
                else{
                    feedComment.get(i).setMine(false);
                }
            }

            //check if post is liked by current user
            for(int i = 0; i < feedComment.size(); i++){
                List<Comment> checkLike = commentRepository.findLike(feedComment.get(i).getId(), user);

                if(!checkLike.isEmpty()){
                    feedComment.get(i).setLiked(true);
                }
                else{
                    feedComment.get(i).setLiked(false);
                }
            }
        }
    }
}