package com.web.BlogApp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.BlogApp.model.PostModel;
import com.web.BlogApp.repository.BlogAppRepository;

import jakarta.transaction.Transactional;

@Service
public class BlogAppServiceImpl implements BlogAppService {

	@Autowired
	BlogAppRepository blogapprepository;

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

	// Removed all commented out code related to PostComentarioModel
}