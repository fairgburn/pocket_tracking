#!/usr/bin/python3

import configparser
try:
    import psycopg2
    import psycopg2.extras
except:
    print("couldn't load module psycopg2!\n(try running 'pip install psycopg2')")
    exit(1)


print('this will erase all inventory, continue? (y/n) ', end='')
if input().lower() not in ('y', 'yes'):
    print('cancelled')
    exit(0)
 
    


# read database info from .ini file
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
    print("couldn't connect to database; check settings.ini\n")
    exit(1)


#### get the line info ####
print('querying line info...')

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
print('creating empty inventory table in database with these zones:\n')

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
        print(line, 'zone', i+1)
        sql += "{}, ".format(str( (line.line_id, i + 1)) )
        

sql = sql.rstrip(', ') # tidy up the end of the string

# pull the trigger
cur.execute(sql)


#### commit changes, done ####
conn.commit()

print('\ndone\n')
