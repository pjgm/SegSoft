# SegSoft Assignment 1

## Installation Instructions

After installing Apache Tomcat 9, you need to install a few libraries to run to program:
1. Servet API, contained in the Tomcat installation folder (inside lib).
1. [SQLite JDBC][1] (we used version 3.14.2).
1. Four Apache Commons:
	2. [DBCP][2]
	2. [DbUtils][3]
	2. [Logging][4]
	2. [Pool][5]

## Running Instructions

After starting the program you will be presented with a Setup webpage, where you need to create the root user.
To do that, just insert your desired password and click 'Submit'.

Note: Passwords must be strings composed of at leat 8 to 64 characters.

Next you need to Login using the root user credentials, and you will get access to the home page, showing all avaiable operations.
Now you're ready to explore and use the system as you desire.

Note: The way we implemented our system, only the root user can access the operations create_user and delete_user.
Also, the passwords of each account can only be changed by the corresponding user.
After changing the password you will be automatically logged out from the system.


[1]: https://bitbucket.org/xerial/sqlite-jdbc/downloads
[2]: http://commons.apache.org/proper/commons-dbcp/download_dbcp.cgi
[3]: http://commons.apache.org/proper/commons-dbutils/download_dbutils.cgi
[4]: http://commons.apache.org/proper/commons-logging/download_logging.cgi
[5]: http://commons.apache.org/proper/commons-pool/download_pool.cgi