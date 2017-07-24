# Pocket Tracking #

## Dependencies ##

### To run: ###
- [**Java JRE**](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (v 1.8+)

- [**Python**](https://www.python.org/downloads/) (v  3.6+)

### To build ###
everything above, plus:

- **psycopg2** - PostgreSQL adapter for Python
	>run `pip install psycopg2` with Python installed

- **PostgreSQL JDBC driver** (v 42.1.1+)
	>[download link](https://jdbc.postgresql.org/download.html) (jdbc.postgresql.org)  
	> add the jar file to your Java project as a library


## Installation and Running ##
Download the latest release from *`release/`* in the repo and extract the archive.  
Run `setup.py` on the database server (`setup.py --help` for usage)  
Move the program wherever you like and execute the run script (`run.bat` on Windows, `run.sh` on Linux/Mac).

Configuration files are located in the directory `root/config/`, with the main configuration as `config.ini`.  
The program will not run if these settings are not configured correctly.  
Generally speaking, you should not have to change the `port` setting for the database.  

