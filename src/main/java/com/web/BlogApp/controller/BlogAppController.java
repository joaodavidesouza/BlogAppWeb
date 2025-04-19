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

/**
 * Controlador principal da aplicação BlogApp.
 *
 * Este controlador gerencia todas as requisições HTTP relacionadas a posts e comentários,
 * implementando o padrão MVC (Model-View-Controller) para separar a lógica de apresentação
 * da lógica de negócios.
 */
@Controller
public class BlogAppController {

	/**
	 * O serviço que contém a lógica de negócios para posts e comentários.
	 * Injetado automaticamente pelo Spring através da anotação @Autowired.
	 */
	@Autowired
	BlogAppService blogappservice;

	/**
	 * Redireciona o caminho raiz para a listagem de posts.
	 * Esta é uma prática comum para não deixar a URL raiz sem conteúdo.
	 *
	 * @return String de redirecionamento para o endpoint de listagem de posts
	 */
	@GetMapping("/")
	public String rootRedirect() {
		return "redirect:/posts";
	}

	/**
	 * Exibe a página de login personalizada.
	 *
	 * @return Nome da view de login que será renderizada
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	/**
	 * Lista todos os posts do blog.
	 * Este método:
	 * 1. Recupera todos os posts do serviço
	 * 2. Adiciona os posts ao modelo
	 * 3. Retorna o ModelAndView que combina dados e template
	 *
	 * @return ModelAndView contendo a lista de posts e a view a ser renderizada
	 */
	@GetMapping(value = "/posts")
	public ModelAndView getPosts() {
		ModelAndView mv = new ModelAndView("posts");
		List<PostModel> posts = blogappservice.findAll();
		mv.addObject("posts", posts);
		return mv;
	}

	/**
	 * Exibe os detalhes de um post específico, incluindo seus comentários.
	 *
	 * Este método:
	 * 1. Busca o post pelo ID fornecido
	 * 2. Se encontrado, prepara a view de detalhes com o post e um DTO vazio para novos comentários
	 * 3. Se não encontrado, redireciona para a lista de posts com mensagem de erro
	 *
	 * @param id O UUID do post a ser exibido
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @return ModelAndView com detalhes do post ou redirecionamento se não encontrado
	 */
	@GetMapping(value = "/posts/{id}")
	public ModelAndView getPostDetails(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
		Optional<PostModel> post = blogappservice.findById(id);

		if (post.isPresent()) {
			ModelAndView mv = new ModelAndView("postDetails");
			mv.addObject("post", post.get());
			mv.addObject("commentDto", new CommentDto(null, null));
			return mv;
		} else {
			// Tratamento para caso de post não encontrado
			redirectAttributes.addFlashAttribute("error", "Post not found!");
			return new ModelAndView("redirect:/posts");
		}
	}

	/**
	 * Exibe o formulário para criação de um novo post.
	 *
	 * Este método adiciona um DTO vazio ao modelo para ser preenchido pelo formulário.
	 * Utiliza o padrão DTO (Data Transfer Object) para separar os dados do formulário
	 * da entidade de domínio.
	 *
	 * @param model Modelo para adicionar o DTO vazio
	 * @return Nome da view do formulário a ser renderizada
	 */
	@GetMapping(value = "/newpost")
	public String getPostForm(Model model) {
		model.addAttribute("postDto", new BlogAppRecordDto(null, null, null));
		return "newpostForm";
	}

	/**
	 * Salva um novo post a partir dos dados do formulário.
	 *
	 * Este método:
	 * 1. Valida os dados do post usando as anotações de validação do Jakarta
	 * 2. Se inválido, retorna ao formulário mantendo os dados inseridos
	 * 3. Se válido, converte o DTO para uma entidade e salva através do serviço
	 *
	 * Utiliza o padrão PRG (Post-Redirect-Get) para evitar reenvios de formulário
	 * acidentais ao atualizar a página.
	 *
	 * @param postDto DTO contendo os dados do formulário
	 * @param bindingResult Resultados da validação do formulário
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @return String indicando redirecionamento ou nome da view se houver erros
	 */
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

	/**
	 * Remove um post específico do blog.
	 *
	 * Este método:
	 * 1. Busca o post pelo ID fornecido
	 * 2. Se encontrado, exclui o post e redireciona com mensagem de sucesso
	 * 3. Se não encontrado, redireciona com mensagem de erro
	 *
	 * @param id O UUID do post a ser excluído
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @return String de redirecionamento para a listagem de posts
	 */
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

	/**
	 * Exibe o formulário para edição de um post existente.
	 *
	 * Este método:
	 * 1. Busca o post pelo ID fornecido
	 * 2. Se encontrado, prepara o formulário com os dados atuais do post
	 * 3. Se não encontrado, redireciona para a lista de posts com mensagem de erro
	 *
	 * @param id O UUID do post a ser editado
	 * @param model Modelo para adicionar os dados do post e seu ID
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @return Nome da view do formulário ou string de redirecionamento se não encontrado
	 */
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

	/**
	 * Atualiza um post existente com os dados do formulário.
	 *
	 * Este método:
	 * 1. Valida os dados do post
	 * 2. Se inválido, retorna ao formulário mantendo os dados inseridos
	 * 3. Se válido, busca o post existente e atualiza apenas os campos editáveis
	 *    (mantendo a data original e outros campos que não devem ser alterados)
	 *
	 * @param id O UUID do post a ser atualizado
	 * @param postDto DTO contendo os dados do formulário
	 * @param bindingResult Resultados da validação do formulário
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @return String indicando redirecionamento ou nome da view se houver erros
	 */
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
			// Mantém a data original

			blogappservice.save(post);
			redirectAttributes.addFlashAttribute("message", "Post updated successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Post not found!");
		}

		return "redirect:/posts";
	}

	/**
	 * Adiciona um comentário a um post específico.
	 *
	 * Este método:
	 * 1. Valida os dados do comentário
	 * 2. Se inválido, retorna para o formulário com erros
	 * 3. Se válido, salva o comentário e redireciona para a página de detalhes do post
	 *
	 * Utiliza o padrão PRG (Post-Redirect-Get) para evitar reenvios de formulário
	 * acidentais ao atualizar a página.
	 *
	 * @param postId ID do post ao qual adicionar o comentário
	 * @param commentDto DTO com dados do comentário a ser adicionado
	 * @param bindingResult Resultados da validação do formulário
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @param model Modelo para adicionar atributos quando retornando à mesma página
	 * @return String indicando redirecionamento ou nome da view
	 */
	@PostMapping("/posts/{postId}/comments")
	public String addComment(@PathVariable UUID postId,
							 @Valid @ModelAttribute("commentDto") CommentDto commentDto,
							 BindingResult bindingResult,
							 RedirectAttributes redirectAttributes,
							 Model model) {

		// Tratamento de erros de validação
		if (bindingResult.hasErrors()) {
			Optional<PostModel> post = blogappservice.findById(postId);
			if (post.isPresent()) {
				model.addAttribute("post", post.get());
				model.addAttribute("commentDto", commentDto);
				model.addAttribute("error", "Please fill out all comment fields");
				return "postDetails";
			}
			return "redirect:/posts/" + postId;
		}

		Optional<PostModel> post = blogappservice.findById(postId);

		if (post.isPresent()) {
			// Criação e configuração do objeto de comentário
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

		// Redirecionamento seguindo o padrão PRG (Post-Redirect-Get)
		return "redirect:/posts/" + postId;
	}

	/**
	 * Exibe o formulário para edição de um comentário existente.
	 *
	 * Este método:
	 * 1. Busca o comentário pelo ID fornecido
	 * 2. Verifica se o comentário existe e pertence ao post especificado
	 * 3. Se encontrado, prepara o formulário com os dados atuais do comentário
	 * 4. Se não encontrado ou inválido, redireciona com mensagem de erro
	 *
	 * @param postId O UUID do post ao qual o comentário pertence
	 * @param commentId O UUID do comentário a ser editado
	 * @param model Modelo para adicionar os dados do comentário e IDs
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @return Nome da view do formulário ou string de redirecionamento se não encontrado
	 */
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

	/**
	 * Atualiza um comentário existente com os dados do formulário.
	 *
	 * Este método:
	 * 1. Valida os dados do comentário
	 * 2. Se inválido, retorna ao formulário mantendo os dados inseridos
	 * 3. Se válido, busca o comentário existente e atualiza apenas os campos editáveis
	 *    (mantendo a data original e a referência ao post)
	 * 4. Verifica se o comentário realmente pertence ao post especificado
	 *
	 * @param postId O UUID do post ao qual o comentário pertence
	 * @param commentId O UUID do comentário a ser atualizado
	 * @param commentDto DTO contendo os dados do formulário
	 * @param bindingResult Resultados da validação do formulário
	 * @param model Modelo para adicionar atributos quando retornando à mesma página
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @return String indicando redirecionamento ou nome da view se houver erros
	 */
	@PostMapping("/posts/{postId}/comments/{commentId}")
	public String updateComment(@PathVariable UUID postId,
								@PathVariable UUID commentId,
								@Valid @ModelAttribute("commentDto") CommentDto commentDto,
								BindingResult bindingResult,
								Model model,
								RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("postId", postId);
			model.addAttribute("commentId", commentId);
			return "editCommentForm";
		}

		Optional<PostCommentModel> existingComment = blogappservice.findCommentById(commentId);

		if (existingComment.isPresent() && existingComment.get().getPost().getId().equals(postId)) {
			PostCommentModel comment = existingComment.get();
			comment.setAuthor(commentDto.author());
			comment.setContent(commentDto.content());
			// Mantém a data original e a referência ao post

			blogappservice.saveComment(comment);
			redirectAttributes.addFlashAttribute("message", "Comment updated successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Comment not found!");
		}

		return "redirect:/posts/" + postId;
	}

	/**
	 * Remove um comentário específico de um post.
	 *
	 * Este método:
	 * 1. Busca o comentário pelo ID fornecido
	 * 2. Verifica se o comentário existe e pertence ao post especificado
	 * 3. Se encontrado e válido, exclui o comentário
	 * 4. Redireciona para a página de detalhes do post com mensagem apropriada
	 *
	 * @param postId O UUID do post ao qual o comentário pertence
	 * @param commentId O UUID do comentário a ser excluído
	 * @param redirectAttributes Atributos para mensagens flash após redirecionamento
	 * @return String de redirecionamento para a página de detalhes do post
	 */
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