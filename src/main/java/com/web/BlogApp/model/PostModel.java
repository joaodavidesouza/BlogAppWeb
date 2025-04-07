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

@Entity
@Table(name="TB_POST")
public class PostModel {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;

	@NotBlank
	private String autor;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate data;

	@NotBlank
	private String titulo;

	@NotBlank
	@Lob
	@Column(columnDefinition="text")
	private String texto;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostCommentModel> comments = new ArrayList<>();

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

	public void addComment(PostCommentModel comment) {
		comments.add(comment);
		comment.setPost(this);
	}

	public void removeComment(PostCommentModel comment) {
		comments.remove(comment);
		comment.setPost(null);
	}
}