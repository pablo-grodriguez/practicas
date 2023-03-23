DROP TABLE IF EXISTS Purchase;
DROP TABLE IF EXISTS Session;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Film;
DROP TABLE IF EXISTS Hall;

CREATE TABLE User (
    id BIGINT NOT NULL AUTO_INCREMENT,
    userName VARCHAR(60) COLLATE latin1_bin NOT NULL,
    password VARCHAR(60) NOT NULL, 
    firstName VARCHAR(60) NOT NULL,
    lastName VARCHAR(60) NOT NULL, 
    email VARCHAR(60) NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT UserPK PRIMARY KEY (id),
    CONSTRAINT UserNameUniqueKey UNIQUE (userName),
    CONSTRAINT validRole CHECK (role='VIEWER' OR role='TICKET_SELLER')
) ENGINE = InnoDB;

CREATE INDEX UserIndexByUserName ON User (userName);

CREATE TABLE Film (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(60) NOT NULL,
    summary VARCHAR(180) NOT NULL,
    duration SMALLINT NOT NULL,

    CONSTRAINT FilmPK PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE INDEX FilmIndexByTitle ON Film (title);

CREATE TABLE Hall (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    capacity SMALLINT NOT NULL,

    CONSTRAINT HallPK PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE INDEX HallIndexByName ON Hall (name);

CREATE TABLE Session (
    id BIGINT NOT NULL AUTO_INCREMENT,
    dateTime DATETIME NOT NULL,
    price REAL NOT NULL,
    freeLocs SMALLINT NOT NULL,
    hallId BIGINT NOT NULL,
    filmId BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT SessionPK PRIMARY KEY (id),
    CONSTRAINT FK_HallSession FOREIGN KEY (hallId) REFERENCES Hall(id),
    CONSTRAINT FK_FilmSession FOREIGN KEY (filmId) REFERENCES Film(id)
) ENGINE = InnoDB;

CREATE TABLE Purchase (
    id BIGINT NOT NULL AUTO_INCREMENT,
    amount TINYINT NOT NULL,
    creditCard VARCHAR(16) NOT NULL,
    dateTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delivered BIT NOT NULL,
    sessionId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT PurchasePK PRIMARY KEY (id),
    CONSTRAINT FK_UserPurchase FOREIGN KEY (userId) REFERENCES User(id),
    CONSTRAINT FK_SessionPurchase FOREIGN KEY (sessionId) REFERENCES Session(id)
) ENGINE = InnoDB;
