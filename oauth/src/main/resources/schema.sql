CREATE TABLE custom_authority (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  authority varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE custom_credentials (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE custom_credentials_authorities (
  custom_credentials_id bigint(20) NOT NULL,
  authorities_id bigint(20) NOT NULL,
  KEY FK_CUSTOM_AUTHORITIES (authorities_id),
  KEY FK_CUSTOM_CREDENTIALS (custom_credentials_id),
  CONSTRAINT FK_CUSTOM_AUTHORITIES FOREIGN KEY (authorities_id) REFERENCES custom_authority (id),
  CONSTRAINT FK_CUSTOM_CREDENTIALS FOREIGN KEY (custom_credentials_id) REFERENCES custom_credentials (id)
);

CREATE TABLE custom_client_details (
  client_id varchar(255) NOT NULL,
  authorities varchar(255) DEFAULT NULL,
  grant_types varchar(255) DEFAULT NULL,
  resource_ids varchar(255) DEFAULT NULL,
  scope varchar(255) DEFAULT NULL,
  PRIMARY KEY (client_id)
);