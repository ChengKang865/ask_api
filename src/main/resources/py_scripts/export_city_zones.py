import sys
reload(sys)
sys.setdefaultencoding('utf8')

import json

import MySQLdb

SCHEMA = 'autoask'
web_product_setting = {
        'host': '121.40.107.160',
        'port': 3306,
        'user': 'autoask',
        'passwd': 'autoask',
        'db': SCHEMA,
        'charset': 'utf8',
}

def get_zone_dict():
    conn = MySQLdb.connect(**web_product_setting)
    
    cursor = conn.cursor()
    
    try:
        cursor.execute("select c1.areaName, c2.areaName, c3.areaName from city_area c1 LEFT JOIN "
                       "city_area c2 on c1.id = c2.parent_id LEFT JOIN city_area c3 on c2.id = c3.parent_id where c1.level = 1;")
    except:
        import traceback
        traceback.print_exc()
    
    zone_dict = {}
    for province, city, region in cursor.fetchall():
        zone_dict.setdefault(province, {}).setdefault(city, []).append(region)
    return zone_dict


if __name__ == '__main__':
    f = open('region.json', 'wb')
    json.dump(get_zone_dict(), f, ensure_ascii=False)
    f.close()