package com.myblog.controller;

import com.myblog.model.Post;
import com.myblog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts") // Убираем {id} из пути
    public String viewPost(
            @RequestParam("id") Long id, // Используем @RequestParam
            Model model
    ) {
        return postService.getPostById(id)
                .map(post -> {
                    model.addAttribute("post", post);
                    return "post"; // Имя шаблона (post.html)
                })
                .orElse("redirect:/posts"); // Редирект, если пост не найден
    }
}