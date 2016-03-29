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
<br>

<b>API</b>
<br>
https://docs.google.com/document/d/1RyXXQ-zcGm4rnN19AYb6j3E4v6uj2yToCrBW6-jeSfY/edit?usp=sharing
<br>

<b>References:</b><br>
https://jersey.java.net/<br>
https://www.ibm.com/developerworks/library/wa-jaxrs/<br>

# MySQL Database Server

<b>Development Environment</b>
- MySQL Server Community Edition 5.6.21 or 5.7.11 (root@localhost: 123456 / u3CXCF?JBkWS)
- hibernate-search-orm 4.5.1
- hibernate-core 4.3.1
- hibernate-manager 4.3.1
- lucene-core 3.5.0
- mysql-connector-java 5.1.38
<br>

<b>Useful commands</b><br>
mysqladmin -u root -p version <b>-- show the version of database</b><br>
mysqlshow -u root -p <b>-- show all the database</b><br>
mysqlshow <i>db_name</i> -u root -p <b>-- show all the tables in the database</b><br>
mysql -h host -u user -p < batch-file > mysql.out <b>-- execute batch of SQL commands and redirect the output to a file</b><br>
mysql -e "select * from <i>table_name</i>" <i>db_name</i> -u root -p <b>-- select all the records from a table of a given db</b><br>

<b>References:</b><br>
https://dev.mysql.com/downloads/mysql/<br>
https://dev.mysql.com/doc/refman/en/<br>
https://docs.jboss.org/hibernate/search/4.2/reference/en-US/html/search-configuration.html#search-configuration-event<br>
https://docs.jboss.org/hibernate/search/3.3/reference/en-US/html/search-query.html<br>
https://docs.jboss.org/hibernate/search/3.2/reference/en/html/manual-index-changes.html<br>

# Amazon Free Web Hosting Service
We use AWS (Amazon Web Service) Free Tier which is free available for 12 months, 750 hours per month of Linux virtual server (EC2 instance). Our web apps is structured into 2 tiers, the first tier is an application server, which is responsible for the web service functionality. The second tier is a database server, which is responsible for data storage, while the mobile app is for presenting the user interface and providing limited offline functionality.

<b>References:</b><br>
https://aws.amazon.com/free/<br>
https://aws.amazon.com/ec2/<br>
http://docs.aws.amazon.com/gettingstarted/latest/wah-linux/web-app-hosting-intro.html<br>
http://docs.aws.amazon.com/gettingstarted/latest/awsgsg-intro/gsg-aws-tutorials.html<br>
