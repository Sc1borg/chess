# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[![Sequence Diagram]()](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYFXBjV+2BFkuYO189Q+qpelCugYexHepO+6r+wNmyxhutoKMxuOFdv9lMYeoAVicTizYxzqjz83qY070HqHDUICgxBXMAgADMYJQSw30Bxu9p7X2JwOqEiMBoD4CAIAmFL3D2dKqLUN7BuibrsiO2jct+vYCvcxi1AoHAcJq0raOB45Umh0Gwaq6IKD4eoYsA1HxDyqE0uhgpYTheFUXqo7OoYf4wk8pbYmieJqCBWD8XC-bti8EyGkqSynsCyx0Xq7QQAuCnLJcCaUOuyCpjA4S7vuiqfDAinfCp8RqRp5lae+TaeN4fj+NA7CMjEIpwBG0hwAoMAADIQFkhQbswTrUP6zRtF0vQGOo+RoFmpkoGsvz-BwVxrvcEn+iMKVpfofw7NCjySRFAEujACDBeKGJBSFBJEmApJjrxJHMWRMCMiybIpUxvLQRUWFihKboynKZbvEqzaqhqAByEBTS+1o5LapEOn+bX1MBoFESgE7STAi1ukuKCxolOlQBOYW1OmACM+6HseeznqWaBLW8MCPr4nAOZ+LbqjA4pUCaSB4XBV4YEgJrrZ1-KHTxtTjYRbWI5FdQAJJoKDyAcKd0bnSuV03fpm7psEMBPfyL2LG99TY7j4MwJD5gwwgqj-bNrb-rpb1fvD6jo5VQ4o-I+3C88jNg-jAxnRd8btqTJRgLUO5OFTowHjT+Z08WF5GNet4K2gD7Pq+0Bc1BCMVNtYvABL5TW-SPWcfEtH0YxEsVLldQNeKGSqGJmC+5LdT5XJcxrFZNnoFlU7XRUt2GcZpuvJH8wgjH6n1lcH6mF4vgBF4KDoDEcSJCXZcNb4WBhYKR0NNIEYBRG7QRt0PTxaoiXDNnC4k+Uvu1CM-dxyHZWULbSM1fYtf1cFtdNWoLWO87MFXtwlGe9oHuqTnaADT+qjDcKKIzBAND4e6hGAxqPjTPR0BIAAXign7r8LSIiuCPgot7Q9J5+0XtRQOwdQ7TwxrUGuoDRIIDABiH+pw-5chJknMmqsYAPU1jAbMOsTzLGlnjY09FY5oC5nfXmHZ9ZQAFoNG25RtpIJSP-NGPsgHQJAWAMB8CJ6winpAmonC56wKDvAxBv8UStQTsrAy6scF4NzLrQsNDahXlUDeO8iUzYvhoVbDaQtBFIjID4dC+1+zDxgdwuB4kgFh2EUvGxGITHoWkRjWRm4jJ7i1s9ZRYwGqMjwrXGAQd0KPhAggSwXMnJF38CiPC-hsDig1AFNEMAADiSoND1wqlFdJbdO72CVH3UhB9B7D1HqUhcpV+HXUYTPNE9U0TLxyPHQcFR161GQDkPe1kD5H3QqfUU4o8KZJzNzIGeg8IokJCvOhx8v4umYSggBljmk2L4QJexqScg8IQcsqRaDyjJ3TBrYYuCzx6NPFMmAMzmoULmgqR+epn5vw-gYk+RilmSLEGwwBtToHrLEbY2p2ygViQkcgw5St0EqzTE4e6CjLlvlPA-Kyrz34PJ5mufmn8vlDgOb8niE41m7I2RA+pUCdnWOBZClhqCYXHIwduXcSK3oFjGOozRJsdEWygPowWnzKUi1qC46C5i+IcOpXszZcIwVktpWK-kbiageNVl485IxlhFJzAWBo4wdUoExtIAs91wjBECCCTY8RdQoDdJyPY3xkigDVPa+SixviGvmkqD1FwYCdDzo5QuLkOAAHY3BOBQBrfwEZghwG8gANngBRQwYzDBFAwQ3BO9RoodEKcU55fSFxZi9UqNpqr-kCRHqWuYNStnCqRHBOQKAMTHDrC0sA5bRAdI+eReCLaa2oM-uULCABZcMGSZqUJuXcuZeKG3fKhUS9p7CAVwBTTKil0l139r2XSlBKqBFMrhVg1lmrkWW2uSGW5KBZk5CxUDNFT9bxvPmYM-FoofmrI4Tu5tm67GCOeL+9Ee7CWHsTsegy2Dz3stRYWjFuxA0A0eTimhb6hofsJd+tdG7yUAeFUB3DtKwNHJOQitlVzlhcuNveJ8ui3xIfQww7aSr1ASpyj+oj4D8Pbq4+I1jqhwNqtqBqtO2qlTGtNeawIMBGMxJcpYFAoEICbHLkgBIYBFPKdUwAKQgOKSdcwYjOpAGqDNKss1QMaE0ZksUeiGpKfvYtoxsAIGAIpqAcAIA1SgGsQ1xry1T0rXCatEnpB1rlQuocAArfTaAMSxYDre5qKru1O17Yl+L-npADKGiO4Uo1RlTseTO5Lc6PmLIJV+v5w89NJeBbKo90k6toFAz8oTsKoNnrThe-lV7pllfvYxyhzJ4MvvfkxwxUXP1Luw1Wlr-7QWAbqAtpxJHGVkcRTByjEwxuv0xcNx5aaYAigiZNoVTDqvEtXfNuLi2tnLdqKt4j7XSPMtPZTc9viCFjBNLOGAtZc6NmQ9ihOMBcUVcw1dldwXnjPe40tgjK27trdext978ivv4I5dRrRBReV6MY-OljpjxVtQsRw+HvCt3ZqpwggTHXIOeNTrguTwbi5QHc2pjTXgueIGDLAYA2BXOEDyPj8z5hLNCMaM3Vu7dO7GHKRwkYEWgvbU3kyFAHaMSpZUD2wVailOa-O0MgA6sQQwivKGoa7MTpGWGascI1yBvDiPpLO616jpdjPbrq2pkok8vW9jSCNnj02dG+UCvoVNknZjHcAo9-duVj3E9OIZ29uFomVfA8wEAA)

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
