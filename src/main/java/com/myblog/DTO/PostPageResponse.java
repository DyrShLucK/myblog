package com.myblog.DTO;

import com.myblog.model.Comment;
import com.myblog.model.Post;

import java.util.List;
import java.util.Map;

public record PostPageResponse(List<Post> posts, PagingInfo paging){

    public record PagingInfo(int pageNumber, int pageSize, int totalPages) {
        public boolean hasNext() {
            return pageNumber < totalPages;
        }
        public boolean hasPrevious() {
            return pageNumber > 1;
        }
    }

}