package com.web.BlogApp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuração de segurança para a aplicação BlogApp.
 * Esta classe define as regras de autenticação e autorização.
 *
 * Importante: Algumas configurações (como CSRF desabilitado) são adequadas apenas para ambiente
 * de desenvolvimento e devem ser revisadas antes de implantar em produção.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define os caminhos públicos da aplicação que não requerem autenticação.
     * Isso inclui a página inicial, listagem de posts, visualização individual de posts,
     * e recursos estáticos como CSS, JavaScript e imagens.
     */
    private static final String[] PUBLIC_PATHS = {
            "/",
            "/posts",
            "/posts/{id}",
            "/css/**",
            "/js/**",
            "/images/**"
    };

    /**
     * Configura a cadeia de filtros de segurança HTTP.
     *
     * Esta configuração:
     * 1. Desabilita CSRF para simplificar o desenvolvimento (não recomendado para produção)
     * 2. Define quais URLs são públicas e quais requerem autenticação
     * 3. Configura o formulário de login personalizado e o comportamento de logout
     *
     * @param http O objeto HttpSecurity a ser configurado
     * @return A cadeia de filtros de segurança configurada
     * @throws Exception Se ocorrer um erro durante a configuração
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(PUBLIC_PATHS).permitAll() // Rotas públicas acessíveis a todos
                        .requestMatchers(HttpMethod.GET, "/newpost").authenticated() // Rotas protegidas
                        .requestMatchers(HttpMethod.POST, "/newpost").authenticated()
                        .requestMatchers("/posts/delete/**").authenticated()
                        .anyRequest().authenticated() // Todas as outras rotas requerem autenticação
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // Página de login
                        .defaultSuccessUrl("/posts") // Redirecionamento após login bem-sucedido
                        .permitAll() // A página de login deve ser acessível por todos
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/posts") // Redirecionamento após logout
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Configura recursos que devem ser completamente ignorados pelo Spring Security.
     * Isso é útil para recursos estáticos que nunca precisam de segurança.
     *
     * @return Um customizador de segurança web
     */

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/bootstrap/**");
    }

    /**
     * Define o codificador de senha a ser usado para verificação de senhas.
     * BCrypt é um algoritmo de hash seguro com salt automático.
     *
     * @return Um codificador de senha BCrypt
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura os usuários em memória para autenticação.
     *
     * NOTA: Esta implementação é adequada apenas para ambiente de desenvolvimento.
     * Em produção, deve-se implementar um UserDetailsService que carregue usuários
     * de um banco de dados ou outro sistema persistente.
     *
     * @param passwordEncoder O codificador de senha a ser usado
     * @return Um serviço de detalhes de usuário com usuários em memória
     */

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Criação do usuário administrador
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("adminpassword"))
                .roles("ADMIN")
                .build();

        // Criação de um usuário comum
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("userpassword"))
                .roles("USER")
                .build();

        // Retorna um gerenciador de usuários em memória com os usuários criados acima
        return new InMemoryUserDetailsManager(admin, user);
    }
}