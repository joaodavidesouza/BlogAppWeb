package com.web.BlogApp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidade JPA que representa um post de blog no sistema.
 *
 * Esta classe é mapeada para a tabela TB_POST no banco de dados e
 * mantém um relacionamento um-para-muitos com comentários (PostCommentModel).
 */
@Entity
@Table(name="TB_POST")
public class PostModel {

	/**
	 * Identificador único do post.
	 * Usa UUID em vez de ID sequencial para maior segurança e compatibilidade
	 * com sistemas distribuídos.
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;

	/**
	 * Nome do autor do post.
	 * Anotação @NotBlank garante que o campo não seja nulo ou vazio.
	 */
	@NotBlank
	private String autor;

	/**
	 * Data de criação do post.
	 * @JsonFormat define o formato para serialização/deserialização JSON.
	 */

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate data;

	/**
	 * Título do post.
	 * Anotação @NotBlank garante que o campo não seja nulo ou vazio.
	 */

	@NotBlank
	private String titulo;

	/**
	 * Conteúdo textual do post.
	 *
	 * Anotações:
	 * - @NotBlank: Garante que o campo não seja nulo ou vazio
	 * - @Lob: Indica que este campo deve ser armazenado como um objeto grande (LOB)
	 * - @Column(columnDefinition="text"): Define o tipo de coluna no banco como TEXT
	 *   para suportar conteúdos longos
	 */

	@NotBlank
	@Lob
	@Column(columnDefinition="text")
	private String texto;

	/**
	 * Lista de comentários associados a este post.
	 *
	 * Configuração:
	 * - mappedBy="post": Define que o relacionamento é gerenciado pelo campo "post" na entidade PostCommentModel
	 * - cascade=CascadeType.ALL: Propaga todas as operações (persist, remove, etc.) aos comentários filhos
	 * - orphanRemoval=true: Remove automaticamente comentários que não estão mais referenciados pelo post
	 */

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostCommentModel> comments = new ArrayList<>();

	// Getters e Setters

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public List<PostCommentModel> getComments() {
		return comments;
	}

	public void setComments(List<PostCommentModel> comments) {
		this.comments = comments;
	}

	/**
	 * Adiciona um comentário a este post.
	 * Também configura a referência bidirecional definindo este post no comentário.
	 *
	 * @param comment O comentário a ser adicionado
	 */

	public void addComment(PostCommentModel comment) {
		comments.add(comment);
		comment.setPost(this);
	}

	/**
	 * Remove um comentário deste post.
	 * Também limpa a referência bidirecional definindo null no campo post do comentário.
	 *
	 * @param comment O comentário a ser removido
	 */

	public void removeComment(PostCommentModel comment) {
		comments.remove(comment);
		comment.setPost(null);
	}
}