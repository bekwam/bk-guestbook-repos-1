# bk-guestbook-repos-1
Repository for Bekwam Guestbook software architecture demo

The demo presents UML modeling techniques for analysis and design with a fully-implemented example..  The goal of the modeling is twofold:

1. Bolster the system definition and requirements gathering effort, and
2. Provide structures to coordinate the coding effort. 

The demo features an Single Page App hooked up to a RESTful API.  A third-party vendor, WebPurify, is integrated into the API to provide content filtering services. The following UML Deployment Diagram shows a Rackspace-hosted Linux server "Bekwam.us" running the WildFly app server and PostgreSQL.  The project code is packaged in several Web Application Archive (WAR) files and deployed in WildFly.  The code calls out the the WebPurify service.

![Architecture](https://www.dropbox.com/s/73c242unh0mkkll/BKGuestbook_Deployment_Production.png?raw=1)

## Problem Statement

The demo is a guestbook where anonymous users can post a text message.  Initially, this will just be handling a single form submission coming from the Internet.  A special requirement though is to moderate the content.  Because the WebPurify service charges by the concurrent request, Java Messaging Service (JMS) code rate limits the request.  

See the Wiki for more information about the specific requirements and the artifacts supporting the effort.

## Installation

The code is deployable as a set of WARs running in the WildFly app server.  PostgresSQL is used to hold the messages submitted on the site.

### Database

The following combination of DDL and DML is used to initialize the database.  A dedicated tablespace is used for the demo as are a pair of non-DBA accounts.

```
-- provision dba account
CREATE TABLESPACE tbs_06 LOCATION '/var/lib/postgresql/data_06';

CREATE DATABASE bk_guestbook TABLESPACE tbs_06;

CREATE USER bkg_admin LOGIN PASSWORD 'password';

ALTER TABLESPACE tbs_06 OWNER TO bkg_admin;

-- provision user account
CREATE USER bkg_user LOGIN PASSWORD ‘password';

GRANT CONNECT ON DATABASE bk_guestbook TO bkg_user;

-- create table
-- \connect bk_guestbook
CREATE SEQUENCE bkg_entry_id_seq;

CREATE TABLE bkg_entry (
        id BIGINT PRIMARY KEY,
        text VARCHAR(1024),
        created_on TIMESTAMP NOT NULL,
        created_by VARCHAR(255),
        version INT,
        state VARCHAR(25)        
);

CREATE SEQUENCE bkg_config_id_seq;

CREATE TABLE bkg_config (
  id BIGINT PRIMARY KEY,
  name VARCHAR(128),
  value VARCHAR(1024),
  version INT
);

-- grants
GRANT SELECT, UPDATE, INSERT, DELETE ON ALL TABLES IN SCHEMA PUBLIC TO bkg_user;

GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA PUBLIC TO bkg_user;

INSERT INTO bkg_config (id, name, value, version)
VALUES (nextval('bkg_config_id_seq'), 'us.bekwam.guestbook.profanity.client.WebPurifyClient.api_key', 'api_key', 0);

INSERT INTO bkg_config (id, name, value, version)
VALUES (nextval('bkg_config_id_seq'), 'us.bekwam.guestbook.profanity.client.WebPurifyClient.url', 'http://api1.webpurify.com/services/rest/', 0);

The "api_key" property inserted into the bkg_config table requires a valid API Key provided by WebPurify.
```
