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

    @Autowired
    private SearchService searchService;

    public Response listFeedPost(Principal principal){
        List<FeedPostDTO> feedPosts = feedRepository.findBy();
        setPrincipalProperties(feedPosts, principal);

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listFeedPostSortTop(Principal principal){
        List<FeedPostDTO> feedPosts = feedRepository.findAllSort(Sort.by(Sort.Direction.DESC, "likesCount"));
        setPrincipalProperties(feedPosts, principal);

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listFeedPostSortLatest(Principal principal){
        List<FeedPostDTO> feedPosts = feedRepository.findAllSort(Sort.by(Sort.Direction.DESC, "date"));
        setPrincipalProperties(feedPosts, principal);

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listFeedPostByFollow(Principal principal){
        Optional<User> user = userRepository.findById(principal.getName());
        List<FeedPostDTO> feedPosts = feedRepository.findByFollow(user);

        setPrincipalProperties(feedPosts, principal);

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    public Response listByKeyword(String query, Principal principal){
        List<FeedPostDTO> feedPosts = searchService.getPostByKeyword(query);

        setPrincipalProperties(feedPosts, principal);

        return new Response(feedPosts, ServiceStatus.SUCCESS);
    }

    private void checkLikeOnFeedPost(List<FeedPostDTO> feedPosts, Optional<User> user){
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

    private void checkMineOnFeedPost(List<FeedPostDTO> feedPosts, Optional<User> user){
        //check if post is liked by current user
        for(int i = 0; i < feedPosts.size(); i++){
            if(feedPosts.get(i).getUser().get("id").equals(user.get().getId())){
                feedPosts.get(i).setMine(true);
            }
            else{
                feedPosts.get(i).setMine(false);
            }
        }
    }

    private void setPrincipalProperties(List<FeedPostDTO> feedPosts, Principal principal){
        if(principal != null){
            Optional<User> user = userRepository.findById(principal.getName());
            checkLikeOnFeedPost(feedPosts, user);
            checkMineOnFeedPost(feedPosts, user);
        }
    }

}
