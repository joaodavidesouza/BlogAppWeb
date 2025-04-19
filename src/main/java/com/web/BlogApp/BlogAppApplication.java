package com.web.BlogApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*/
 * Classe principal que inicializa a aplicação Spring Boot do sistema de Blog.
 * Esta classe contém o método main que serve como ponto de entrada da aplicação.
 *
 * A anotação @SpringBootApplication combina três anotações:
 * - @Configuration: marca a classe como fonte de definições de beans
 * - @EnableAutoConfiguration: habilita a configuração automática do Spring Boot
 * - @ComponentScan: permite a detecção automática de componentes no pacote atual e subpacotes
 */

@SpringBootApplication
public class BlogAppApplication {

	/**
	 * Método principal que inicia a aplicação Spring Boot.
	 * @param args Argumentos de linha de comando passados para a aplicação
	 */
	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
	}
}