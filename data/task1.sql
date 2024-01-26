-- Write your Task 1 answers in this file

-- Drop the database if it exists
DROP DATABASE IF EXISTS bedandbreakfast;

-- Create the database
CREATE DATABASE bedandbreakfast;

-- Switch to the created database
USE bedandbreakfast;

-- Create the users table
CREATE TABLE users (
    email VARCHAR(128) NOT NULL,
    name VARCHAR(128),
    PRIMARY KEY (email)
);

-- Create the bookings table
CREATE TABLE bookings (
    booking_id CHAR(8) NOT NULL,
    listing_id VARCHAR(20) unique,
    duration INT NOT NULL,
    email VARCHAR(128) NOT NULL,
    
    PRIMARY KEY (booking_id)
);

-- Create the reviews table
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

-- Flush privileges to apply changes
FLUSH PRIVILEGES;

Insert into users(email,name) Values ('fred@gmail.com', 'Fred Flintstone'),('barney@gmail.com', 'Barney Rubble'),('fry@planetexpress.com', 'Philip J Fry'),('hlmer@gmail.com', 'Homer Simpson');

Select * from users;







