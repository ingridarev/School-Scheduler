CREATE TABLE GROUPS (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(100),
   school_year INT NOT NULL,
   student_amount INT NOT NULL,
   program_id BIGINT,
   shift_id BIGINT,
   deleted BOOLEAN,
   created_date TIMESTAMP,
   modified_date TIMESTAMP,
   created_by VARCHAR(255),
   modified_by VARCHAR(255),
   CONSTRAINT pk_groups PRIMARY KEY (id)
);

CREATE TABLE SUBJECT (
   ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
   NAME CHARACTER VARYING(100),
   DESCRIPTION CHARACTER VARYING(500),
   CREATED_DATE TIMESTAMP,
   MODIFIED_DATE TIMESTAMP,
   MODULE_ID BIGINT,
   DELETED BOOLEAN,
   CONSTRAINT SUBJECT_PK PRIMARY KEY (ID)
);

CREATE TABLE MODULE (
   ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
   NAME CHARACTER VARYING(100),
   DESCRIPTION CHARACTER VARYING(500),
   CREATED_DATE TIMESTAMP,
   MODIFIED_DATE TIMESTAMP,
   DELETED BOOLEAN,
   CONSTRAINT MODULE_PK PRIMARY KEY (ID)
);

CREATE TABLE SHIFT (
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
   NAME VARCHAR(100),
   STARTS INTEGER,
   ENDS INTEGER,
   CREATED_DATE TIMESTAMP,
   MODIFIED_DATE TIMESTAMP,
   DELETED BOOLEAN,
   CONSTRAINT PK_SHIFT PRIMARY KEY (id)
);

CREATE TABLE modules_subjects (
  module_id BIGINT NOT NULL,
   subject_id BIGINT NOT NULL,
   CONSTRAINT pk_modules_subjects PRIMARY KEY (module_id, subject_id)
);

ALTER TABLE modules_subjects ADD CONSTRAINT fk_modsub_on_module FOREIGN KEY (module_id) REFERENCES module (id);

ALTER TABLE modules_subjects ADD CONSTRAINT fk_modsub_on_subject FOREIGN KEY (subject_id) REFERENCES subject (id);

CREATE TABLE ROOM (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(100),
   building VARCHAR(255),
   description VARCHAR(500),
   created_date TIMESTAMP,
   modified_date TIMESTAMP,
  DELETED BOOLEAN,
   CONSTRAINT pk_room PRIMARY KEY (id)
);
CREATE TABLE rooms_subjects (
  room_id BIGINT NOT NULL,
   subject_id BIGINT NOT NULL,
   CONSTRAINT pk_rooms_subjects PRIMARY KEY (room_id, subject_id)
);

ALTER TABLE rooms_subjects ADD CONSTRAINT fk_roosub_on_room FOREIGN KEY (room_id) REFERENCES room (id);

ALTER TABLE rooms_subjects ADD CONSTRAINT fk_roosub_on_subject FOREIGN KEY (subject_id) REFERENCES subject (id);

CREATE TABLE teacher (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(100),
   login_email VARCHAR(255),
   contact_email VARCHAR(255),
   phone VARCHAR(255),
   work_hours_per_week DOUBLE NOT NULL,
   shift VARCHAR(255),
   created_date TIMESTAMP,
   modified_date TIMESTAMP,
   created_by VARCHAR(255),
   modified_by VARCHAR(255),
  DELETED BOOLEAN,
   CONSTRAINT pk_teacher PRIMARY KEY (id)
);


CREATE TABLE teachers_subjects (
  subject_id BIGINT NOT NULL,
   teacher_id BIGINT NOT NULL,
   CONSTRAINT pk_teachers_subjects PRIMARY KEY (subject_id, teacher_id)
);

ALTER TABLE teachers_subjects ADD CONSTRAINT fk_teasub_on_subject FOREIGN KEY (subject_id) REFERENCES subject (id);

ALTER TABLE teachers_subjects ADD CONSTRAINT fk_teasub_on_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id);

CREATE TABLE program
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(100),
    description   VARCHAR(500),
    created_date  TIMESTAMP,
    modified_date TIMESTAMP,
    DELETED BOOLEAN,
    CONSTRAINT pk_program PRIMARY KEY (id)
);

CREATE TABLE program_subject_hours
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    program_id    BIGINT                NULL,
    subject_id    BIGINT                NULL,
    subject_hours INT                   NOT NULL,
    CONSTRAINT pk_programsubjecthours PRIMARY KEY (id)
);

CREATE TABLE groups_program (
                                program_id BIGINT NOT NULL,
                                subject_hours_id BIGINT NOT NULL
);

ALTER TABLE groups_program ADD CONSTRAINT uc_groups_program_program UNIQUE (program_id);

ALTER TABLE groups_program ADD CONSTRAINT fk_gropro_on_groups FOREIGN KEY (subject_hours_id) REFERENCES GROUPS (id);

ALTER TABLE groups_program ADD CONSTRAINT fk_gropro_on_program FOREIGN KEY (program_id) REFERENCES program (id);


ALTER TABLE program_subject_hours
    ADD CONSTRAINT FK_PROGRAMSUBJECTHOURS_ON_PROGRAM FOREIGN KEY (program_id) REFERENCES program (id);

ALTER TABLE program_subject_hours

ADD CONSTRAINT FK_PROGRAMSUBJECTHOURS_ON_SUBJECT FOREIGN KEY (subject_id) REFERENCES subject (id);

ALTER TABLE GROUPS ADD CONSTRAINT FK_GROUPS_ON_PROGRAM FOREIGN KEY (program_id) REFERENCES program (id);

ALTER TABLE GROUPS ADD CONSTRAINT FK_GROUPS_ON_SHIFT FOREIGN KEY (shift_id) REFERENCES shift (id);

CREATE TABLE teacher_shifts (
  shift_id BIGINT NOT NULL,
   teacher_id BIGINT NOT NULL,
   CONSTRAINT pk_teacher_shifts PRIMARY KEY (shift_id, teacher_id)
);

ALTER TABLE teacher_shifts ADD CONSTRAINT fk_teashi_on_shift FOREIGN KEY (shift_id) REFERENCES shift (id);

ALTER TABLE teacher_shifts ADD CONSTRAINT fk_teashi_on_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id);