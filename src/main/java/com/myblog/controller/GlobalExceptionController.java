package com.myblog.controller;

import com.myblog.excepetion.CommentNotFoundExcepetion;
import com.myblog.excepetion.PostNotFoundException;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(PostNotFoundException.class)
    public ModelAndView ErrorPostNotFount(PostNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
    @ExceptionHandler(CommentNotFoundExcepetion.class)
    public ModelAndView ErrorCommentNotFount(CommentNotFoundExcepetion e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView GlobalError(Exception e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Непредвиденная ошибка: " + e.getMessage());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }
}
