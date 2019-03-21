SET MODE mySql;

CREATE TABLE IF not exists restaurants(
    id int primary key auto_increment,
    name varchar(40),
    address varchar(40),
    zipcode varchar(40),
    phone varchar(40),
    website varchar(40),
    email varchar(40)
);

create table if not exists foodtypes(
  id int primary key auto_increment,
  name varchar(40)
);

create table if not exists reviews(
  id int primary key auto_increment,
  writtenby varchar(40),
  content text,
  rating int,
  restaurantID INTEGER
);
-- Many to many relationship between restaurants and foodtypes
create table if not exists restaurants_foodtypes(
  id int primary key auto_increment,
  foodtypeid INTEGER,
  restaurantid INTEGER
);