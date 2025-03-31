package com.web.BlogApp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.BlogApp.model.PostModel;
import com.web.BlogApp.repository.BlogAppRepository;

import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataUtil {

    @Autowired
    BlogAppRepository blogRepository;

    @PostConstruct // Uncommented to load sample data on startup
    public void savePosts(){
        // Only save sample data if the repository is empty
        if (blogRepository.count() == 0) {
            List<PostModel> postList = new ArrayList<>();

            PostModel post1 = new PostModel();
            post1.setAutor("Bruno Alexandre");
            post1.setData(LocalDate.now());
            post1.setTexto("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
            post1.setTitulo("Docker");

            PostModel post2 = new PostModel();
            post2.setAutor("Maicon Davi");
            post2.setData(LocalDate.now());
            post2.setTexto("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
            post2.setTitulo("API REST");

            postList.add(post1);
            postList.add(post2);

            for(PostModel post: postList){
                PostModel postSaved = blogRepository.save(post);
                System.out.println("Initialized sample post with ID: " + postSaved.getId());
            }
        }
    }
}