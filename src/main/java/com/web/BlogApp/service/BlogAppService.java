package com.web.BlogApp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.web.BlogApp.model.PostCommentModel;
import com.web.BlogApp.model.PostModel;

/**
 * Interface de serviço que define as operações disponíveis para gerenciamento de posts e comentários.
 *
 * Esta interface segue o padrão de design de interface de serviço, que:
 * 1. Separa a definição das operações de sua implementação
 * 2. Facilita a substituição da implementação (útil para mocks em testes)
 * 3. Estabelece um contrato claro das operações disponíveis
 */
public interface BlogAppService {

	/**
	 * Recupera todos os posts do blog.
	 *
	 * @return Lista de todos os posts ordenados conforme definido no repositório
	 */
	List<PostModel> findAll();

	/**
	 * Busca um post específico pelo seu identificador único.
	 *
	 * @param id O UUID do post a ser encontrado
	 * @return Um Optional contendo o post se encontrado, ou vazio se não existir
	 */
	Optional<PostModel> findById(UUID id);

	/**
	 * Salva ou atualiza um post no banco de dados.
	 *
	 * @param post O objeto post a ser salvo ou atualizado
	 * @return O post salvo/atualizado com possíveis campos gerados (como ID)
	 */
	PostModel save(PostModel post);

	/**
	 * Remove um post do banco de dados.
	 * Devido à configuração cascade, todos os comentários relacionados também serão removidos.
	 *
	 * @param post O post a ser excluído
	 */
	void delete(PostModel post);

	// Métodos relacionados a comentários

	/**
	 * Recupera todos os comentários do sistema.
	 *
	 * @return Lista de todos os comentários
	 */
	List<PostCommentModel> findAllComments();

	/**
	 * Busca comentários associados a um post específico.
	 *
	 * @param postId O UUID do post
	 * @return Lista de comentários do post especificado
	 */
	List<PostCommentModel> findCommentsByPostId(UUID postId);

	/**
	 * Busca um comentário específico pelo seu identificador único.
	 *
	 * @param id O UUID do comentário
	 * @return Um Optional contendo o comentário se encontrado, ou vazio se não existir
	 */
	Optional<PostCommentModel> findCommentById(UUID id);

	/**
	 * Salva ou atualiza um comentário no banco de dados.
	 *
	 * @param comment O objeto comentário a ser salvo ou atualizado
	 * @return O comentário salvo/atualizado com possíveis campos gerados
	 */
	PostCommentModel saveComment(PostCommentModel comment);

	/**
	 * Remove um comentário do banco de dados.
	 *
	 * @param comment O comentário a ser excluído
	 */

	void deleteComment(PostCommentModel comment);
}