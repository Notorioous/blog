package com.example.blog.model.controller;

import com.example.blog.model.Category;
import com.example.blog.model.Post;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String main( ModelMap map){

        List<Post> posts = postRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        map.addAttribute("posts",posts);
        map.addAttribute("categories",categories);
        return "home";
    }

    @GetMapping(  "/add")
    public String add(ModelMap map){

        List<Category> categories = categoryRepository.findAll();

        map.addAttribute("categories",categories);

        return "add";
    }

}
