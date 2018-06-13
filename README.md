# staruml-infomodel-java
Java project that generates a Centric Information Model XML and HTML file from a StarUML project (mdj) file. Validated for StarUML version 2.1.1.  Used to generate runnable JAR for http://github.com/jkanel/staruml-infomodel.

## Command Line Example
```java -jar com.centric.infomodel.jar "c:/test/myproject.mdj" "c:/target/mydocument.html" "c:/test/transform.xslt"```


## Arguments

-p "{Source StarUML Project Json File Path}"
Fully qualified path of the StarUML project file (*.mdj)

-x "{Xslt File Path}"
Fully qualified path of the Xslt transformation file.

-t (Optional) "{Target File Path}"
Fully qualified path of target file resulting from the Xslt transformation. Instructs the software to generate the target file.

-m (Optional) "Xml File Path}"
Fully qualified path of the Xslt transformation file. Instructs the software to generate the Xml file.

-g (Optional)
Instructs the software to generate an Xml file having the same name as the {Target File Path} with file type of ```xml```.

## Other Notes
1. The folder path for the Html file is used to create an identically named Xml file.
2. Windows file paths are automatlically normalized to used the correct path delimiters.  Forwardslash is replaced with backslash on Windows platforms.
3. Main class is Application.
