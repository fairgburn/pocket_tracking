# Pocket Tracking #



## Dependencies ##

### [**Java JRE**](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (v 1.8+)

### [**Python**](https://www.python.org/downloads/) (v  3.6+)

### **psycopg2** - PostgreSQL adapter for Python (needed for setup.py)
>run `pip install psycopg2` with Python installed

### Only needed if building from source:

- [**Java JDK**](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (v 1.8+) (only if building from source)

- [**PostgreSQL JDBC driver**](https://jdbc.postgresql.org/download.html) (v 42.1.1+)
> download the driver and add the jar file to your classpath for the project



## Installation and Running ##
Download the latest release from *`release/`* in the repo and extract the archive.  
Run `setup.py` on the database server (`setup.py --help` for usage)  
Move the program wherever you like and execute the run script (`run.bat` on Windows, `run.sh` on Linux/Mac).

Configuration files are located in the directory *`root/config/`*, with the main configuration as `config.ini`.  
The program will not run if these settings are not configured correctly.  
Generally speaking, you should not have to change the `port` setting for the database.  


