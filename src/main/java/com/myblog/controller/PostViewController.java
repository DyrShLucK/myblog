package com.myblog.controller;

import com.myblog.DTO.PostPageResponse;
import com.myblog.model.Comment;
import com.myblog.model.Post;
import com.myblog.service.CommentService;
import com.myblog.service.ImageStorageService;
import com.myblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
public class PostViewController {

    @Autowired
    private PostService postService;
    @Autowired
    public CommentService commentService;
    @Autowired
    public ImageStorageService imageStorageService;


    //Основная страница
    @GetMapping({"/posts", "", "/index", "/"})
    public String showPosts(
            @RequestParam(name = "search", defaultValue = "") String searchTag,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
            Model model) {

        PostPageResponse response = postService.getAllPostsWithPaginationAndTag(pageSize, pageNumber, searchTag);
        Map<Long, List<Comment>> commentsMap = commentService.countCommentsByPosts(response.posts());

        model.addAttribute("posts", response.posts());
        model.addAttribute("search", searchTag);
        model.addAttribute("paging", response.paging());
        model.addAttribute("comments", commentsMap);
        return "posts";
    }


    //Страница поста
    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPostById(id)
                .orElseThrow();
        List<Comment> comments = commentService.getAllCommentByPostId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);
        return "post";
    }

    //Отображение добавления поста
    @GetMapping("/posts/add")
    public String showAddForm(Model model) {
        System.out.println("Метод showAddForm вызван!"); // Для теста
        return "add-post";
    }
}
