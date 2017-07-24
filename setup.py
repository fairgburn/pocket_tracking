#!/usr/bin/python3

import configparser
import argparse

try:
    import psycopg2
    import psycopg2.extras
except:
    print("couldn't load module psycopg2!\n(try running 'pip install psycopg2')")
    exit(1)


# command line arguments
clargs_p = argparse.ArgumentParser()
clargs_p.add_argument('-io', '--inv-only', help='only generate inventory table', action='store_true')
clargs_p.add_argument('-lo', '--lines-only', help='only generate lines table', action='store_true')
clargs_p.add_argument('-f', '--force', help='force run (don\'t ask before doing dangerous stuff)', action='store_true')
clargs = vars(clargs_p.parse_args())



##################################
# Getting ready (always do this)
##################################



# read database info from config.ini
print('reading settings...')

cfg = configparser.ConfigParser()
cfg.read('config/config.ini')

database = str(cfg.get('database', 'database'))
host = str(cfg.get('database', 'host'))
port = str(cfg.get('database', 'port'))
user = str(cfg.get('database', 'user'))
password = str(cfg.get('database', 'password'))

# connect to database
print('connecting to database...')

conn = None
cur = None
try:
    conn = psycopg2.connect(database=database, host=host, port=port, user=user, password=password)
    cur = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
except:
    print("couldn't connect to database; check config files")
    exit(1)



#################################
# Generate the inventory table
#################################


def generate_inventory_table():
    print('creating empty inventory table in database...')

    if not clargs['force']:
        print('this will erase all inventory, continue? (y/n) ', end='')
        if input().lower() not in ('y', 'yes'):
            print('cancelled')
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

    # pull the trigger
    cur.execute(sql)

    print('done')



##########################
#Generate the lines table
##########################



def generate_lines_table():
    print('creating lines table...')

    if not clargs['force']:
        print('this will overwrite the lines table, continue? (y/n) ', end='')
        if input().lower() not in ('y', 'yes'):
            print('cancelled')
            exit(0)
    


    cur.execute("DROP TABLE IF EXISTS lines")
    cur.execute("""CREATE TABLE lines (\
                 id int primary key,\
                 width int,\
                 length int,\
                 num_zones int)""")

    # make a row for each line
    with open ('config/lines.tsv') as f:
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

print('setup completed successfully')
