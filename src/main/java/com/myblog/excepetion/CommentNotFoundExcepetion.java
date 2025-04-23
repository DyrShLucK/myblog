package com.myblog.excepetion;

public class CommentNotFoundExcepetion extends RuntimeException{
    public CommentNotFoundExcepetion(Long commentId){
        super("Комментарий с ID " + commentId + " не найден");
    }
}
