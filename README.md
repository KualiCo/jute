Jute: Kuali Core Libraries for Java
=====================================

* Jute - a fiber used in the making of strong, coarse, threads

Summary
-------

* Small library providing a few essential features on top of those provided natively by the JDK
* Heavily influenced by the [Guava](https://github.com/google/guava) team's approach to interacting with the Java language 
* Especially with regards to:
 * [using and avoiding null](https://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained)
 * [immutable collections](https://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained)
 * [functional idioms](https://code.google.com/p/guava-libraries/wiki/FunctionalExplained)

System
-------
* Provide an immutable and strongly typed object containing essential information about the JVM we are running on
* For example to get the home directory of the user the JVM is running as:
```
VirtualSystem vs = VirtualSystem.build();
User user = vs.getUser();
File home = user.getHome();
```



