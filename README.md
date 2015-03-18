# star-infomodel-java
Java project that generates a Centric Information Model XML and HTML file from a StarUML project (mdj) file. Validated for StarUML version 2.1.1.  Used to generate runnable JAR for http://github.com/jkanel/staruml-infomodel.

<b>MAIN CLASS</b>
Main class is Application.

<b>ARGUMENTS</b>
arg[0] = Absolute path of the StarUML project file (*.mdj)
arg[1] = Absolute path of the target Html file.  
arg[2] = Absolute path of the Xslt transformation file.

<b>OTHER NOTES</b>
1. The folder path for the Html file is used to create an identically named Xml file.
