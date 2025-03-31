# Web Blog Application

## Overview

This is a Spring Boot-based web blog application that allows users to create, view, and delete blog posts. The application uses Thymeleaf for templating, Spring Data JPA for database interactions, and Bootstrap for responsive design.

## Features

- Create new blog posts
- View list of all posts
- View detailed post information
- Delete existing posts
- Responsive design with Bootstrap
- MySQL database integration

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Thymeleaf
- MySQL
- Bootstrap 5
- Maven

## Prerequisites

- Java Development Kit (JDK) 17
- MySQL Database
- Maven

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/web-blog-application.git
cd web-blog-application
```

### 2. Database Configuration

- Create a MySQL database named `blogappdbweb`
- Update database credentials in `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost/blogappdbweb?createIfNotExists=true&allowPublicKeyRetrieval=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=America/Sao_Paulo&SameSite=None
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## Project Structure

- `src/main/java/com/web/BlogApp/`: Main application package
  - `controller/`: Web controllers
  - `model/`: Database entities
  - `repository/`: Data access repositories
  - `service/`: Business logic services
  - `dtos/`: Data Transfer Objects
  - `configuration/`: Application configurations
  - `utils/`: Utility classes

- `src/main/resources/`
  - `templates/`: Thymeleaf HTML templates
  - `application.properties`: Application configuration

## Database Schema

The application uses a `TB_POST` table with the following columns:
- `id`: Unique identifier (UUID)
- `autor`: Post author
- `data`: Post creation date
- `titulo`: Post title
- `texto`: Post content

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

Your Name - your.email@example.com

Project Link: [https://github.com/yourusername/web-blog-application](https://github.com/yourusername/web-blog-application)
