CREATE TABLE author
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    age  INT NULL,
    CONSTRAINT pk_author PRIMARY KEY (id)
);

CREATE TABLE book
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    isbn      VARCHAR(255) NULL,
    title     VARCHAR(255) NULL,
    author_id BIGINT NULL,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

ALTER TABLE book
    ADD CONSTRAINT FK_BOOK_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES author (id);