-- Load employees data
CREATE TABLE IF NOT EXISTS employees (
  employee_id BIGINT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  contracted_from DATE,
  contracted_to DATE,
  archived BOOLEAN
);

DELETE FROM employees;

INSERT INTO employees (employee_id, first_name, last_name, name, contracted_from, contracted_to, archived)
VALUES (101, 'Test', 'Employee1', 'Test Employee 1', '2021-01-01', '2021-12-31', true);

INSERT INTO employees (employee_id, first_name, last_name, name, contracted_from, contracted_to, archived)
VALUES (102, 'Test', 'Employee2', 'Test Employee 2', '2022-02-01', '2022-12-31', true);

INSERT INTO employees (employee_id, first_name, last_name, name, contracted_from, contracted_to, archived)
VALUES (103, 'Test', 'Employee3', 'Test Employee 3', '2023-03-01', '2023-12-31', true);

-- Load projects data
CREATE TABLE IF NOT EXISTS projects (
  project_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_number BIGINT,
  name VARCHAR(255) NOT NULL,
  start_date DATE,
  end_date DATE,
  project_length_months DOUBLE,
  project_booked_months DOUBLE,
  remaining_booked_months DOUBLE,
  number_of_employees INTEGER,
  comment VARCHAR(255),
  archived BOOLEAN NOT NULL
);

DELETE FROM projects;

INSERT INTO projects (project_id, project_number, name, start_date, end_date, project_length_months, project_booked_months, remaining_booked_months, number_of_employees, comment, archived)
VALUES (111, 1, 'Test Project 1', '2021-01-01', '2021-12-31', 12.0, 12.0, 0.0, 2, 'Test Comment 1', true);

INSERT INTO projects (project_id, project_number, name, start_date, end_date, project_length_months, project_booked_months, remaining_booked_months, number_of_employees, comment, archived)
VALUES (222, 2, 'Test Project 2', '2022-01-01', '2022-12-31', 12.0, 9.0, 3.0, 3, 'Test Comment 2', true);

INSERT INTO projects (project_id, project_number, name, start_date, end_date, project_length_months, project_booked_months, remaining_booked_months, number_of_employees, comment, archived)
VALUES (333, 3, 'Test Project 3', '2023-01-01', '2023-12-31', 12.0, 6.0, 6.0, 4, 'Test Comment 3', true);

INSERT INTO projects (project_id, project_number, name, start_date, end_date, project_length_months, project_booked_months, remaining_booked_months, number_of_employees, comment, archived)
VALUES (444, 4, 'Test Project 4', '2023-06-01', '2023-12-31', 6.0, 3.0, 6.0, 2, 'Test Comment 4', false);

-- Load employee_projects data
CREATE TABLE IF NOT EXISTS employee_projects (
   employee_project_id BIGINT PRIMARY KEY AUTO_INCREMENT,
   employee_id BIGINT,
   project_id BIGINT,
   employee_booked_months DOUBLE,
   employee_project_start_date VARCHAR(255),
   employee_project_end_date VARCHAR(255),
   project_name VARCHAR(255),
   confirm_booking BOOLEAN NOT NULL,
   percentage INTEGER NOT NULL
);

DELETE FROM employee_projects;

INSERT INTO employee_projects (employee_id, project_id, employee_booked_months, employee_project_start_date, employee_project_end_date, project_name, confirm_booking, percentage)
VALUES (101, 111, 6.0, '2021-01-01', '2021-12-31', 'Test Project 1', true, 100);

INSERT INTO employee_projects (employee_id, project_id, employee_booked_months, employee_project_start_date, employee_project_end_date, project_name, confirm_booking, percentage)
VALUES (102, 111, 9.0, '2022-01-01', '2022-12-31', 'Test Project 1', false, 100);

INSERT INTO employee_projects (employee_id, project_id, employee_booked_months, employee_project_start_date, employee_project_end_date, project_name, confirm_booking, percentage)
VALUES (103, 111, 12.0, '2023-01-01', '2023-12-31', 'Test Project 1', true, 50);

INSERT INTO employee_projects (employee_id, project_id, employee_booked_months, employee_project_start_date, employee_project_end_date, project_name, confirm_booking, percentage)
VALUES (101, 222, 9.0, '2022-04-01', '2022-12-31', 'Test Project 2', false, 100);

INSERT INTO employee_projects (employee_id, project_id, employee_booked_months, employee_project_start_date, employee_project_end_date, project_name, confirm_booking, percentage)
VALUES (101, 333, 12.0, '2023-05-01', '2023-12-31', 'Test Project 3', true, 50);

CREATE TABLE IF NOT EXISTS custom_project_details (
  custom_project_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT,
  custom_project_year VARCHAR(255) NOT NULL,
  custom_project_months DOUBLE
);

DELETE FROM custom_project_details;

INSERT INTO custom_project_details (project_id, custom_project_year, custom_project_months)
VALUES (111, '2023', 10.0);

INSERT INTO custom_project_details (project_id, custom_project_year, custom_project_months)
VALUES (111, '2024', 11.0);

INSERT INTO custom_project_details (project_id, custom_project_year, custom_project_months)
VALUES (222, '2024', 8.0);


