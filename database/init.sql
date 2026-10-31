DROP DATABASE IF EXISTS internship_db;
CREATE DATABASE internship_db;
USE internship_db;

CREATE TABLE internships (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    skills TEXT NOT NULL,
    category VARCHAR(100) NOT NULL,
    apply_link VARCHAR(500) NOT NULL
);

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    skills TEXT,
    interests TEXT,
    activities TEXT
);

CREATE TABLE internship_applications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    internship_id INT NOT NULL,
    applied_at DATETIME NOT NULL,
    status VARCHAR(40) NOT NULL DEFAULT 'APPLIED',
    UNIQUE KEY uq_user_internship (user_id, internship_id),
    CONSTRAINT fk_app_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_app_internship FOREIGN KEY (internship_id) REFERENCES internships(id)
);

INSERT INTO internships (title, company, skills, category, apply_link) VALUES
('Backend Developer Intern', 'TechNova', 'Java, Spring Boot, SQL, Git', 'Backend', 'https://example.com/apply/backend-intern'),
('Data Analyst Intern', 'Insight Labs', 'Python, SQL, Excel, Statistics', 'Data', 'https://example.com/apply/data-analyst-intern'),
('Frontend Developer Intern', 'PixelCraft', 'HTML, CSS, JavaScript, React', 'Frontend', 'https://example.com/apply/frontend-intern'),
('Full Stack Intern', 'CodeBridge', 'Java, Spring Boot, JavaScript, MySQL', 'Full Stack', 'https://example.com/apply/fullstack-intern'),
('AI/ML Intern', 'FutureMind', 'Python, Machine Learning, Pandas, TensorFlow', 'AI/ML', 'https://example.com/apply/aiml-intern');
