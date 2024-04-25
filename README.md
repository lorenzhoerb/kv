# Simple Key-Value Protocol

This project implements a straightforward key-value memory store protocol similar to REDIS, utilizing Java sockets for communication.

## Overview

The Simple Key-Value Protocol allows clients to interact with a server to store, retrieve, and delete key-value pairs. It offers a simple yet efficient solution for managing data in memory.

## Features

- **Basic Commands**: Supports essential commands like `PUT`, `GET`, `DEL`, `ALL`, and `CLEAR`.
- **Error Handling**: Provides clear error messages for invalid commands or operations.
- **Thread Safety**: Ensures thread safety for concurrent access to the key-value store.

## Commands

- `ALL`: Returns all key-value entries in the store.
- `PUT [key] [value]`: Sets a key-value pair in the store.
- `CLEAR`: Clears all entries in the store.
- `GET [key]`: Retrieves the value associated with a given key.
- `DEL [key]`: Deletes the entry associated with the given key.

## Protocol

Upon connection, the server responds with `OK`. After each command, the protocol responds with either `OK` if the command was successful or `ERROR: [error message]` if there was an issue, each on a new line.


### ALL
Returns all key-value pairs in the following format: `[key]: [value]`

```
> ALL
name: KV
version: 1.0
OK
```

### PUT
Sets a key-value pair.


```
> PUT name KV
OK
```

## CLEAR
Clears the entire store.


```
> CLEAR
OK
```

### GET
Retrieves the value of a specified key. Returns `ERROR: ...` if the `key` is not existing.


Example:

```
> GET name
KV
OK
```

Lets assume that the key `animal` is not existing.
```
> GET 
KV
ERROR: Couldn't get value of key 'animal' 
```


### GET
Deletes a key-value pair associated with a specified key. 


Example:

```
> DEL name
OK
```

## API

The `IKeyValueSession` interface provides methods for interacting with the key-value store:

- `put(String key, String value)`: Sets a key-value pair.
- `getAll()`: Retrieves all key-value pairs.
- `get(String key)`: Retrieves the value associated with a key.
- `clear()`: Clears all entries.
- `del(String key)`: Deletes the entry associated with a key.

## Usage

To establish a session with the server, use the `Connection` class:

```java
Connection.getSession(String host, int port);
```

Example

```java
   IKeyValueSession session = Connection.getSession("localhost", 6543);

    session.put("name", "KV");
    session.put("version", "1.0.0");
    
    session.del("version");
    
    HashMap<String, String> keyValues = session.getAll();
    String session.get("name");
    session.clear();
```