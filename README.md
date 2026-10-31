# Skills and Internship Recommender System

This project is a complete full-stack web application with:
- Frontend: HTML, CSS, JavaScript
- Backend: Java + Spring Boot (REST API)
- Database: MySQL
- Features: user register/login, profile-based recommendations, internship applications, admin-protected panel

## Project Structure

- `backend/` - Spring Boot REST API
- `frontend/` - Static frontend pages (`index.html`, `login.html`, `register.html`, `user.html`, `admin-login.html`, `admin.html`)
- `database/init.sql` - MySQL schema + sample data

## Backend Setup (Spring Boot)

1. Open terminal in `backend` folder.
2. Update DB credentials in `src/main/resources/application.properties`:
   - `spring.datasource.username`
   - `spring.datasource.password`
3. Run backend:
   - `mvn spring-boot:run`
4. Optional: change `app.admin.passcode` in `src/main/resources/application.properties`.
5. Backend starts on: `http://localhost:8080`

## MySQL Setup

1. Ensure MySQL server is running.
2. Run script from `database/init.sql` using MySQL client or Workbench.
3. Verify database `internship_db` and table `internships` are created.

## Frontend Setup

1. Open `frontend/index.html` in browser.
2. For users:
   - Go to `Register` (or `Login`)
   - Open `user.html` automatically after auth
   - Update profile (skills, interests, activities)
   - View recommendations and apply internships
   - See applied internships in user portal table
3. For admin:
   - Open `admin-login.html`
   - Login with admin passcode (`app.admin.passcode`)
   - Manage internships and view all user applications in `admin.html`

## API Endpoints

### Auth APIs
- `POST /api/auth/register`
- `POST /api/auth/login`

### User APIs
- `GET /api/users/{userId}`
- `PUT /api/users/{userId}/profile`
- `POST /api/users/{userId}/applications/{internshipId}`
- `GET /api/users/{userId}/applications`

### Recommendation API
- `POST /api/recommend`

Request Body:
```json
{
  "skills": "Java, SQL",
  "interests": "Backend development"
}
```

### Admin APIs
- `POST /api/admin/login` (header: `X-Admin-Pass`)
- `POST /api/admin/internships`
- `GET /api/admin/internships`
- `PUT /api/admin/internships/{id}`
- `DELETE /api/admin/internships/{id}`
- `GET /api/admin/applications`

## Postman Testing Steps

1. Create a new collection.
2. Add request: `POST http://localhost:8080/api/recommend`
3. Set body as raw JSON and send sample payload.
4. Test admin CRUD endpoints with JSON body:
```json
{
  "title": "Cloud Intern",
  "company": "SkyNet",
  "skills": "AWS, Linux, Docker",
  "applyLink": "https://example.com/apply/cloud-intern"
}
```

## Notes

- CORS is enabled for frontend access.
- Validation and global exception handling are implemented.
- Passwords are stored using BCrypt hash.
- Matching score formula: `(matchedSkills / totalRequiredSkills) * 100`, with interest bonus up to 10%.
