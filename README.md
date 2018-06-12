# codingchallengetest

#I have added required dependecies in the pom.xml file.
#Database set up :
#MAC OS :
#SQLite is included in the mac os. What we have to do is run the sqlite3 command to check the version.
#Run this command to create database # sqlite3 <databasename>.db
#And run the DDL script to create table in that database.
  
#Windows : 
# We need to download sqlite binaries from website and run the same commands.

Database configuration in code :
#Created connection object to establish connetion between java code and database."jdbc:sqlite:/Users/chandu/sqlite/test.db" this the URL connection string in my local system.

# Reading CSV file
#I have added the dependency apache csv jar in pom.xml to get the required calsses that needs to be used to read the data from each row and cell of the input file.
#I used for loop to iterate through each row.

# Writng CSV file
# I have created required objects and created new file to send the bad data using apache csv classes.

#Log file
# Added logger statements in the code log the different kind of records.
