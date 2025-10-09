# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[![Sequence Diagram]()](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYFXBjV+2BFkuYO189Q+qpelCugYexHepO+6r+wNmyxhutoKMxuOFdv9lMYeoAVicTizYxzqjz83qY070HqHDUICgxBXMAgADMYJQSw30Bxu9p7X2JwOqEiMBoD4CAIAmFL3D2dKqLUN7BuibrsiO2jct+vYCvcxi1AoHAcJq0raOB45Umh0Gwaq6IKD4eoYsA1HxDyqE0uhgpYTheFUXqo7OoYf4wk8pbYmieJqCBWD8XC-bti8EyGkqSynsCyx0Xq7QQAuCnLJcCaUOuyCpjA4S7vuiqfDAinfCp8RqRp5lae+TaeN4fj+NA7CMjEIpwBG0hwAoMAADIQFkhQbswTrUP6zRtF0vQGOo+RoFmpkoGsvz-BwVxrvcEn+iMKVpfofw7NCjySRFAEujACDBeKGJBSFBJEmApJjrxJHMWRMCMiybIpUxvLQRUWFihKboynKZbvEqX6dfyE5tbU42ESazAAHIQFNL7WjkGhtQtU6lhtbpLigsaJTpUATmFtTpgAjPuh7Hns56lmgm1vDAj6+Jw74IAYMDilQJpIHhcFXhgSAmntPEHZFdQAJJoMDyAcCd0ZnSul3Xfpm7psEMCPfyz2LK99RIyjoMwOD5hQ-9f0A2ur1ESgcM1LUFMg2jAyned8btjjJRgLUO5OITowHsT+ak8WF5GNet582gD7Pq+0AOZ++3lFB-K1D1nHxLR9GMSzE65XUDXihkqhiZg5ts88+VyXMaxWTZ6BZYdgsGUZe7K68zvzCCbvqfWVwfqYXi+AEXgoOgMRxIksfxw1vhYGFgrSY00gRgFEbtBG3Q9PFqiJcMIcLtj5Tm7UIwVx7dtlZQFSLTV9hp-VwVp01agtab2ukbrV7cJRxvaEbqmh2gA0-qow3CiiMwQDQ+HuitCDMD40z0dASAAF4oJ+OvqGzSIiuCPgoqbFQ16n1HW7b9st4dtR32AD8bxi5+nJfXLYxUN0YD3XFjAbMUsTzLE5qjY09F3ZoAZoYJmssoCzUGvNZ+g5RQXyvvtG+TcLZd3vqJDejdYTNwwezN+H8wBf2wX-AWADcbCxgKLEBYDczS0LMg2oV5VA3jvIlFWL5kEa1QbPU+LoyA+HQizfst9CHv2IeJfBDsCHtyITbT+Uj0KtS9owoWtRfZEw4RAsYDVGR4TTjAG26FHwgQQJYURTlo7+BRHhfw2BxQagCmiGAABxJUGgM4VSin4-ORd7BKnLrAqeVca51xiQuUqZCrrlFbmieqaIe45E9oOCox8YLIByBPayU8Z7oXnqKcUeEAk5kwKtWUIYYAokJL3MRFSKFnzodfau+DX5ZKUaQgSqj+k5GobQn+KJdHw29puIBu42FnhEaePQeEWnNQQQqbeepd4HyPoPE+nSXTfxSDg2GeCUmjMUZo5RKSRk+LGUoiZpz6F6PKIA4BwxQFLLfKeLeVldmH02UgrsBSJFDhOb-Hpt8Bk3KGXCe5sKxLPN-tMmoszmGsK+eMV6BYxi8P4UrIRasoCiLBUcoc2joKyL4n0h51zH4qIoc8el4yqX8jReQ95TDDHGX9ssSJOYCwNHGIKlACNpAFjuuEYIgQQSbHiLqFAbpOR7G+MkUAaoVXyUWN8MVa0lS6ouDATo4dHJRxchwAA7G4JwKAxb+AjMEOA3kABs8AKKGFqYYIoTDM4v0aK0DoESonbNKQuLM+qlS5PRb0y5Iwo1zGScMtJPFyLwRQBiY4dZslgBjaIfJByYJwTkJmxNf8wXlCwgAWXDP4maDTVnNJQK0nI7ShoUqwZMsQuC40CVqHAT11D4VcukoOjN4zIVTP-tygx8yxbYp+erFZTT1m902f8net49ntvQamzBU6e3nL7XCAdQ7BlP33ezcdpbJ10M5VdfRBlPn+yXaSv5YbAW7HDv9RBh0YDM3JVerp3boV9JveiYdl6x3npuSi6dDDZ3PqcHdRZuLTwEsVveJ8wi3xmv2XNQ5wHJHSOpW1OR4HYOMrucyuoEGUBstIxymdN0jH8rGGKiVUqZWBBgPhyOzkAiWBQKBCAmwE5IASGAYTonxMACkIDinrXMGIGqQBql9ULf18N6jNGZLFHoYromTwjaMbACBgDCagHACANUoBrE49IGNzcT2O0c8mhFxGhwACtFNoAxL5q2LbmpooLQPQjMFAv+cc+UoaVbhSjRqQ2jejS1nBbaUBxah6wOXIU0FuF0GX55bQHe7tD6MVpgWYu9Dywm1rpyJs5kn7t2H13URrL3Te012K1BplV7ng9aeYe8rT65kvu+TViYzX95Ap-QDb1MART2La3PTt2Wut9MGwVvr0ktvIuGyxnl8yCaLqepwsYJpZwwFrGHMwv7-y6UA0W8FXaXk5f7XtkhhWdOfZoQdxDgCsWvrO6YzDAiCjEpEfxzLab2XqBpTlTbfnes0f63UX7GI4eqBG0hzcbHQH8ecS5LwlmJNSZJ-KRAwZYDAGwOZwgeQIeafMNp9m2dc750Lr0YwcS+kjA8y5xaw8mQMfS8U0LKhC0RZ4SJkXK3KkAHViCGB5y2dUD2OzIPl2tzrx6a7C8gxenbL8Dei7g-9t5N1RbGKPNLN9expAK3B8rHDJKyXPc7Vj97p7Tco+GbRmXI8zfIqxzj1jfL+eNk-EAA)

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
