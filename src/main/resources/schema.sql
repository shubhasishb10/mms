-- My Sql table schemas

---------------------------------
--- Medicine table schema -------
---------------------------------
CREATE TABLE if not exists medicine (
        medicineId bigint NOT NULL AUTO_INCREMENT primary key,
    name VARCHAR(255) NOT NULL,
    companyStringName VARCHAR(255),
    price DECIMAL(8,2),
    purchaseDate TIMESTAMP,
    volume INT,
    boxNumber INT,
    displayName varchar(50),
    companyDisplayName varchar(100),
    companyId bigint
);

---------------------------------
--- Retail table schema -------
---------------------------------
create table if not exists retail (
        retailId bigint not null primary key auto_increment,
    retailDate TIMESTAMP not null,
    customerName VARCHAR(50),
    customerAddress varchar(100)
);

---------------------------------
--- RetailMedicine for many to many relationship table schema -------
---------------------------------
create table if not exists retail_medicine (
        retailMedicineId bigint not null primary key auto_increment,
    retailId bigint,
    boxId bigint,
    medicineId bigint not null,
    retailCount int not null
);

---------------------------------
--- Company table schema -------
---------------------------------
create table if not exists company (
        companyId bigint not null primary key auto_increment,
    name varchar(100) not null,
    contactPerson varchar(100),
    mobileNumber VARCHAR(15)
);

---------------------------------
--- Box table schema -------
---------------------------------
create table if not exists box (
        boxId bigint not null primary key auto_increment,
    number varchar(5) not null,
    location varchar(100),
    capacity int,
    name varchar(100)
);

---------------------------------
--- MedicineBox for many to many relationship table schema -------
---------------------------------
create table if not exists medicine_box (
        medicineBoxId bigint not null primary key auto_increment,
    medicineId bigint not null,
    boxid bigint not null,
    medicineCount int
);