# SegSoft Assignment 1

## Installation Instructions

This project uses [Maven][1] so you can use your IDE of choice and import this repo directly as a maven project or
clone it and run if you have maven installed.

## Running Instructions

After starting the program you will be presented with a Setup webpage, where you need to create the root user.
To do that, just insert your desired password and click 'Submit'.

Note: Passwords must be strings composed of at leat 8 to 64 characters.

Next you need to Login using the root user credentials, and you will get access to the home page, showing all avaiable operations.
Now you're ready to explore and use the system as you desire.

Note: The way we implemented our system, only the root user can access the operations create_user and delete_user.
Also, the passwords of each account can only be changed by the corresponding user.
After changing the password you will be automatically logged out from the system.

[1]: https://maven.apache.org/