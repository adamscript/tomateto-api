package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.helpers.handler.Response;
import com.adamscript.tomatetoapi.helpers.service.ServiceStatus;
import com.adamscript.tomatetoapi.models.dto.FeedPostDTO;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import com.adamscript.tomatetoapi.models.repos.FeedRepository;
import com.adamscript.tomatetoapi.models.repos.PostRepository;
import com.adamscript.tomatetoapi.models.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public Response listFeedPost(){
        List<FeedPostDTO> feedPosts = feedRepository.findBy();

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listFeedPostSortTop(){
        List<FeedPostDTO> feedPosts = feedRepository.findAllSort(Sort.by(Sort.Direction.DESC, "likesCount"));

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listFeedPostSortLatest(){
        List<FeedPostDTO> feedPosts = feedRepository.findAllSort(Sort.by(Sort.Direction.DESC, "date"));

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listFeedPostByFollow(String userId, Principal principal){
        System.out.println("Logged in " + principal);

        Optional<User> user = userRepository.findById(userId);

        List<FeedPostDTO> feedPosts = feedRepository.findByFollow(user);

        checkLikeOnFeedPost(feedPosts, user);

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public void checkLikeOnFeedPost(List<FeedPostDTO> feedPosts, Optional<User> user){
        //check if post is liked by current user
        for(int i = 0; i < feedPosts.size(); i++){
            List<Post> checkLike = postRepository.findLike(feedPosts.get(i).getId(), user);

            if(!checkLike.isEmpty()){
                feedPosts.get(i).setLiked(true);
            }
            else{
                feedPosts.get(i).setLiked(false);
            }
        }
    }

}
