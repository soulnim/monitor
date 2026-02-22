-- Monitor Application Database Schema
-- Database: Java DB (Apache Derby)
-- Date: February 8, 2026

-- =====================================================
-- 1. USERS TABLE
-- =====================================================
CREATE TABLE users (
    user_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    profile_picture VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

-- =====================================================
-- 2. CATEGORIES TABLE (for transactions and goals)
-- =====================================================
CREATE TABLE categories (
    category_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    category_name VARCHAR(50) NOT NULL,
    category_type VARCHAR(20) NOT NULL, -- 'INCOME', 'EXPENSE', 'GOAL'
    is_default SMALLINT DEFAULT 0, -- 0 = custom, 1 = default/system
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_categories PRIMARY KEY (category_id),
    CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- 3. TRANSACTIONS TABLE
-- =====================================================
CREATE TABLE transactions (
    transaction_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    transaction_type VARCHAR(10) NOT NULL, -- 'INCOME' or 'EXPENSE'
    transaction_date DATE NOT NULL,
    description VARCHAR(255),
    is_recurring SMALLINT DEFAULT 0, -- 0 = no, 1 = yes
    recurring_frequency VARCHAR(20), -- 'DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_transactions PRIMARY KEY (transaction_id),
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- =====================================================
-- 4. BUDGETS TABLE
-- =====================================================
CREATE TABLE budgets (
    budget_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    budget_month INTEGER NOT NULL, -- 1-12
    budget_year INTEGER NOT NULL,
    budget_limit DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_budgets PRIMARY KEY (budget_id),
    CONSTRAINT fk_budgets_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_budgets_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE,
    CONSTRAINT uk_budgets UNIQUE (user_id, category_id, budget_month, budget_year)
);

-- =====================================================
-- 5. TASKS TABLE
-- =====================================================
CREATE TABLE tasks (
    task_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    description CLOB,
    due_date DATE,
    priority VARCHAR(10) DEFAULT 'MEDIUM', -- 'HIGH', 'MEDIUM', 'LOW'
    status VARCHAR(20) DEFAULT 'PENDING', -- 'PENDING', 'IN_PROGRESS', 'COMPLETED'
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_tasks PRIMARY KEY (task_id),
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- 6. TAGS TABLE
-- =====================================================
CREATE TABLE tags (
    tag_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    tag_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_tags PRIMARY KEY (tag_id),
    CONSTRAINT fk_tags_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT uk_tags UNIQUE (user_id, tag_name)
);

-- =====================================================
-- 7. TASK_TAGS TABLE (Many-to-Many relationship)
-- =====================================================
CREATE TABLE task_tags (
    task_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    CONSTRAINT pk_task_tags PRIMARY KEY (task_id, tag_id),
    CONSTRAINT fk_task_tags_task FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
    CONSTRAINT fk_task_tags_tag FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE
);

-- =====================================================
-- 8. GOALS TABLE
-- =====================================================
CREATE TABLE goals (
    goal_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    category_id INTEGER,
    title VARCHAR(200) NOT NULL,
    description CLOB,
    target_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- 'ACTIVE', 'ACHIEVED', 'ABANDONED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_goals PRIMARY KEY (goal_id),
    CONSTRAINT fk_goals_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_goals_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- =====================================================
-- 9. MILESTONES TABLE
-- =====================================================
CREATE TABLE milestones (
    milestone_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    goal_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    description CLOB,
    target_date DATE,
    is_completed SMALLINT DEFAULT 0, -- 0 = no, 1 = yes
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_milestones PRIMARY KEY (milestone_id),
    CONSTRAINT fk_milestones_goal FOREIGN KEY (goal_id) REFERENCES goals(goal_id) ON DELETE CASCADE
);

-- =====================================================
-- 10. EVENTS TABLE (Calendar/Schedule)
-- =====================================================
CREATE TABLE events (
    event_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    description CLOB,
    event_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    event_type VARCHAR(50),
    is_recurring SMALLINT DEFAULT 0, -- 0 = no, 1 = yes
    recurring_frequency VARCHAR(20), -- 'DAILY', 'WEEKLY', 'MONTHLY'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_events PRIMARY KEY (event_id),
    CONSTRAINT fk_events_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- 11. BILLS TABLE
-- =====================================================
CREATE TABLE bills (
    bill_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    bill_name VARCHAR(100) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    due_date DATE NOT NULL,
    category VARCHAR(50),
    status VARCHAR(20) DEFAULT 'UNPAID', -- 'UNPAID', 'PAID', 'OVERDUE'
    payment_date DATE,
    is_recurring SMALLINT DEFAULT 0, -- 0 = no, 1 = yes
    recurring_frequency VARCHAR(20), -- 'MONTHLY', 'YEARLY', etc.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_bills PRIMARY KEY (bill_id),
    CONSTRAINT fk_bills_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- 12. NOTES TABLE
-- =====================================================
CREATE TABLE notes (
    note_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    content CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_notes PRIMARY KEY (note_id),
    CONSTRAINT fk_notes_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- =====================================================
-- 13. NOTE_TAGS TABLE (Many-to-Many relationship)
-- =====================================================
CREATE TABLE note_tags (
    note_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    CONSTRAINT pk_note_tags PRIMARY KEY (note_id, tag_id),
    CONSTRAINT fk_note_tags_note FOREIGN KEY (note_id) REFERENCES notes(note_id) ON DELETE CASCADE,
    CONSTRAINT fk_note_tags_tag FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE
);

-- =====================================================
-- INSERT DEFAULT CATEGORIES
-- =====================================================
-- Note: These will be inserted after first user registers
-- For now, we'll create them manually or via application code

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================
CREATE INDEX idx_transactions_user_date ON transactions(user_id, transaction_date);
CREATE INDEX idx_tasks_user_status ON tasks(user_id, status);
CREATE INDEX idx_tasks_user_due_date ON tasks(user_id, due_date);
CREATE INDEX idx_bills_user_due_date ON bills(user_id, due_date);
CREATE INDEX idx_events_user_date ON events(user_id, event_date);
CREATE INDEX idx_goals_user_status ON goals(user_id, status);
CREATE INDEX idx_categories_user_type ON categories(user_id, category_type);
-- =====================================================
-- 14. USER_PREFERENCES TABLE
-- =====================================================
CREATE TABLE user_preferences (
    user_id INTEGER NOT NULL,
    email_notifications SMALLINT DEFAULT 1,
    bill_reminders SMALLINT DEFAULT 1,
    task_reminders SMALLINT DEFAULT 1,
    theme VARCHAR(10) DEFAULT 'dark',
    language VARCHAR(10) DEFAULT 'en',
    date_format VARCHAR(20) DEFAULT 'MM/DD/YYYY',
    currency VARCHAR(10) DEFAULT 'USD',
    profile_visibility VARCHAR(20) DEFAULT 'private',
    data_sharing SMALLINT DEFAULT 0,
    CONSTRAINT pk_user_preferences PRIMARY KEY (user_id),
    CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
