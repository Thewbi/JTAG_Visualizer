# javafx_maven_template

JavaFX maven template

# Install JavaFX on Ubuntu Linux

```
apt-get install openjfx
```

This does not work:
From https://gluonhq.com/products/javafx/ download openjfx-20_linux-aarch64_bin-sdk.zip (or the newer version openjfx-23.0.1_linux-aarch64_bin-sdk.zip)

```
tar -xvf openjfx-20_linux-aarch64_bin-sdk.zip
sudo mv javafx-sdk-20/ /opt/
```

# Create a launch.json file

In Visual Studio Code, click the Run/Debug button inside the left quick bar.
Select: To customize Run and Debug create a launch.json file > Java

# Install maven on Ubuntu Linux

Download apache-maven-3.9.9-bin.tar.gz from https://maven.apache.org/download.cgi

Unzip the archive

```
tar -xvf apache-maven-3.6.3-bin.tar.gz
```

Install the archive into the /opt folder

```
mv apache-maven-3.9.9 /opt/
```

In Visual Studio Code, update the maven setting 'maven.executable.path'
Enter: 

```
/opt/apache-maven-3.9.9/bin/mvn
```

It should now be possible to install the pom.xml file from within Visual Studio Code.



# test

```
<!-- https://stackoverflow.com/questions/574594/how-can-i-create-an-executable-jar-with-dependencies-using-maven -->
<!-- echo %JAVA_HOME% -->
<!-- -->
<!-- Building: -->
<!-- cd C:\aaa_se\wirehippo\wirehippo -->
<!-- SET JAVA_HOME=C:\Program Files\Java\jdk-17.0.1 -->
<!-- SET JAVA_HOME=C:\Program Files\Java\jdk-19 -->
<!-- mvn clean compile assembly:single -->
<!-- jar is output here: C:\aaa_se\wirehippo\wirehippo\target\wirehippo-?.?.?-jar-with-dependencies.jar -->
<!-- -->
<!-- Running: WireHippo requires an installation of the JDK (Java SDK) 17 or newer. -->
<!-- To start, execute the jar file (java -jar ...) or double click the jar file. -->
<!-- -->
<!-- SET JAVA_HOME=C:\Program Files\Java\jdk-17.0.1 -->
<!-- java -jar wirehippo-1.0.0-jar-with-dependencies.jar -->
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <configuration>
        <archive>
            <manifest>
                <mainClass>de.wire.StarterClass</mainClass>
            </manifest>
        </archive>
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
    </configuration>
</plugin>
```


```
{
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "de.template.App",
            "projectName": "wirehippo",
            "vmArgs": "--module-path \"/opt/javafx-sdk-20/lib\" --add-modules javafx.controls,javafx.fxml"
        },
```