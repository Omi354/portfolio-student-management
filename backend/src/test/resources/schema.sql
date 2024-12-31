CREATE TABLE students (
  id varchar(36) NOT NULL,
  full_name varchar(50) NOT NULL,
  kana varchar(100) DEFAULT NULL,
  nick_name varchar(50) DEFAULT NULL,
  email varchar(254) NOT NULL,
  city varchar(50) NOT NULL,
  age int NOT NULL,
  gender varchar(15) DEFAULT 'Unspecified',
  remark text,
  is_deleted boolean DEFAULT false,
  PRIMARY KEY (id),
  UNIQUE (email),
  CONSTRAINT chk_gender CHECK (gender IN ('Male', 'Female', 'NON_BINARY', 'Unspecified'))
);

CREATE TABLE students_courses (
  id varchar(36) NOT NULL DEFAULT RANDOM_UUID(),
  student_id varchar(36) NOT NULL,
  course_name varchar(100) NOT NULL,
  start_date timestamp NULL DEFAULT NULL,
  end_date timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (student_id) REFERENCES students (id)
);

CREATE TABLE enrollment_statuses (
	id VARCHAR(36) PRIMARY KEY,
	student_course_id VARCHAR(36) NOT NULL,
	status VARCHAR(20) NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (student_course_id) REFERENCES students_courses(id)
);