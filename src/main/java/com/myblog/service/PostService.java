package com.myblog.service;

import com.myblog.DTO.PostPageResponse;
import com.myblog.controller.PostViewController;
import com.myblog.model.Comment;
import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "all_posts", allEntries = true),
                    @CacheEvict(value = "posts_by_tag", allEntries = true),
                    @CacheEvict(value = "post", key = "#id")
            }
    )
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
    @Cacheable("all_posts")
    public PostPageResponse getAllPostsWithPaginationAndTag(int pageSize, int pageNumber, String searchTag) {
        List<Post> posts;
        int totalPosts;
        if (searchTag.isEmpty()) {
            posts = postRepository.findAllWithPagnation(pageSize, pageNumber);
            totalPosts = postRepository.getTotalPostsCount();
        } else {
            posts = postRepository.findAllWithPagnationAndTag(pageSize, pageNumber, searchTag);
            totalPosts = postRepository.getTotalPostsCountByTag(searchTag);
        }

        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);
        PostPageResponse.PagingInfo paging = new PostPageResponse.PagingInfo(
                pageNumber,
                pageSize,
                totalPages
        );
        return new PostPageResponse(posts, paging);
    }
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTag(tag);
    }

    public void incrementPostLikes(Long postId) {
        postRepository.incrementLikes(postId);
    }
    public void decrementLikes(Long postId){
        postRepository.decrementLikes(postId);
    }

    public Optional<Integer> getLikesCount(Long postId) {
        return postRepository.getLikesCount(postId);
    }

    @Cacheable(value = "post", key = "#id")
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @CachePut(value = "post", key = "#post.id")
    public void  updatePost(Post post){
        postRepository.UpdatePost(post);
    }

}