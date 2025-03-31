package com.web.BlogApp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.web.BlogApp.model.PostModel;

public interface BlogAppService {

	List<PostModel> findAll();
	Optional<PostModel> findById(UUID id);
	PostModel save(PostModel post);
	void delete(PostModel post);

	// Removed all commented out methods related to PostComentarioModel
}