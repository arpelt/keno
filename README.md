# Keno application
<br />
## Installing on Windows

* Download Keno zip file and extract all files.
* Download *javax.json-1.0.4.jar* file from http://search.maven.org/
* Copy files *keno_result_file1.txt* and *keno_result_file2.txt* from *\keno-master\keno-master\data\* directory to *\keno-master\keno-master\src* directory.
* Open command prompt and compile Java source code. For example:

```
C:\Users\test>"C:\Program Files\Java\jdk1.8.0_91\bin\javac" -cp C:\Users\test\Downloads\javax.json-1.0.4.jar C:\Users\test\Downloads\keno-master\keno-master\src\keno\*.java
```

* Run Java Keno application from the *scr* directory.

```
C:\Users\test\Downloads\keno-master\keno-master\src>java -cp C:\Users\test\Downloads\javax.json-1.0.4.jar; keno.Keno
```
<br />
### Keno UI
![Keno UI](../master/docs/KenoUI.png)
