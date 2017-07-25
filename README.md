# Pocket Tracking #



## Dependencies ##

### [**Java JRE**](http://www.oracle.com/technetwork/java/javase/downloads/index.html) *(v 1.8+)*

### [**Python**](https://www.python.org/downloads/) *(v 3.6+)*

### [**psycopg2**](http://initd.org/psycopg/) - *PostgreSQL adapter for Python (needed for setup.py)*
>run `pip install psycopg2` with Python installed

### Only needed if building from source:

- [**Java JDK**](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (v 1.8+) (only if building from source)

- [**PostgreSQL JDBC driver**](https://jdbc.postgresql.org/download.html) (v 42.1.1+)
  > download the driver and add the jar file to your classpath for the project



## Installation and Running ##
Download the latest release from *`release/`* in the repo and extract the archive.  
[*Set up a PostgreSQL server*](https://www.postgresql.org/docs/9.1/static/runtime.html) and make an empty database called `pt-db`  
Run `setup.py` configured for the database server (`setup.py --help` for usage)  
Move the program folder wherever you like and execute the run script (`run.bat` on Windows, `run.sh` on Linux/Mac).

Configuration files are located in the directory *`config/`*

- `config.ini` - general settings
- `lines.tsv` - line definitions (line ID, width, length, etc.)

The program will not run if these settings are not configured correctly.  
Generally speaking, you should not have to change the `port` setting for the database.  


### Editing the line definitions
`config/lines.tsv` is a list of lines in the plant used to draw the GUI and set up the database. An example is included.  
Alternatively you can use something like Excel to make this file by making a normal spreadsheet and saving it as a TSV:  

<p align="center">

>![spreadsheet](http://i.imgur.com/ulvxVVl.png)  
>*note the first row is optional (anything after a semicolon is ignored by the parser)*
>
>![saving](http://i.imgur.com/2djBqgr.png)  
>*in Excel, the quotes in the file name are required so it doesn't append `.txt` to your file name*

</p>


