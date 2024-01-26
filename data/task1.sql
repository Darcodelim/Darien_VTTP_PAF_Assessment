-- Write your Task 1 answers in this file

DROP DATABASE IF EXISTS bedandbreakfast;


CREATE DATABASE bedandbreakfast;


USE bedandbreakfast;


CREATE TABLE users (
    email VARCHAR(128) NOT NULL,
    name VARCHAR(128),
    PRIMARY KEY (email)
);


CREATE TABLE bookings (
    booking_id CHAR(8) NOT NULL,
    listing_id VARCHAR(20) unique,
    duration INT NOT NULL,
    email VARCHAR(128) NOT NULL,
    
    PRIMARY KEY (booking_id)
);

CREATE TABLE reviews (
    id INT AUTO_INCREMENT,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    listing_id VARCHAR(20),
    reviewer_name VARCHAR(64),
    comments TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (listing_id) REFERENCES bookings(listing_id)
);


GRANT ALL PRIVILEGES ON bedandbreakfast.* TO 'fred'@'%';


FLUSH PRIVILEGES;

Insert into users(email,name) Values ('fred@gmail.com', 'Fred Flintstone'),('barney@gmail.com', 'Barney Rubble'),('fry@planetexpress.com', 'Philip J Fry'),('hlmer@gmail.com', 'Homer Simpson');

Select * from users;







