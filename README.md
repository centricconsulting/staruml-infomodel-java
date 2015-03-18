# star-infomodel-java
Java project that generates a Centric Information Model XML and HTML file from a StarUML project (mdj) file. Validated for StarUML version 2.1.1.  Used to generate runnable JAR for http://github.com/jkanel/staruml-infomodel.

## Command Line Example
```java -jar com.centric.infomodel.jar "c:/test/myproject.mdj" "c:/target/mydocument.html" "c:/test/transform.xslt"```


## Arguments
arg[0] = Absolute path of the StarUML project file (*.mdj)

arg[1] = Absolute path of the target Html file.

arg[2] = Absolute path of the Xslt transformation file.

## Other Notes
1. The folder path for the Html file is used to create an identically named Xml file.
2. Windows file paths are automatlically normalized to used the correct path delimiters.  Forwardslash is replaced with backslash on Windows platforms.
3. Main class is Application.
