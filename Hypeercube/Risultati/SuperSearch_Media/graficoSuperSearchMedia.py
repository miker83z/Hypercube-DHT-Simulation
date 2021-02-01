import matplotlib
import csv
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.ticker import FormatStrFormatter



with open("./resultSuperMedia2.csv", newline="", encoding="ISO-8859-1") as filecsv:
    lettore = csv.reader(filecsv,delimiter=",")
    header = next(lettore)
    firstRow = next(lettore)
    secondRow = next(lettore)
    thirdRow = next(lettore)
   
   
    
fig, ax = plt.subplots()


x = header[1:]
x1 = firstRow[1:]
y = np.array(x1, dtype=float)
x2 = secondRow[1:]
y2 = np.array(x2, dtype=float)
x3 = thirdRow[1:]
y3 = np.array(x3, dtype=float)




colors = ["gold", "brown", "orange"]
w=0.2
index = np.arange(len(x))
index2 = [i+w for i in index]
index3 = [i+w for i in index2]

#100
r1=[0,1,2,3,4,5,6]
r2=[18.29,35.9,51.18,91.06,115.7,196,243.9]

#1000
r3=[0,1,2,3,4,5,6]
r4=[4.54,6.8,12.16,21.7,34.56,63.38,120.38]

#10000
r5=[0,1,2,3,4,5,6]
r6=[3.52,4.16,4.46,5.08,7.84,11.92,20.38]

label = ['18.29','35.9','51.18','91.06','115.7','196','243.9']
label1 = ['4.54','6.8','12.16','21.7','34.56','63.38','120.38']
label2 = ['3.52','4.16','4.46','5.08','7.84','11.92','20.38']

for i in range(len(r1)):
    plt.text(x = r1[i]-0.09, y = r2[i]+0.013, s = label[i], size = 7, weight='bold')

for i in range(len(r3)):
    plt.text(x = r3[i]+0.12, y = r4[i]+0.016, s = label1[i], size = 7, weight='bold')

for i in range(len(r5)):
    plt.text(x = r5[i]+0.32, y = r6[i]+0.016, s = label2[i], size = 7, weight='bold')


plt.bar(index, y, w, color=colors[0], label=firstRow[0]) 
plt.bar(index2, y2, w, color=colors[1], label=secondRow[0])  
plt.bar(index3, y3, w, color=colors[2], label=thirdRow[0]) 

ax.yaxis.set_major_formatter(FormatStrFormatter('%.2f'))

plt.xticks(index+w, x, size=11)                    
plt.legend(loc="upper left", title="N.oggetti")
plt.ylabel('Numero di hop effettuati', size=16)
plt.xlabel('Dimensione Ipercubo', size=16)
plt.title('SUPER SET SEARCH - Media di 50 ricerche - Oggetti restituiti: 10 ', size=18)
plt.savefig("resultMedia.png")
plt.show()