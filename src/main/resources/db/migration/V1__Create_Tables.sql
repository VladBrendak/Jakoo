-- Створення таблиці Employee та зовнішнього ключа
CREATE TABLE IF NOT EXISTS Employee (
    employee_id SERIAL PRIMARY KEY,
	name VARCHAR(255),
    description TEXT
);

-- Створення таблиці Note та зовнішнього ключа
CREATE TABLE IF NOT EXISTS Note (
    note_id SERIAL PRIMARY KEY,
	employee INT,
    text TEXT,
    date TIMESTAMP,
	CONSTRAINT fk_employee FOREIGN KEY (employee) REFERENCES Employee (employee_id)
);

-- Створення таблиці Workdays та зовнішнього ключа
CREATE TABLE IF NOT EXISTS Workdays (
    workday_id SERIAL PRIMARY KEY,
    status VARCHAR(20) CHECK (status IN ('present', 'missing', 'valid reason', 'day off', 'sick leave'))
);

INSERT INTO Workdays (status) VALUES
    ('present'),
    ('missing'),
    ('valid reason'),
    ('day off'),
    ('sick leave');

-- Створення таблиці Status та зовнішнього ключа
CREATE TABLE IF NOT EXISTS Status (
    status_id SERIAL PRIMARY KEY,
    status VARCHAR(50) CHECK (status IN ('in line', 'in progress', 'done', 'not working (cannot be verified)', 'clarification needed'))
);

INSERT INTO Status (status) VALUES
    ('in line'),
    ('in progress'),
    ('done'),
    ('not working (cannot be verified)'),
    ('clarification needed');

-- Створення таблиці Priority та зовнішнього ключа
CREATE TABLE IF NOT EXISTS Priority (
    priority_id SERIAL PRIMARY KEY,
    priority VARCHAR(30) CHECK (priority IN ('low', 'mid', 'high', 'ultimate (very high)'))
);

INSERT INTO Priority (priority) VALUES
    ('low'),
    ('mid'),
    ('high'),
    ('ultimate (very high)');

-- Створення таблиці workdays_note
CREATE TABLE IF NOT EXISTS workdays_note (
    workdays_note_id SERIAL PRIMARY KEY,
	employee INT,
    workday INT,
    note INT,
	work_time INT,
	date TIMESTAMP,
	CONSTRAINT fk_emloyee FOREIGN KEY (employee) REFERENCES Employee (employee_id),
    CONSTRAINT fk_workday FOREIGN KEY (workday) REFERENCES Workdays (workday_id),
    CONSTRAINT fk_note FOREIGN KEY (note) REFERENCES Note (note_id)
);

-- Створення таблиці EmployeeGroup
CREATE TABLE IF NOT EXISTS EmployeeGroup (
    group_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    is_custom BOOLEAN
);

-- Створення таблиці Members та зовнішнього ключа
CREATE TABLE IF NOT EXISTS Members (
    members_id SERIAL PRIMARY KEY,
    employee_group INT,
    employee INT,
    CONSTRAINT fk_group FOREIGN KEY (employee_group) REFERENCES EmployeeGroup (group_id) ON DELETE CASCADE,
    CONSTRAINT fk_employee FOREIGN KEY (employee) REFERENCES Employee (employee_id)
);

CREATE TABLE IF NOT EXISTS Tag (
    tag_id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

-- Створення таблиці Task та зовнішніх ключів
CREATE TABLE IF NOT EXISTS Task (
    task_id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    employees INT,
    responsible INT,
    results TEXT,
    status INT,
    creation_date TIMESTAMP,
    priority INT,
    CONSTRAINT fk_employees FOREIGN KEY (employees) REFERENCES EmployeeGroup (group_id),
    CONSTRAINT fk_responsible FOREIGN KEY (responsible) REFERENCES EmployeeGroup (group_id),
    CONSTRAINT fk_status FOREIGN KEY (status) REFERENCES Status (status_id),
    CONSTRAINT fk_priority FOREIGN KEY (priority) REFERENCES Priority (priority_id)
);

CREATE TABLE IF NOT EXISTS TaskTag (
    task_id INT,
    tag_id INT,
    PRIMARY KEY (task_id, tag_id),
    FOREIGN KEY (task_id) REFERENCES Task(task_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tag(tag_id)
);
