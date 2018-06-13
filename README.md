# staruml-infomodel-java
Java project that generates a Centric Information Model Library Xml and text file from a StarUML project (mdj) file. Validated for StarUML version 3.0.0.  Used to generate runnable JAR for http://github.com/centricconsulting/staruml-infomodel.

## Command Line Arguments

-p "{Source StarUML Project Json File Path}"
Fully qualified path of the StarUML project file (*.mdj)

-x "{Xslt File Path}"
Fully qualified path of the Xslt transformation file.

-t (Optional) "{Target File Path}"
Fully qualified path of target file resulting from the Xslt transformation. Instructs the software to generate the target file.

-m (Optional) "{Xml File Path}"
Fully qualified path of the Xslt transformation file. Instructs the software to generate the Xml file.

-g (Optional)
Instructs the software to generate an Xml file having the same name as the {Target File Path} with file type of ```xml```.

## Command Line Examples

Example 1: Generates a library text file (result.txt) using the transformation file (transform.xslt)
```java -jar com.centric.infomodel.jar -p "c:/test/myproject.mdj" -x "c:/test/transform.xslt" -t "c:/Temporary/result.txt"```

Example 2: Generates both a library text file (result.txt) and like-named Xml (result.xml) using the transformation file (transform.xslt)
```java -jar com.centric.infomodel.jar -p "c:/test/myproject.mdj" -x "c:/test/transform.xslt" -t "c:/Temporary/result.txt" -g```

Example 3: Generates an Xml file (result2.xml) only
```java -jar com.centric.infomodel.jar -p "c:/test/myproject.mdj" -m "c:/Temporary/result2.xml"```


## Other Notes
1. Windows file paths are automatlically normalized to used the correct path delimiters.  Forwardslash is replaced with backslash on Windows platforms.
2. Entry point is the static Application class.