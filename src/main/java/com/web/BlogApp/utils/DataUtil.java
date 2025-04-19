package com.web.BlogApp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.BlogApp.model.PostModel;
import com.web.BlogApp.repository.BlogAppRepository;

import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para inicialização de dados de exemplo na aplicação.
 *
 * Esta classe usa o método @PostConstruct para garantir que os dados de exemplo
 * sejam carregados após a inicialização dos beans Spring, mas antes que a aplicação
 * esteja disponível para receber requisições.
 */
@Component
public class DataUtil {

    /**
     * Repositório de posts injetado para permitir a criação de dados iniciais.
     */
    @Autowired
    BlogAppRepository blogRepository;

    /**
     * Método que cria posts de exemplo se o banco de dados estiver vazio.
     *
     * Esta abordagem é útil para:
     * 1. Ter dados disponíveis para demonstração assim que a aplicação iniciar
     * 2. Evitar duplicação de dados de exemplo a cada reinicialização
     *
     * A anotação @PostConstruct garante que este método seja executado
     * após a inicialização completa do bean e suas dependências.
     */
    @PostConstruct
    public void savePosts(){
        // Verifica se já existem posts para evitar duplicação
        if (blogRepository.count() == 0) {
            List<PostModel> postList = new ArrayList<>();

            // Criação do primeiro post de exemplo
            PostModel post1 = new PostModel();
            post1.setAutor("Bruno Alexandre");
            post1.setData(LocalDate.now());
            post1.setTexto("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
            post1.setTitulo("Docker");

            // Criação do segundo post de exemplo
            PostModel post2 = new PostModel();
            post2.setAutor("Maicon Davi");
            post2.setData(LocalDate.now());
            post2.setTexto("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
            post2.setTitulo("API REST");

            postList.add(post1);
            postList.add(post2);

            // Salva cada post e registra o ID gerado no log
            for(PostModel post: postList){
                PostModel postSaved = blogRepository.save(post);
                System.out.println("Initialized sample post with ID: " + postSaved.getId());
            }
        }
    }
}