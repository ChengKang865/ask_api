from collections import Counter

import pymysql

TABLE_TYPE_PREFIX = 'com.autoask.entity.mysql'
TABLES = ['card']

SCHEMA = 'autoask'
web_product_setting = {
    'host': '121.40.107.160',
    'port': 3306,
    'user': 'autoask',
    'passwd': 'autoask',
    'db': SCHEMA,
    'charset': 'utf8',
}

JDBC_TYPE_MAPPER = {
    'varchar': 'VARCHAR',
    'bigint': 'BIGINT',
    'tinyint': 'BOOLEAN',
    'decimal': 'DECIMAL',
    'timestamp': 'TIMESTAMP',
    'date': 'DATE',
    'time': 'TIME',
    'double': 'DOUBLE',
    'int': 'INTEGER',
    'smallint': 'SMALLINT'
}


def camel_case(word, capitalize=True):
    if not word:
        return ''
    word = str(word)
    word = word.lower()
    seq = word.split('_')
    seq = [_f for _f in seq if _f]
    seq = [str(x).capitalize() for x in seq]
    new_str = ''.join(seq)
    if not capitalize:
        return new_str[0].lower() + new_str[1:]
    return new_str


def get_table_columns(table):
    conn = pymysql.connect(**web_product_setting)
    cursor = conn.cursor()
    cursor.execute("desc %s;" % table)
    fields = cursor.fetchall()
    return fields

column_sql_template = """<sql id="{sql_id}">
   {columns}
</sql>
"""

column_xml_template = """<result column="{column_name}" jdbcType="{jdbc_type}" property="{property}"/>
"""

table_xml_template = """<resultMap id="{table_map_id}" type="{TABLE_TYPE_PREFIX}">
    {column_xml_template}</resultMap>
"""


def main():
    out_put = []

    for table in TABLES:
        columns = get_table_columns(table)
        column_xml_list = []
        new_column_names = []
        for column in columns:
            column_name = column[0]
            new_column_name = '_' + table + '_' + column[0]
            column_type = column[1].split('(')[0]
            jdbc_type = JDBC_TYPE_MAPPER[column_type]

            column_xml_list.append(column_xml_template.format(column_name=new_column_name, jdbc_type=jdbc_type,
                                                              property=camel_case(column_name, False)))
            new_column_names.append(table + '.' + column_name + ' as ' + new_column_name)

        table_xml = table_xml_template.format(table_map_id=table + '_map', column_xml_template=''.join(column_xml_list),
                                              TABLE_TYPE_PREFIX=TABLE_TYPE_PREFIX + '.' + camel_case(table))
        out_put.append(table_xml)

        columns_str = ', '.join(new_column_names)
        sql_id = table + '_fields'
        out_put.append(column_sql_template.format(sql_id=sql_id, columns=columns_str))
    print(''.join(out_put))


if __name__ == '__main__':
    main()
