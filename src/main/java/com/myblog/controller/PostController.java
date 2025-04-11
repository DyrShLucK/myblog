package com.myblog.controller;

import com.myblog.model.Comment;
import com.myblog.model.Post;
import com.myblog.service.CommentService;
import com.myblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    public CommentService commentService;

    @GetMapping("/posts")
    public String showPosts(
            @RequestParam(name = "search", defaultValue = "") String searchTag,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
            Model model) {
        //запрос по параметрам поиска
        List<Post> posts = searchTag.isEmpty()
                ? postService.getAllPosts()
                : postService.getPostsByTag(searchTag);
        //сортировка(сначала новые)
        posts.sort(Comparator.comparing(Post::getCreated_at).reversed());

        //пагинация
        int totalPosts = posts.size();
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);
        pageNumber = Math.max(1, Math.min(pageNumber, totalPages));

        List<Post> paginatedPosts = List.of();
        if (totalPosts > 0) {
            int fromIndex = (pageNumber - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalPosts);
            paginatedPosts = posts.subList(fromIndex, toIndex);
        }

        //кол-во комментариев
        Map<Long, List<Comment>> commentsMap = new HashMap<>();
        for (Post post : posts) {
            List<Comment> comments = commentService.getAllCommentByPostId(post.getId());
            commentsMap.put(post.getId(), comments);
        }
        // Передача данных в шаблон
        model.addAttribute("posts", paginatedPosts);
        model.addAttribute("search", searchTag);
        model.addAttribute("paging", new PagingInfo(pageNumber, pageSize, totalPages));
        model.addAttribute("comments", commentsMap);
        return "posts";
    }

    // Вспомогательный record для фильтров
    private record PagingInfo(int pageNumber, int pageSize, int totalPages) {
        public boolean hasNext() { return pageNumber < totalPages; }
        public boolean hasPrevious() { return pageNumber > 1; }
    }
}