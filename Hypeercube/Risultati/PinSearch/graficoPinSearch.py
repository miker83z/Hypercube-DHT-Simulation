import matplotlib
import csv
import matplotlib.pyplot as plt
import numpy as np


with open("./result7.csv", newline="", encoding="ISO-8859-1") as filecsv:
    lettore = csv.reader(filecsv,delimiter=",")
    header = next(lettore)
    firstRow = next(lettore)
    secondRow = next(lettore)
    thirdRow = next(lettore)
   


x = header[2:]
x1 = firstRow[2:]
y = np.array(x1, dtype=int)
x2 = secondRow[2:]
y2 = np.array(x2, dtype=int)
x3 = thirdRow[2:]
y3 = np.array(x3, dtype=int)


colors = ["green", "blue", "yellow"]
w=0.2
index = np.arange(len(x))
index2 = [i+w for i in index]
index3 = [i+w for i in index2]


plt.bar(index, y, w, color=colors[0], label = firstRow[1]) 
plt.bar(index2, y2, w, color=colors[1], label = secondRow[1])  
plt.bar(index3, y3, w, color=colors[2], label = thirdRow[1]) 



plt.xticks(index+w, x, size=11)                    
plt.legend(loc="upper right", title="N.oggetti")
plt.ylabel('Numero di hop effettuati', size=16)
plt.xlabel('Numero di ricerche effettuate', size=16)
plt.title('Ricerca PIN SEARCH - Dimensione Ipercubo: 8192', size=20)
plt.savefig("resultSingolo.png")
plt.show()