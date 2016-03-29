# P2P-Project

This application aims to provide mobile service for money transfer among friends.

High-level use cases overview:

1. User download the mobile application from Google Play

2. Register an account and add personal bank account

3. Invite other users to participate in transaction. User can manage his/her contact list.

4. Select a friend and transfer money

5. After successfully transferred the money, receiver will receive a push notification.

<br>


# Resful Web Services

<b>Development Environment</b>
- Jersey 2.22.2
- Apache Tomcat 8.0.32
- JDK 1.8.0.73
- Maven 3.0.5
- Eclipse IDE Mars.2
- Jackson JSON library 2.6.1
- MySQL 5.6.21 or 5.7.11 (root@localhost: 123456 / u3CXCF?JBkWS)
<br>

<b>API</b>
<br>
https://docs.google.com/document/d/1RyXXQ-zcGm4rnN19AYb6j3E4v6uj2yToCrBW6-jeSfY/edit?usp=sharing
<br>

<b>References:</b><br>
https://jersey.java.net/<br>
https://www.ibm.com/developerworks/library/wa-jaxrs/<br>
https://dev.mysql.com/downloads/mysql/<br>
https://dev.mysql.com/doc/refman/en/<br>

# Useful commands

<b>MySQL</b><br>
mysqladmin -u root -p version <b>-- show the version of database</b><br>
mysqlshow -u root -p <b>-- show all the database</b><br>
mysqlshow <i>db_name</i> -u root -p <b>-- show all the tables in the database</b><br>
mysql -h host -u user -p < batch-file > mysql.out <b>-- execute batch of SQL commands and redirect the output to a file</b><br>
mysql -e "select * from <i>table_name</i>" <i>db_name</i> -u root -p <b>-- select all the records from a table of a given db</b><br>


