# sbt-pom-build-plugins
Adds build time plugin dependencies to the pom file build section

# Install
```sbt
addSbtPlugin("com.github.jacoby6000" % "sbt-pom-build" % "1.0.2")
```

# Configuration

* Set `pomBuildPluginsIgnoreNames` to ignore specific plugins by their artifact name. `sbt-bloop` is included there by default. Any valid regex works.
* Set `pomBuildPluginsIgnoreClasses` to ignore specific plugins by their class name. This is an empty set by default.  Any valid regex works.
* Set `pomBuildPluginsIgnoreDirs` to ignore plugins that exist within certain directories.  By default the sbt boot plugin is included here, as well as the `./project` directory.

## Example Config:
```sbt
lazy val myProject = project.in("foo").enablePlugins(SbtPomBuild).settings(
  pomBuildPluginsIgnoreNames ++= Set("sbt-dependency-graph"), // Do not include sbt-dependency-graph in the pom build plugins list
  pomBuildPluginsIgnoreClasses ++= Set("^[^.]+$"), // ignore any plugins if its package has no dots (its in the root project).
  pomBuildPluginsIgnoreDirs ++= Seq(file("~/.ivy2/cache/org.my-org/")) // Do not include any ivy dependencies from `org.my-org` in the pom build plugins list
)
```

## Example output

Note the `build` section, normally not included in sbt pom outputs.

```xml
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>simple</groupId>
  <artifactId>simple_2.12</artifactId>
  <packaging>jar</packaging>
  <description>simple</description>
  <version>0.1.0-SNAPSHOT</version>
  <name>simple</name>
  <organization>
    <name>simple</name>
  </organization>
  <dependencies>
    <dependency>
      <groupId>org.scala-lang</groupId>
      <artifactId>scala-library</artifactId>
      <version>2.12.14</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>com.jacoby6000.sbt</groupId>
        <artifactId>sbt-pom-build</artifactId>
        <version>0.1.0-SNAPSHOT</version>
      </plugin>
    </plugins>
  </build>
</project>
```

# Verifying that the plugin is functioning in your builds
This plugin functions by augmenting the `makePom` step, running an additional step afterward to append the build section
of the pom file.

To test your own builds, simply run `sbt makePom` and then check the contents of the file that is generated.
