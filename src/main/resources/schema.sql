create table customer(
  ID int not null AUTO_INCREMENT,
  CUSTOMER_ID varchar(100) not null,
  FIRST_NAME varchar(100) not null,
  LAST_NAME varchar(100) not null,
  EMAIL varchar(100) not null,
  MOBILE varchar(100) not null,
  PRIMARY KEY ( CUSTOMER_ID )
);


create table account(
  ACCOUNT_NO varchar(100) not null,
  ACCOUNT_NAME varchar(100) not null,
  ACCOUNT_TYPE varchar(10) not null,
  CUSTOMER_ID varchar(100) not null,
  CREATED_DATE date not null,
  UPDATED_DATE date not null,
  PRIMARY KEY ( ACCOUNT_NO ),
  FOREIGN KEY (CUSTOMER_ID) REFERENCES customer(CUSTOMER_ID)
);