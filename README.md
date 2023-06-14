# CGLIB + Mocking Issue

## Problem 
A colleague of mine had an issue when they had tried to mock an implementation class through Spock. 

From the stack trace, it did seem that the issue stemmed from the CGLIB library, although we were not sure of a the cause at that time.

## Investigation
We started out with a sample project to understand why this was happening.

Soon, we figured out the problem - it was due to CGLIB using an internal API call, and this operation is no longer permitted after Java 16. 

Running the test works perfectly well in Java 11.

### References 
1. [https://github.com/cglib/cglib/issues/191](https://github.com/cglib/cglib/issues/191)

## Verification
### Java 11 
Note: [SDKMAN](https://sdkman.io/)  was used to switch Java versions

When we do a `./gradlew test` we see that the tests pass (albeit with a warning)

```sh
➜  spock-cglib git:(main) ✗ sdk use java 11.0.17-amzn

Using java version 11.0.17-amzn in this shell.
➜  spock-cglib git:(main) ✗ java --version
openjdk 11.0.17 2022-10-18 LTS
OpenJDK Runtime Environment Corretto-11.0.17.8.1 (build 11.0.17+8-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.17.8.1 (build 11.0.17+8-LTS, mixed mode)
➜  spock-cglib git:(main) ✗ ./gradlew test
Starting a Gradle Daemon, 5 incompatible Daemons could not be reused, use --status for details

> Task :test
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by net.sf.cglib.core.ReflectUtils$1 (file:/Users/vyvyd/.gradle/caches/modules-2/files-2.1/cglib/cglib-nodep/3.3.0/87271c95d5bc9e37e4981c9593ff14d470b6684b/cglib-nodep-3.3.0.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
WARNING: Please consider reporting this to the maintainers of net.sf.cglib.core.ReflectUtils$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release

BUILD SUCCESSFUL in 6s
3 actionable tasks: 1 executed, 2 up-to-date
```

## Java 17 
Now we see the test fail, the mocked class cannot be created.

```sh
➜  spock-cglib git:(main) ✗ sdk use java 17.0.7-amzn

Using java version 17.0.7-amzn in this shell.
➜  spock-cglib git:(main) ✗ ./gradlew test

> Task :test FAILED

HelloSpockSpec > length of Spock's and his friends' names > HelloSpockSpec.length of Spock's and his friends' names [name: Spock, length: 5, #0] FAILED
    java.lang.ExceptionInInitializerError at TestSpec.groovy:17
        Caused by: net.sf.cglib.core.CodeGenerationException at TestSpec.groovy:17
            Caused by: java.lang.reflect.InaccessibleObjectException at TestSpec.groovy:17

HelloSpockSpec > length of Spock's and his friends' names > HelloSpockSpec.length of Spock's and his friends' names [name: Kirk, length: 4, #1] FAILED
    java.lang.ExceptionInInitializerError at TestSpec.groovy:17
        Caused by: net.sf.cglib.core.CodeGenerationException at TestSpec.groovy:17
            Caused by: java.lang.reflect.InaccessibleObjectException at TestSpec.groovy:17

HelloSpockSpec > length of Spock's and his friends' names > HelloSpockSpec.length of Spock's and his friends' names [name: Scotty, length: 6, #2] FAILED
    java.lang.ExceptionInInitializerError at TestSpec.groovy:17
        Caused by: net.sf.cglib.core.CodeGenerationException at TestSpec.groovy:17
            Caused by: java.lang.reflect.InaccessibleObjectException at TestSpec.groovy:17

HelloSpockSpec > can mock an implementation class FAILED
    java.lang.NoClassDefFoundError at TestSpec.groovy:17
        Caused by: java.lang.ExceptionInInitializerError at TestSpec.groovy:17

4 tests completed, 4 failed

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':test'.
> There were failing tests. See the report at: file:///Users/vyvyd/vyvyd/spock-cglib/spock-cglib/build/reports/tests/test/index.html

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 2m 19s
3 actionable tasks: 3 executed
```

