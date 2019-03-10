package com.example.blog.model.controller;


import com.example.blog.model.Category;
import com.example.blog.model.Post;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    @Value("${image.upload.dir}")
    private String imageUploadDir;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/add")
    public String add(@ModelAttribute Post post, @RequestParam("picture") MultipartFile file) throws IOException {

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File picture = new File(imageUploadDir + File.separator + fileName);
        file.transferTo(picture);

        post.setPicUrl(fileName);
        post.setCreatedDate(new Date());
        postRepository.save(post);

        return "redirect:/";
    }

    @GetMapping("/getImage")
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(imageUploadDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping("/getPostById")
    public String getPostById(@RequestParam("id") int id, ModelMap map){

        Post one = postRepository.getOne(id);

        map.addAttribute("post",one);
        return "postDetail";
    }

    @GetMapping("/getPostByCategory")
    public String getPostByCatId(@RequestParam("id") int id, ModelMap map){

//        Category category = categoryRepository.getOne(id);
//        List<Post> posts = postRepository.findAll();
//        List<Post> getPosts = new ArrayList<>();
//        for (Post post: posts) {
//            for(Category cat : post.getCategories()){
//                if(cat == category){
//                    getPosts.add(post);
//                }
//            }
//        }
//
//        if(getPosts != null){
//            map.addAttribute("posts",getPosts);
//
//            return "postsByCategory";
//        }

        List<Post> allByCategoryId = postRepository.findAllByCategoriesId(id);
        map.addAttribute("posts",allByCategoryId);
        return "postsByCategory";
    }

    @GetMapping("/delete")
    public String deletePost(@RequestParam("id") int id){

       postRepository.deleteById(id);

        return "redirect:/";
    }

}
