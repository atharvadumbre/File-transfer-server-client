# Multi-Threaded Java Socket Programming Project

This project demonstrates a multi-threaded server-client architecture using Java socket programming. The server can handle multiple clients simultaneously, providing options to list files, download files, upload files, and exit.

## Features

- **Multi-threaded Server:** Capable of handling multiple clients concurrently using threads.
- **Client Options:**
  1. List files present on the server.
  2. Download a specific file from the server.
  3. Upload a file to the server.
  4. Exit the connection.

## Prerequisites

- Java Development Kit (JDK) 8 or higher

## Setup and Usage

1. **Clone the Repository:**
    ```bash
    git clone [https://github.com/yourusername/java-socket-programming.git](https://github.com/atharvadumbre/File-transfer-server-client)
    ```

2. **Compile the Code:**
    ```bash
    javac MathServer.java
    javac MathClient.java
    ```

3. **Run the Server:**
    ```bash
    java MathServer <portnumber>
    ```

4. **Run the Client:**
    ```bash
    java MathClient <host name> <port number>
    ```

## Project Structure

- **MathServer.java:** The server-side code that handles multiple clients using threads.
- **MathClient.java:** The client-side code that provides options to interact with the server.

## Usage Instructions

1. **Start the Server:**
   Run the `MathServer.java` file to start the server. The server will wait for client connections.

2. **Connect a Client:**
   Run the `MathClient.java` file to connect to the server. Once connected, the client will be presented with four options:
   - **List Files:** List all files available on the server.
   - **Download File:** Download a specific file from the server by entering the file name.
   - **Upload File:** Upload a file to the server by specifying the file path.
   - **Exit:** Disconnect from the server.

3. **Interacting with the Server:**
   Follow the on-screen prompts to choose the desired option and interact with the server.

## Example Usage

**Commands Available**
   ```
   Please select an option:
   1. list
   2. download
   3. upload
   4. exit
   ```



## Contact

For any questions or issues, please contact [atharva.dumbre1@gmail.com](mailto:atharva.dumbre1@gmail.com).

---

Thank you for checking out this project! We hope it helps you understand the basics of Java socket programming and multi-threaded server-client architecture. Happy coding!
