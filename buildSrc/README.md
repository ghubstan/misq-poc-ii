# buildSrc

The buildSrc directory is treated as an included build. Gradle automatically compiles buildSrc/build.gradle and puts it
in the classpath of Misq project build scripts. There can be only one buildSrc directory, located in the root project
directory.

This buildSrc/build.gradle file is used to tailor gradle tasks run by other Misq build scripts in one place; they can be
overridden.

Common but not ubiquitous tasks can be defined in buildSrc as well. For example, many but not all modules will generate
protobuf sources. Module build files can share the `protobuf` task as needed by including the line

```asciidoc
apply from: '../buildSrc/gen-protos.gradle'
``` 

