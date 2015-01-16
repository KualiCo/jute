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
* `VirtualSystem` - immutable, strongly typed, object containing system level information
 * system properties and environment variables
 * encoding/character set
 * user
 * timezone
 * locale
 * java version
* For example to find the home directory for the user the JVM is running as:
```
File home = VirtualSystem.build().getUser().getHome();
```
* `VirtualRuntime` - immutable, strongly typed, object containing runtime information
 * uptime
 * memory usage
 * process id
 * system load
 * class loading statistics
 * garbage collection events
* For example to find how long the JVM has been running:
```
long uptime = VirtualRuntime.build().getUptime().getElapsed();
```
* `Environment` - abstraction for locating string values (typically used to override a default value)
* `Encryptor` - password based encryption using AES 128, fully compatible with OpenSSL
* `JsonService` - easily read/write data structures as JSON via Jackson
* `Precondition` - argument checking with a meaningful error message that include the argument name

Dependency Injection
-------
* Guice Modules capable of wiring everything together via dependency injection are included
* For example, if you need to encrypt a string and send it over the wire as JSON
```
List<AbstractModule> modules = Lists.newArrayList();
modules.add(new SystemModule());
modules.add(new EnvModule());
modules.add(new OpenSSLModule());
modules.add(new JacksonModule());
Injector injector = Guice.createInjector(modules);
Encryptor enc = injector.createInstance(Encryptor.class);
JsonService json = injector.createInstance(JsonService.class);

String plaintext = "foobar";
String encrypted = enc.encrypt(plaintext);
String asJson = json.writeString(encrypted);
```
