# Fit4Life

Fit4Life is a secure, role-based fitness studio rating and management platform. It enables users to register, search, rate, comment, and subscribe to fitness studios. The system provides a clean architecture with support for both REST and SOAP APIs, JWT-based authentication, and administrative moderation tools.

### Technologies Used

- Backend: Spring Boot, Spring Security  
- Database: PostgreSQL  
- Authentication: JWT, Role-based access control, BCrypt  
- Build Tool: Maven  
- Other Tools: Postman, PGAdmin  

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9.9+
- PostgreSQL 17.5+
- Git
- Postman

### Installation
1. Clone the repository
```
git clone https://github.com/Bokizz/Fit4Life.git
```
3. Navigate to the project directory
```
cd fit4life
```

5. Set up the database:
   - Name: proekt_db (change if you want)
   - Update your application.properties file with your DB credentials and port.

2. Build the project:

```
mvn clean install
```

3. Run the application:

```
mvn spring-boot:run
```

## **API Documentation**

### REST Endpoints
**Users**
- POST /user/ban – Ban/Unban users (Admins only)
- POST /user/chat-restrict – Chat restrict users (Admins and Moderators)
- POST /user/update-profile – Update user profile
- POST /user/create – Create user (Admin only)
- POST /studio/create – Create studio (Admin only)
- GET /user/all – Get all users
- GET /user/me – Get current logged-in user
- GET /user/search – Get user by username or email
- PUT /user/update-username – Update username
- DELETE /user/delete – Delete user or studio (Admin only)

**Studios**
- GET /studio/search – Search by name, location, type, rating, or location+type
- POST /studio/create – Create studio
- PUT /studio/update – Update studio
- GET /studio/{id} – Get studio by ID
- GET /studio/all – Get all studios
  
**Photos**
- POST /api/photos/upload – Upload a photo (Admin only)
- PUT /api/photos/update/{photoId} – Update photo details (Admin only)
- GET /api/photos/studio/{studioId} – Get photos by studio
- GET /api/photos/{photoId} – Get photo by ID
- GET /api/photos/user/{userId} – Get photos uploaded by a user
- GET /api/photos/all/photos – Get all photos
- DELETE /api/photos/delete/{photoId} – Delete photo (Admin only)

**Comments**
- POST /api/comments/add – Add a comment
- PUT /api/comments/update/{commentId} – Update a comment
- GET /api/comments/studio/{studioId} – Get comments by studio
- GET /api/comments/{commentId} – Get comment by ID
- GET /api/comments/user/{userId} – Get comments by user
- GET /api/comments/user/{userId}/studio/{studioId} – Get comments by user and studio
- GET /api/comments/all – Get all comments
- DELETE /api/comments/delete/{commentId} – Delete comment (Admin/Moderator only)

**Ratings**
- POST /api/ratings/add – Add or update rating
- GET /api/ratings – Get all ratings
- GET /api/ratings/studio/{studioId} – Get ratings by studio
- GET /api/ratings/user/{userId}/studio/{studioId} – Get rating by user and studio
- DELETE /api/ratings/{id} – Delete rating

**Subscriptions**
- POST /api/subscriptions/subscribe – Subscribe to a studio
- DELETE /api/subscriptions/cancel – Cancel a subscription
- GET /api/subscriptions/user/{userId} – Get subscriptions by user
- GET /api/subscriptions/studio/{studioId} – Get subscriptions by studio
- GET /api/subscriptions/user-studio – Get subscription by user and studio
- GET /api/subscriptions/all – Get all subscriptions

### SOAP Endpoints
- Ban/Unban users (Admin)  
- Chat restrict users (Admin/Moderator)  
- Reset user password  
- User subscribe to Studio  
- Adda  photo to Studio  

Schema location: src/main/resources/schema/user.xsd

## Security

**Role-Based Access Control**

- **ADMIN**:
  - Ban/Unban and Chat Restrict users/moderators
  - Delete users/moderators/studios
  - Create/update studios
  - Add photos to studios

- **MODERATOR**:
  - Chat restrict/unrestrict users

- **USER**:
  - Rate, comment, and subscribe to studios


- **Authentication & JWT**
  - Access control is enforced using @PreAuthorize annotations (method-level security).
  - JWT is used for secure stateless authentication  
  - Passwords are hashed using BCryptPasswordEncoder 
  - Custom AuthenticationFilter authenticates login credentials and issues JWTs  
  - Token validation includes blacklist filtering for revoked tokens  
  - All secure endpoints are protected with JWT filters and role-based access

## Database Schema
- **Studio** – One-to-Many – **Photo, Comment, Rating, Subscription**
- **User** – One-to-Many – **Photo, Comment, Rating, Subscription**

## Contributions

Feel free to fork the repo and open pull requests. For major changes, please open an issue first to discuss what you would like to change.
