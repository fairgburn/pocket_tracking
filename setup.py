#!/usr/bin/python3
#
# Brandon Fairburn July 24, 2017
#
# setup.py: Run to initialize database and after any changes to a line
#           Edit `config/lines.tsv` before running
#
# If setup.py exits at any time before completion, no changes are saved
# in the database.



description_string = '\n'.join(
    [ "Initialize the database",
      " * reads database info and line definitions from `config/` directory",
      " * DELETES ALL DATA FROM THE DATABASE before recreating it as an empty set",
      " * line definitions (`config/lines.tsv`) is a tab-separated list of lines used",
      "     to initialize the database for all the lines and zones in the plant.",
      "     Edit this before running setup.py.",
      " * configurable with the following options:" ])

import configparser
import argparse

try:
    import psycopg2
    import psycopg2.extras
except:
    print("couldn't load module psycopg2!\n(try running 'pip install psycopg2')")
    exit(1)

# default path to config file and line definitions
default_config_path = './config/config.ini'
default_linedefs_path = './config/lines.tsv'

# command line arguments
clargs_p = argparse.ArgumentParser(formatter_class=argparse.RawDescriptionHelpFormatter,
                                   description=description_string)
clargs_p.add_argument('-I', '--inv-only', help='only generate inventory table', action='store_true')
clargs_p.add_argument('-L', '--lines-only', help='only generate lines table', action='store_true')
clargs_p.add_argument('-f', '--force', help='force run (don\'t ask before altering database)', action='store_true')
clargs_p.add_argument('-c', '--config',
                      help='provide a non-default path to config file (relative to setup.py location)',
                      metavar='PATH',
                      default=default_config_path)
clargs_p.add_argument('-l', '--line-defs',
                      help='provide a non-default path to line definitions (relative)',
                      metavar='PATH',
                      default=default_linedefs_path)

# change file paths if user specified them
clargs = vars(clargs_p.parse_args())
config_path = clargs['config']
linedefs_path = clargs['line_defs']

# display a warning if user is not forcing the run
if not clargs['force']:
    leave = input('WARNING: this will irrevocably change the database. Continue? (y/n) ')
    if leave.lower() not in ('y', 'yes'):
        exit(0)

        

##################################
# Getting ready (always do this)
##################################



# read database info from config.ini
print('reading settings...')

cfg = configparser.ConfigParser()
try:
    cfg.read(config_path)
    database = str(cfg.get('database', 'database'))
    host = str(cfg.get('database', 'host'))
    port = str(cfg.get('database', 'port'))
    user = str(cfg.get('database', 'user'))
    password = str(cfg.get('database', 'password'))
except:
    print("error reading config file `{}` (either missing something or couldn't open file)".format(config_path))
    exit(0)
    
# connect to database
print('connecting to database...')

try:
    conn = psycopg2.connect(database=database, host=host, port=port, user=user, password=password)
    cur = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
except:
    print("couldn't connect to database; check config files")
    exit(1)



##########################
# Generate the lines table
##########################



def generate_lines_table():
    print('creating lines table...')

    if not clargs['force']:
        print('this will overwrite the lines table, continue? (y/n) ', end='')
        if input().lower() not in ('y', 'yes'):
            print('setup cancelled, no changes written to database'.upper())
            exit(0)
    


    cur.execute("DROP TABLE IF EXISTS lines")
    cur.execute("""CREATE TABLE lines (\
                 id int primary key,\
                 width int,\
                 length int,\
                 num_zones int)""")

    # make a row for each line
    try:
        with open (linedefs_path) as f:
            column = {'id': 0, 'width': 1, 'length': 2, 'num_zones': 3}
        
            for line in f:
                if line[0] in ('\n', ';'):
                    continue
            
                line = line.split(';')[0] # ignore comments
                vals = line.split() # split line on whitespace
                sql = "INSERT INTO lines VALUES ({}, {}, {}, {})".format(
                    vals[ column['id'] ],
                    vals[ column['width'] ],
                    vals[ column['length'] ],
                    vals[ column['num_zones'] ])
                cur.execute(sql)
    except:
        print("error reading line definitions (either bad formatting or couldn't open `{}`)".format(
            linedefs_path))
        exit(1)

    print('done')



#################################
# Generate the inventory table
#################################


def generate_inventory_table():
    print('creating empty inventory table in database...')

    if not clargs['force']:
        print('this will erase all inventory, continue? (y/n) ', end='')
        if input().lower() not in ('y', 'yes'):
            print('setup cancelled, no changes written to database'.upper())
            exit(0)

    #### get the line info ####
    cur.execute("SELECT * FROM lines")

    class Line:
        def __init__(self, lid, nz):
            self.line_id = lid
            self.num_zones = nz

            def __str__(self):
                return str('<Line: id=%d, num_zones=%d>' % (self.line_id, self.num_zones))
    
    db_lines_table = cur.fetchall()

    # make a list of the lines
    line_list = list( Line(row['id'], row['num_zones']) for row in db_lines_table )


    #### create the inventory table ####
    cur.execute("DROP TABLE IF EXISTS inventory")
    cur.execute("""CREATE TABLE inventory (\
                 id int,\
                 zone int,\
                 order_num int,\
                 customer text,\
                 width int,\
                 length int)""")

    #### make a row for each zone ####
    sql = "INSERT INTO inventory (id, zone) VALUES "
    for line in line_list:
        for i in range(line.num_zones):
            sql += "{}, ".format(str( (line.line_id, i + 1)) )
        

    sql = sql.rstrip(', ') # tidy up the end of the string

    
    cur.execute(sql)
    print('done')


    
######################
# program body
######################



need_commit = False
done = False

# only make lines table
if clargs['lines_only']:
    generate_lines_table()
    run_all = False
    need_commit = True
    done = True

# only make inventory table
if clargs['inv_only']:
    generate_inventory_table()
    need_commit = True
    done = True

# run the entire setup
if not done:
    generate_lines_table()
    generate_inventory_table()
    need_commit = True
    done = True

# finish up
if done and need_commit:
    conn.commit()

print('setup completed successfully!!!')
