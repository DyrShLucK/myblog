package com.myblog.controller;

import com.myblog.excepetion.CommentNotFoundExcepetion;
import com.myblog.excepetion.PostNotFoundException;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(PostNotFoundException.class)
    public ModelAndView ErrorPostNotFount(PostNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
    @ExceptionHandler(CommentNotFoundExcepetion.class)
    public ModelAndView ErrorCommentNotFount(CommentNotFoundExcepetion e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView GlobalError(Exception e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Непредвиденная ошибка: " + e.getMessage());
        modelAndView.addObject("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFound(NoHandlerFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Страница не найдена: " + ex.getRequestURL());
        modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
