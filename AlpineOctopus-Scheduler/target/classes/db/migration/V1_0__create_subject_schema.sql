CREATE TABLE SUBJECT (
   ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
   NAME CHARACTER VARYING(80),
   DESCRIPTION CHARACTER VARYING(255),

   CONSTRAINT SUBJECT_PK PRIMARY KEY (ID)
);