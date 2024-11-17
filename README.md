# Overview

Bekwam JDBC User Storage SPI project is a JDBC (Java Database Connectivity) extension for [Keycloak](https://www.keycloak.org/).  The SPI allows Keycloak administrators to integrate one or more relational databases (RDBMS) as additional authentication and authorization sources.  An SPI instance belongs to a Realm and a Realm can have multiple SPI instances.  The configuration of the SPI is performed though a User Federation screen.

The SPI configuration includes a set of SQL statements which is the linkage between Keycloak and the specific set of RDMBS tables or views.

The current capabilities are limited to read-only operations on the underlying datastores.  The full set of methods available to an SPI are not implemented.  For example, passwords cannot be set through the SPI.  (To reset a password, modify the datasource outside of Keycloak.)

In version 1.0, only Postgres has been fully-tested and the password field must be a base64-encoded SHA-256 hash.  Oracle has been lightly-tested.  Future versions will expand the tested datasources and allow more hashing and encoding functions.

# Installation

To install the Bekwam JDBC SPI, run a `mvn clean install` from the top level of the bekwam-jdbc-user-storage-spi [project in GitHub](https://github.com/bekwam/bekwam-jdbc-user-storage-spi).  Copy the bekwam-jdbc-user-storage-spi-1.0.jar JAR into the Keycloak /providers folder.

The JDBC driver used by the SPI will also need to be copied into the /providers folder.  This is a JAR file like postgresql-42.6.0.jar or ojdbc11.jar.

Rebuild Keycloak or if evaluating, use `bin/kc.sh start-dev`.

# Configuration

With Keycloak running, log on as a Realm Manager.  Navigate to User Federation and select "Bekwam JDBC Providers".

![image](https://github.com/user-attachments/assets/0a6914d8-d26b-4efb-9fbc-dc32c39d5859 "User Federation Screenshot")

This will take you to the Bekwam JDBC SPI configuration screen.

![image](https://github.com/user-attachments/assets/819a494f-d087-4b44-82de-4af66c0c0fad "Bekwam JDBC Configuration Screenshot")

The following subsections describe the configuration parameters

## UI Display Name

A descriptive name of the SPI instance

## DB Connection URL

A valid JDBC URL

For example jdbc:postgresql://localhost:5433/userdb

## Username

The username for connecting to the database specified in the DB Connection URL parameter

## Password

The credential accompanying the Username parameter

Will be masked after the screen is saved

## Min Pool Size

The starting pool size for the JDBC connection pool 

The number of connections in the pool will always be at least Min Pool Size

## Max Pool Size

The maximum number of connections this SPI instance is allowed

A pool is distinct to a SPI configuration.  If another SPI instance is configured for a different Realm or within the same Realm, each instance will have its own pool.

## Validation Timeout

The interval in seconds that a reaper process will run to test connections

The test is the successful running of the query in the Validation Query parameter.

## Users Query

A SQL SELECT statemnt

This must return a zero or one row containing a single field.  The single field must contain the hashed and encoded password credential.  The query must provide a single bind variable which will be compared against a username suppled by the login form.

## Roles Query

This must return zero or more rows containing a single field which is the role name.  The query must provide a single bind variable which will be compared against a username suppled by the login form.

Both the Users Query and the Roles Query can contain complex SQL that uses joins and views.

Both the Users Query and the Roles Query are case-sensitive.  Keycloak passes in the username as lower-case from the login form.  Apply functions like LOWER() within the WHERE clauses if the data store usernames are upper-case.

## Search All Users Query

A query that returns zero or more rows containing the following four fields in order.

1. username - the username for Keycloak authentication
2. password - the hased and encoded password used for Keycloak authentication
3. name - a descriptive name (ex "last name, firstname") of the user
4. email - the email of the user

*No bind variables are required.*
  
The Search All Users Query can use a function like LOWER() to format upper-case usernames.

An ORDER BY clause can be used for server-side ordering.

## Search Users Query

A parameterized users query for searching specific users.  The query will return zero or more rows containing the following fields

1. username - the username for Keycloak authentication
2. password - the hased and encoded password used for Keycloak authentication
3. name - a descriptive name (ex "last name, firstname") of the user
4. email - the email of the user

The SQL must contain 3 bind variables.  These will be used in a WHERE clause to compare against the following fields in order.

1. username
2. name
3. email

The SQL can contain functions like LOWER() to handle the Keycloak lower-case username.  The SQL can also contain database-specific wildcard characters.

## Validation Query

If the Validation Timeout interval is greater than zero, the SQL in the Validation Query parameter will be invoked for each of the connections in the connection pool.

The query must return quickly.  For Postgres, `SELECT 1` is recommended.  For Oracle, `SELECT 1 FROM DUAL` is recommended.

## Cache Policy

This is a parameter provided to all User Storage SPI implementations.

NO_CACHE is usually the best option because credential changes to the underlying datastore will be effective immediately.

# Troubleshooting

There are some basic validations on the configuration screen.

For more detailed information about the operation or configuration of the SPI, enabled Quarkus logging using the following category.  When running in developer mode

```
bin/kc.sh start-dev -Dquarkus.log.category.\"com.bekwam\".level=TRACE
```

# Support

See [the Wiki](https://github.com/bekwam/bekwam-jdbc-user-storage-spi/wiki) for additional documentation.

Feel free to DM Carl Walker at the Cloud Native Computing Foundation Slack.

![image](https://github.com/user-attachments/assets/1d82b534-4e08-40ae-a7ff-2efe2d142ae4 "Carl")









