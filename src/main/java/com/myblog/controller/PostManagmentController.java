package com.myblog.controller;

import com.myblog.model.Post;
import com.myblog.service.ImageStorageService;
import com.myblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostManagmentController {
    @Autowired
    public ImageStorageService imageStorageService;
    @Autowired
    private PostService postService;

    //Добавление поста
    @PostMapping("/posts")
    public String addPost(@RequestParam("title") String title,
                          @RequestParam("text") String text,
                          @RequestParam("tags") String tags,
                          @RequestPart("image") MultipartFile image){
        Post post = new Post();
        String imageUrl = "images/No_image";

        post.setTitle(title);
        post.setText(text);
        post.setTags(tags);
        post.setImage(imageUrl);
        post = postService.createPost(post);
        imageUrl = imageStorageService.savePostImage(post.getId(), image);
        post.setImage(imageUrl);
        postService.updatePost(post);
        return "redirect:/posts";
    }

    //Лайк/Дизлайк
    @PostMapping("posts/{postId}/like")
    public String LikeControll(@PathVariable Long postId,
                               @RequestParam("like") Boolean like){
        if(like){
            postService.incrementPostLikes(postId);
        }else {
            postService.decrementLikes(postId);
        }

        return "redirect:/posts/" +postId;
    }

    //Удаление поста
    @PostMapping("posts/{postId}/delete")
    public String DeletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return "redirect:/posts";
    }

    //редактирование поста
    @GetMapping("posts/{postId}/edit")
    public String EditPost(@PathVariable Long postId,
                           Model model){
        model.addAttribute("post", postService.getPostById(postId).get());
        return "add-post";
    }

    //обновление поста
    @PostMapping("posts/{postId}")
    public  String updatePost(@PathVariable Long postId,
                              @RequestParam("title") String title,
                              @RequestParam("text") String text,
                              @RequestParam("tags") String tags,
                              @RequestPart("image")MultipartFile image){
        Post post = postService.getPostById(postId).orElseThrow(() -> new RuntimeException("Пост не найден"));
        String imageUrl = post.getImage();
        imageStorageService.updatePostImage(postId, image,imageUrl);
        post.setTitle(title);
        post.setText(text);
        post.setTags(tags);
        post.setImage(imageUrl);
        postService.updatePost(post);
        return "redirect:/posts/" + postId;
    }
}
