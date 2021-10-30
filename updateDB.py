import pymysql
import json

print('try to access mysql...')
passwd = input('please input password')
database = pymysql.connect(
        user='root',
        passwd=passwd,
        db='parkingAppDB',
        charset='utf8'
        )
cursor=database.cursor()

with open('parkingArea.json','r') as json_file:
        data = json.load(json_file)

field = data['fields']

row = data['records']
temp=[]
print(len(row))
print(len(field))

for i in range(0,len(row)):
    temp.clear()
    for j in range(0,len(field)):
       index=field[j]['id']
       if(index in row[i].keys()):
           print('키존재')
       else:      
           break

       if((index=='위도' or index=='경도') and row[i][index]==''):
           print('위도에러')
           break 

       res=row[i][index]
       temp.append(res)
       if(j==len(field)-1):
           print("work")
           cursor.execute("INSERT INTO parkingArea VALUES(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,CAST(%s AS DECIMAL(13,10)),CAST(%s AS DECIMAL(13,10)),%s,%s,%s);", (temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],temp[6],temp[7],temp[8],temp[9],temp[10],temp[11],temp[12],temp[13],temp[14],temp[15],temp[16],temp[17],temp[18],temp[19],temp[20],temp[21],temp[22],temp[23],temp[24],temp[25],temp[26],temp[27],temp[28],temp[29],temp[30],temp[31],temp[32]))              #  print(cursor.fetchone())

print(cursor.fetchall())
database.commit()
database.close()
