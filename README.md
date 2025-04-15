## Mini-Dictionary

Mini Dictionary is a multi-threaded, client-server dictionary application built in Java, allowing multiple clients to connect and interact with a shared dictionary concurrently. I built this project using Java as part of my course (COMP90015) at the Univeristy of Melbourne, featuring robust concurrency handling, socket communication, and a user-friendly GUI using Java Swing.

## Demo Footage
https://github.com/user-attachments/assets/357149dd-7386-49fb-90f8-4c523ba08723

## Key Features

- **Multi-client support**: Multiple clients can connect to the dictionary server simultaneously and perform operations.
- **Thread Pool Architecture** : The server handles concurrent client requests using a fixed-size thread pool and a priority queue.
- Reliable communication: Client-server interaction is handled through TCP sockets using JSON as the message format.
- Dictionary operations including:
  - **Query**: Get meanings of a given keyword
  - **Add**: Insert a new keyword with meanings
  - **Update**: Modify the meanings of an existing keyword
  - **Remove**: Delete a keyword and its meanings
- **Client GUI**:
  - Input text field and buttons to perform dictionary operations
  - Displays responses, errors, or connection issues
  - Add/Update pop-up dialog for detailed input
- **Server GUI**:
  - Displays active client count, thread pool size, queue status
  - Shows most recent request and allows server shutdown

## System Architecture

- **Client-Server model**: Built on TCP with a clear separation between client and server responsibilities.
- **Message format**: All requests and responses are parsed and transmitted in **JSON**.
- **Concurrency**:
  - Requests are added to a **priority queue**
  - Processed using a **fixed-size thread pool** of 5 threads
- **Failure handling**: Disconnection alerts and error messages are shown in client GUIs for graceful failure handling.

## Project Directory

- `server/` – Server logic, threading, queue management, socket handling  
- `client/` – Client logic, middleware for communication, response display  
- `gui/` – Java Swing-based GUI for both server and client  
- `dictionary/` – Dictionary operations, with synchronized methods for thread safety  
- `response/` – Utilities for response formatting and error handling  
- `resources/Dictionary.json` – Sample dictionary used to test during development

## Getting Started

> The following steps are to be used to run the JAR executables. To make any edits to the code and run directly from the source, proceed to the `/src/main/java` directories.

> Dictionary.json is used as a sample dictionary glossary, feel free to add yours!

- Clone the project
```bash
  git clone https://github.com/vik782/Mini-Dictionary.git
```

- Run command to start a dictionary server (e.g., use any any free port for server_port)
```bash
  java -jar DictionaryServer.jar <server_port> Dictionary.json
```

- Run command to join as a User in the dictionary server (e.g., use "localhost" as server_address and the same server_port as the server)
```bash
  java -jar DictionaryClient.jar <server_address> <server_port>
```

- Done, happy querying!
