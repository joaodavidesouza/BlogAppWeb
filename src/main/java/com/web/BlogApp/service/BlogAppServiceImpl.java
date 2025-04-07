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

@Service
public class BlogAppServiceImpl implements BlogAppService {

	@Autowired
	BlogAppRepository blogapprepository;

	@Autowired
	PostCommentRepository commentRepository;

	@Override
	public List<PostModel> findAll() {
		return blogapprepository.findAll();
	}

	@Override
	public Optional<PostModel> findById(UUID id) {
		return blogapprepository.findById(id);
	}

	@Override
	public PostModel save(PostModel post) {
		return blogapprepository.save(post);
	}

	@Override
	@Transactional
	public void delete(PostModel post) {
		blogapprepository.delete(post);
	}

	// Comment related methods
	@Override
	public List<PostCommentModel> findAllComments() {
		return commentRepository.findAll();
	}

	@Override
	public List<PostCommentModel> findCommentsByPostId(UUID postId) {
		return commentRepository.findByPostId(postId);
	}

	@Override
	public Optional<PostCommentModel> findCommentById(UUID id) {
		return commentRepository.findById(id);
	}

	@Override
	public PostCommentModel saveComment(PostCommentModel comment) {
		return commentRepository.save(comment);
	}

	@Override
	@Transactional
	public void deleteComment(PostCommentModel comment) {
		commentRepository.delete(comment);
	}
}