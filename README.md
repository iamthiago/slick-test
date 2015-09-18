## Simple project to test the slick capabilities.

### Instructions

You can choose between mysql or H2. Just change the property name running.db in application.conf.

For mysql, you also need to create a db called mydb, otherwise you have to change in the properties file.

Don't forget to change the implicit import.

- mysql: import slick.driver.MySQLDriver.api._
- h2: import slick.driver.H2Driver.api._
