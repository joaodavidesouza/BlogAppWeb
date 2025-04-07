package com.web.BlogApp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.BlogApp.dtos.BlogAppRecordDto;
import com.web.BlogApp.dtos.CommentDto;
import com.web.BlogApp.model.PostCommentModel;
import com.web.BlogApp.model.PostModel;
import com.web.BlogApp.service.BlogAppService;

import jakarta.validation.Valid;

@Controller
public class BlogAppController {

	@Autowired
	BlogAppService blogappservice;

	// Root path redirection
	@GetMapping("/")
	public String rootRedirect() {
		return "redirect:/posts";
	}

	// Login page
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// List all posts
	@GetMapping(value = "/posts")
	public ModelAndView getPosts() {
		ModelAndView mv = new ModelAndView("posts");
		List<PostModel> posts = blogappservice.findAll();
		mv.addObject("posts", posts);
		return mv;
	}

	// Show post details
	@GetMapping(value = "/posts/{id}")
	public ModelAndView getPostDetails(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
		Optional<PostModel> post = blogappservice.findById(id);

		if (post.isPresent()) {
			ModelAndView mv = new ModelAndView("postDetails");
			mv.addObject("post", post.get());
			mv.addObject("commentDto", new CommentDto(null, null));
			return mv;
		} else {
			// Handle case when post doesn't exist
			redirectAttributes.addFlashAttribute("error", "Post not found!");
			return new ModelAndView("redirect:/posts");
		}
	}

	// Prepare form for new post
	@GetMapping(value = "/newpost")
	public String getPostForm(Model model) {
		model.addAttribute("postDto", new BlogAppRecordDto(null, null, null));
		return "newpostForm";
	}

	// Save new post
	@PostMapping(value = "/newpost")
	public String savePost(@Valid @ModelAttribute("postDto") BlogAppRecordDto postDto,
						   BindingResult bindingResult,
						   RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "newpostForm";
		}

		PostModel post = new PostModel();
		post.setAutor(postDto.autor());
		post.setTitulo(postDto.titulo());
		post.setTexto(postDto.texto());
		post.setData(LocalDate.now());

		blogappservice.save(post);

		redirectAttributes.addFlashAttribute("message", "Post created successfully!");
		return "redirect:/posts";
	}

	// Delete post
	@GetMapping("/posts/delete/{id}")
	public String deletePost(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
		Optional<PostModel> post = blogappservice.findById(id);

		if (post.isPresent()) {
			blogappservice.delete(post.get());
			redirectAttributes.addFlashAttribute("message", "Post deleted successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Post not found!");
		}

		return "redirect:/posts";
	}

	// Show edit form
	@GetMapping("/posts/edit/{id}")
	public String getEditPostForm(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
		Optional<PostModel> post = blogappservice.findById(id);

		if (post.isPresent()) {
			BlogAppRecordDto postDto = new BlogAppRecordDto(
					post.get().getAutor(),
					post.get().getTitulo(),
					post.get().getTexto()
			);
			model.addAttribute("postDto", postDto);
			model.addAttribute("postId", id);
			return "editpostForm";
		} else {
			redirectAttributes.addFlashAttribute("error", "Post not found!");
			return "redirect:/posts";
		}
	}

	// Update post
	@PostMapping("/posts/edit/{id}")
	public String updatePost(@PathVariable UUID id,
							 @Valid @ModelAttribute("postDto") BlogAppRecordDto postDto,
							 BindingResult bindingResult,
							 RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "editpostForm";
		}

		Optional<PostModel> existingPost = blogappservice.findById(id);

		if (existingPost.isPresent()) {
			PostModel post = existingPost.get();
			post.setAutor(postDto.autor());
			post.setTitulo(postDto.titulo());
			post.setTexto(postDto.texto());
			// Keep the original date

			blogappservice.save(post);
			redirectAttributes.addFlashAttribute("message", "Post updated successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Post not found!");
		}

		return "redirect:/posts";
	}

	// Add a comment to a post
	@PostMapping("/posts/{postId}/comments")
	public String addComment(@PathVariable UUID postId,
							 @Valid @ModelAttribute("commentDto") CommentDto commentDto,
							 BindingResult bindingResult,
							 RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "redirect:/posts/" + postId;
		}

		Optional<PostModel> post = blogappservice.findById(postId);

		if (post.isPresent()) {
			PostCommentModel comment = new PostCommentModel();
			comment.setAuthor(commentDto.author());
			comment.setContent(commentDto.content());
			comment.setDateTime(LocalDateTime.now());
			comment.setPost(post.get());

			blogappservice.saveComment(comment);
			redirectAttributes.addFlashAttribute("message", "Comment added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Post not found!");
		}

		return "redirect:/posts/" + postId;
	}

	// Show edit comment form
	@GetMapping("/posts/{postId}/comments/{commentId}/edit")
	public String getEditCommentForm(@PathVariable UUID postId,
									 @PathVariable UUID commentId,
									 Model model,
									 RedirectAttributes redirectAttributes) {

		Optional<PostCommentModel> comment = blogappservice.findCommentById(commentId);

		if (comment.isPresent() && comment.get().getPost().getId().equals(postId)) {
			CommentDto commentDto = new CommentDto(
					comment.get().getAuthor(),
					comment.get().getContent()
			);
			model.addAttribute("commentDto", commentDto);
			model.addAttribute("postId", postId);
			model.addAttribute("commentId", commentId);
			return "editCommentForm";
		} else {
			redirectAttributes.addFlashAttribute("error", "Comment not found!");
			return "redirect:/posts/" + postId;
		}
	}

	// Update comment
	@PostMapping("/posts/{postId}/comments/{commentId}")
	public String updateComment(@PathVariable UUID postId,
								@PathVariable UUID commentId,
								@Valid @ModelAttribute("commentDto") CommentDto commentDto,
								BindingResult bindingResult,
								RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "editCommentForm";
		}

		Optional<PostCommentModel> existingComment = blogappservice.findCommentById(commentId);

		if (existingComment.isPresent() && existingComment.get().getPost().getId().equals(postId)) {
			PostCommentModel comment = existingComment.get();
			comment.setAuthor(commentDto.author());
			comment.setContent(commentDto.content());
			// Keep the original date and post

			blogappservice.saveComment(comment);
			redirectAttributes.addFlashAttribute("message", "Comment updated successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Comment not found!");
		}

		return "redirect:/posts/" + postId;
	}

	// Delete comment
	@GetMapping("/posts/{postId}/comments/{commentId}/delete")
	public String deleteComment(@PathVariable UUID postId,
								@PathVariable UUID commentId,
								RedirectAttributes redirectAttributes) {

		Optional<PostCommentModel> comment = blogappservice.findCommentById(commentId);

		if (comment.isPresent() && comment.get().getPost().getId().equals(postId)) {
			blogappservice.deleteComment(comment.get());
			redirectAttributes.addFlashAttribute("message", "Comment deleted successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Comment not found!");
		}

		return "redirect:/posts/" + postId;
	}
}