# P2P-Project

This application aims to provide mobile service for money transfer among friends.<br>

High-level use cases overview:<br>
1. User download the mobile application from Google Play<br>
2. Register an account and add personal bank account<br>
3. Invite other users to participate in transaction. User can manage his/her contact list.<br>
4. Select a friend and transfer money<br>
5. After successfully transferred the money, receiver will receive a push notification.<br>
<br>
This is initially developed by myself and my group teammate (Yeung Mei Yan Becky and Chan Yee Kwan) as the final project of the course - <u>COMP5527 Mobile Computing and Data Management</u> during our Master Study in HK Polytechnic University.
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
- MySQL Server Community Edition 5.6.21 or 5.7.11 (root@localhost: 123456)
- hibernate-search-orm 4.5.1
- hibernate-core 4.3.1
- hibernate-manager 4.3.1
- lucene-core 3.5.0
- mysql-connector-java 5.1.38
<br>

<b>Useful commands</b><br>
mysqladmin -u <i>db_user_name</i> -p version <b>-- show the version of database</b><br>
mysqlshow -u <i>db_user_name</i> -p <b>-- show all the database</b><br>
mysqlshow <i>db_name</i> -u <i>db_user_name</i> -p <b>-- show all the tables in the database</b><br>
mysql -h host -u <i>db_user_name</i> -p < batch-file > mysql.out <b>-- execute batch of SQL commands and redirect the output to a file</b><br>
mysql -e "select * from <i>table_name</i>" <i>db_name</i> -u <i>db_user_name</i> -p <b>-- select all the records from a table of a given db</b><br>

<b>References:</b><br>
https://dev.mysql.com/downloads/mysql/<br>
https://dev.mysql.com/doc/refman/en/<br>
https://docs.jboss.org/hibernate/search/4.2/reference/en-US/html/search-configuration.html#search-configuration-event<br>
https://docs.jboss.org/hibernate/search/3.3/reference/en-US/html/search-query.html<br>
https://docs.jboss.org/hibernate/search/3.2/reference/en/html/manual-index-changes.html<br>

# Android Mobile App

<b>Development Environment</b>
- JDK 1.8.0.73
- Maven 3.0.5
- Eclipse IDE Mars.2
- Android SDK 19
- Android Support Library v4 r23
- Google Play Service 8.1.0
- Jackson JSON library 2.6.1
<br>

<b>Release:</b><br>
https://play.google.com/apps/testing/hk.edu.polyu.P2pMobileApp <b>Subscribed as beta tester</b><br>
https://play.google.com/store/apps/details?id=hk.edu.polyu.P2pMobileApp <b>Live</b><br>

<b>References:</b><br>
http://developer.android.com/index.html<br>

# Linux

We encountered an unsolvable issue with hibernate indexing where SQL query will just hung when the server has been running for 1-2 days. As a workaround to this issue, we have to create a cronjob on linux so that it reboot each day around 5am, then upon the startup of tomcat web server, we executed a dummy web service request (via CURL) so that it will instantiate the re-indexing of hibernate.<br> 

<b>Dummy Web Service Request</b><br>
http://ec2-52-10-73-179.us-west-2.compute.amazonaws.com:8080/P2pWebServices/rest/hello/user<br>

<b>References:</b><br>
https://www.liferay.com/web/brett.swaim/blog/-/blogs/sample-tomcat-startup-scripts<br>
https://shabirimam.wordpress.com/2009/03/10/adding-script-to-run-at-startup-or-shutdown<br>
http://serverfault.com/questions/155239/how-can-i-schedule-a-reboot-in-linux<br>

# Amazon Free Web Hosting Service

We use AWS (Amazon Web Service) Free Tier which is free available for 12 months, 750 hours per month of Linux virtual server (EC2 instance). Our web apps is structured into 2 tiers, the first tier is an application server, which is responsible for the web service functionality. The second tier is a database server, which is responsible for data storage, while the mobile app is for presenting the user interface and providing limited offline functionality.<br>

<b>AWS SSH command</b>
- ssh -i "comp5527.pem" ec2-user@ec2-52-10-73-179.us-west-2.compute.amazonaws.com<br>
- port 22<br>

<b>AWS SSH file transfer</b>
- scp -i "comp5527.pem" <i>local_file_path</i> ec2-user@ec2-52-10-73-179.us-west-2.compute.amazonaws.com:<i>remote_file_path</i><br>

<b>AWS Management Console</b><br>
- https://polyu-comp5527.signin.aws.amazon.com/console<br>

<b>Amazon Linux AMI Test Page</b> 
- http://ec2-52-10-73-179.us-west-2.compute.amazonaws.com<br>
- port 80<br>

<b>Tomcat Page</b>
- http://ec2-52-10-73-179.us-west-2.compute.amazonaws.com
- port 8080<br>

<b>Amazon RDS DB instance</b>
- mydbinstance.cenmpokbqyfp.us-west-2.rds.amazonaws.com<br>
- port 3366<br>

<b>References:</b><br>
https://aws.amazon.com/free/<br>
https://aws.amazon.com/ec2/<br>
http://docs.aws.amazon.com/gettingstarted/latest/wah-linux/web-app-hosting-intro.html<br>
http://docs.aws.amazon.com/gettingstarted/latest/awsgsg-intro/gsg-aws-tutorials.html<br>
