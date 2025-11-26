CREATE TABLE user_tasks (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            title VARCHAR(255),
                            description TEXT,
                            status VARCHAR(50),
                            priority VARCHAR(50),
                            due_date TIMESTAMP NULL,
                            assigned_to BIGINT,
                            created_at TIMESTAMP,
                            updated_at TIMESTAMP
);
