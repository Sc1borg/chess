# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[![Sequence Diagram]()](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkEkSYCkm+hi7jS+4MkyU76XOnkjhUy5ihKboynKZbvEqmAqsGGoAHIQJFMBWjaN4BQ6Sa+s6XYwD2fbbigWWgfUiVulGMZxoUoFZaJtTpgAjAROaqHm8yLEWJbdklbwwAAZr4nANvRMWqhq4pUCaSDroeHDmEgJq2re-LFTltRhVubmrRJdQAJJoJNyAcOV0YoLGCnoVAtXIKm6bBDAzX8m1eyddA9T7Yd00wLN82LcNTbDitFRuetM6bTlFSA-SjJgM+8Tnpe15bRUOl1A54oZKoAGYKj201LUelEQZxqXlR6CadUlDXSUYA4XhwwwOB5afLBpOIfWdFNp43h+P4XgoOgMRxIk-OCw5vhYKJgolY00gRvxEbtBG3Q9HJqgKcMFFk9VlNXeUqME1r7NoKZsJU+UIM2fYEv2UJEtOWoLmFZDy30nN3DHoj2gI-Bxv+bygXlMuKIzBANAbu6W6xeqMDpQHQMW2tIrgj4KLOyjZnPOLp6Y9juPA7rtTZ2AucIGAGLJ6cqdcpd1OpjAjUPWgjNjC1z3QR9U3rkb1GcwxUOqHjSJkD4+6Fe2BvF6XgGZ1dBc7UXds5-+ZcYiP+6ubrdfYbh+GjK3T35tBDmMuuEswFj+59b2CCWP9DHc8x-gouu-jYOKGr8WiMAAOJKhoUsLKSR-orFW9glSazZkhWu+tZ6GygeTHGs956WRdMgHI9k0QOxyBTTsLsMr0nQeXHu6B-YLkHkHYUIV1x-xzKNOKscB5DxdJXFIadkawLNmjLBK8Z5cLxlnHhWNV6sOrpvHa29aYNycA1JuLdXpQALBMaYl5oBIAAF4oF2H3OO5DmFdnXiOceWUHhcKLkIvOyCUFZyXiXXha9R4jnETUSRtRd4ES-jkJRDQMTgJzMSTo99GI8wCBwAA7G4JwKAnAxAjMEOAXEABs8AJyGFoYYIoN0yjZQXo0VoHQwEQJUb7JCWY-EoHikqHRj9eaWBQH2CAmwhZIASGAOpDSmkACkIDil-hWfwyRQBqkyTTaWhc8nMhkj0cpkCSnoCzNgBAwA6lQDgBAGyUA1jlN2tIapTFeZeGWc01phz5SIGDLAYA2BFmEDyAUWOgCcn41lvLRWytejGEbAxIAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
