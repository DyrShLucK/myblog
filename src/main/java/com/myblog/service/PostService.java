package com.myblog.service;

import com.myblog.DTO.PostPageResponse;
import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    @Caching(
            put = {
                    @CachePut(value = "postById", key = "#post.id"),
                    @CachePut(value = "allPosts", key = "#post.id") // Если нужно
            }
    )
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "allPosts", allEntries = true),
                    @CacheEvict(value = "postById", key = "#postId")
            }
    )
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

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

    @Cacheable(value = "allPosts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Cacheable(value = "postsByTag", key = "#tag")
    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTag(tag);
    }

    @Caching(
            put = {
                    @CachePut(value = "likesCount", key = "#postId"),
                    @CachePut(value = "postById", key = "#postId")
            }
    )
    public void incrementPostLikes(Long postId) {
        postRepository.incrementLikes(postId);
    }

    @Caching(
            put = {
                    @CachePut(value = "likesCount", key = "#postId"),
                    @CachePut(value = "postById", key = "#postId")
            }
    )
    public void decrementLikes(Long postId) {
        postRepository.decrementLikes(postId);
    }

    @Cacheable(value = "likesCount", key = "#postId")
    public Optional<Integer> getLikesCount(Long postId) {
        return postRepository.getLikesCount(postId);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
    @Caching(
            evict = {
                    @CacheEvict(value = "allPosts", allEntries = true),
                    @CacheEvict(value = "postById", key = "#post.id")
            }
    )
    public void updatePost(Post post) {
        postRepository.UpdatePost(post);
    }
}