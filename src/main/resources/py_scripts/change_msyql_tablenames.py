import MySQLdb

SCHEMA = 'autoask_test'
web_product_setting = {
        'host': '127.0.0.1',
        'port': 3306,
        'user': 'autoask',
        'passwd': 'autoask',
        'db': SCHEMA,
        'charset': 'utf8',
}

def get_table_names():
    conn = MySQLdb.connect(**web_product_setting)
    
    cursor = conn.cursor()
    
    try:
        cursor.execute("show tables;")
    except:
        import traceback
        traceback.print_exc()
        
    table_names = [i[0] for i in cursor.fetchall()]
    return table_names

def camel_case(word):
    if not word:
        return ''
    word = str(word)
    word = word.lower()
    seq = word.split('_')
    seq = filter(None, seq)
        seq = map(lambda x: str(x).capitalize(), seq)
        new_str = ''.join(seq)
        return new_str

template = """ 
<table schema="{schema}" tableName="{table_name}" domainObjectName="{domain_object_name}" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false" />
"""

if __name__ == '__main__':
    table_names = get_table_names()
    all_temps = []
    for name in table_names:
        new_name = camel_case(name)
        all_temps.append(template.format(schema=SCHEMA, table_name=name, domain_object_name=new_name))
    print '\n'.join(all_temps)