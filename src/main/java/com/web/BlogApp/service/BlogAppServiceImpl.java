package com.web.BlogApp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.BlogApp.model.PostCommentModel;
import com.web.BlogApp.model.PostModel;
import com.web.BlogApp.repository.BlogAppRepository;
import com.web.BlogApp.repository.PostCommentRepository;

import jakarta.transaction.Transactional;

/**
 * Implementação da interface BlogAppService que fornece a lógica de negócios
 * para operações de posts e comentários.
 *
 * Esta classe atua como intermediária entre os controladores e os repositórios,
 * encapsulando a lógica de acesso a dados e regras de negócio.
 */
@Service
public class BlogAppServiceImpl implements BlogAppService {

	/**
	 * Repositório para operações CRUD de posts.
	 * Injetado automaticamente pelo Spring.
	 */
	@Autowired
	BlogAppRepository blogapprepository;

	/**
	 * Repositório para operações CRUD de comentários.
	 * Injetado automaticamente pelo Spring.
	 */
	@Autowired
	PostCommentRepository commentRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostModel> findAll() {
		return blogapprepository.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<PostModel> findById(UUID id) {
		return blogapprepository.findById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostModel save(PostModel post) {
		return blogapprepository.save(post);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @Transactional garante que a operação seja executada dentro de uma transação,
	 * garantindo atomicidade e que todas as alterações sejam confirmadas ou revertidas
	 * como uma única unidade.
	 */
	@Override
	@Transactional
	public void delete(PostModel post) {
		blogapprepository.delete(post);
	}

	// Implementações de métodos relacionados a comentários

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostCommentModel> findAllComments() {
		return commentRepository.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostCommentModel> findCommentsByPostId(UUID postId) {
		return commentRepository.findByPostId(postId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<PostCommentModel> findCommentById(UUID id) {
		return commentRepository.findById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostCommentModel saveComment(PostCommentModel comment) {
		return commentRepository.save(comment);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @Transactional garante que a operação seja executada dentro de uma transação.
	 */
	@Override
	@Transactional
	public void deleteComment(PostCommentModel comment) {
		commentRepository.delete(comment);
	}
}