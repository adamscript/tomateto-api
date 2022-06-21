package com.adamscript.tomatetoapi.services;

import com.adamscript.tomatetoapi.models.dto.FeedPostDTO;
import com.adamscript.tomatetoapi.models.dto.FeedUserDTO;
import com.adamscript.tomatetoapi.models.entities.Post;
import com.adamscript.tomatetoapi.models.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final EntityManager em;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initiateIndexing() throws InterruptedException{
        log.info("Initiating search indexing...");

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        fullTextEntityManager.createIndexer().startAndWait();

        log.info("Search index initiated");
    }

    public List<FeedPostDTO> getPostByKeyword(String keyword){

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);

        QueryBuilder qb = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Post.class)
                .get();

        Query query = qb.keyword()
                .onField("content")
                .matching(keyword)
                .createQuery();

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Post.class);
        fullTextQuery.setProjection("id", "user", "content", "photo", "date", "likesCount", "commentsCount", "isEdited");

        return getPostList(fullTextQuery.getResultList());

    }

    public List<FeedUserDTO> getUserByKeyword(String keyword){

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);

        QueryBuilder qb = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(User.class)
                .get();

        Query query = qb.keyword()
                .onFields("username", "displayName", "bio")
                .matching(keyword)
                .createQuery();

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, User.class);
        fullTextQuery.setProjection("id", "displayName", "username", "bio", "avatar");

        return getUserList(fullTextQuery.getResultList());

    }

    private List<FeedPostDTO> getPostList(List<Object[]> posts){
        List<FeedPostDTO> postList = new ArrayList<>();
        for(Object[] objects : posts){
            FeedPostDTO feedPostDTO = new FeedPostDTO(
                    (Long) objects[0],
                    (String) objects[2],
                    (String) objects[3],
                    (Instant) objects[4],
                    (Long) objects[5],
                    (Long) objects[6],
                    (User) objects[1],
                    (Boolean) objects[7]);
            postList.add(feedPostDTO);
        }
        return postList;
    }

    private List<FeedUserDTO> getUserList(List<Object[]> users){
        List<FeedUserDTO> userList = new ArrayList<>();
        for(Object[] objects : users){
            FeedUserDTO feedUserDTO = new FeedUserDTO(
                    (String) objects[0],
                    (String) objects[1],
                    (String) objects[2],
                    (String) objects[3],
                    (String) objects[4]);
            userList.add(feedUserDTO);
        }
        return userList;
    }

}
