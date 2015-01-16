Jute: Core Libraries for Java
-------

* Jute - a fiber used in the making of strong, coarse, threads

Summary
-------

* Small library providing a few essential features on top of those provided natively by the JDK
* Heavily influenced by the [Guava](https://github.com/google/guava) team's approach to interacting with the Java language 
* Especially with regards to:
 * [using and avoiding null](https://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained)
 * [immutable collections](https://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained)
 * [functional idioms](https://code.google.com/p/guava-libraries/wiki/FunctionalExplained)

Highlights
-------
* `VirtualSystem` - immutable, strongly typed, object containing system level information about the JVM including:
 * timezone
 * encoding/character set
 * locale
 * system properties and environment variables
 * user
 * jvm specification
* For example to find the home directory for the user the JVM is running as:
```
File home = VirtualSystem.build().getUser().getHome();
```
* `VirtualRuntime` - immutable, strongly typed, object containing runtime information about the JVM including:
 * uptime
 * memory usage
 * process id
 * system load
 * class loading statistics
 * garbage collection events
* For example to find how long the JVM has been running:
```
int pid = VirtualRuntime.build().getUptime().getElapsed();
```
* `Environment` - abstraction for searching the current environment for string values (typically used to override a default value)
* `Encryptor` - password based encryption using AES 128 that is fully compatible with OpenSSL
* `JsonService` - easily read/write data structures as JSON via Jackson
* `Precondition` - argument checking with a meaningful error message that includes the argument name

Dependency Injection
-------
* The project contains Guice Modules capable of wiring everything together using dependency injection
* For example, if you need to encrypt a string and send it over the wire as JSON
```
Injector injector = Guice.createInjector(new SystemModule(), new EnvModule(), new OpenSSLModule(), new JacksonModule());
Encryptor enc = injector.createInstance(Encryptor.class);
JsonService json = injector.createInstance(JsonService.class);

String plaintext = "foobar";
String encrypted = enc.encrypte(plaintext);
String converted = json.writeString(encrypted);
```
