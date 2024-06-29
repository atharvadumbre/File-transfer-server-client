import java.io.*;
import java.net.*;

public class MathServer {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java MathServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Create a new thread to handle each client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] result = inputLine.split(" ");
                    if (result[0].equals("list")) {
                        listFilesInCurrentDirectory(out);
                    } else if (result[0].equals("download")) {
                        sendFile(clientSocket, result[1]);
                    } else if (result[0].equals("upload")) {
                        getFile(in);
                    } else {
                        out.println("Unknown method");
                    }
                }
            } catch (IOException e) {
                System.out.println("Exception caught when handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Exception caught when closing client socket: " + e.getMessage());
                }
            }
        }
    }

public static void listFilesInCurrentDirectory(PrintWriter out) {
    String currentDirectoryPath = System.getProperty("user.dir");
    File currentDirectory = new File(currentDirectoryPath);
    File[] files = currentDirectory.listFiles();

    if (files != null) {
        for (File file : files) {
            out.println(file.getName());
        }
    } else {
        System.out.println("No files found in the current directory.");
    }
}



public static void sendFile(Socket clientSocket, String fileName) throws IOException {
    File fileToSend = new File(fileName);
    if (!fileToSend.exists()) {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("File not found: " + fileName);
        return;
    }

    OutputStream socketOut = clientSocket.getOutputStream();

    PrintWriter out = new PrintWriter(socketOut, true);
    FileInputStream fileIn = new FileInputStream(fileToSend);
    long fileSize = fileToSend.length();

    out.println("file_transfer_start");
    out.println(fileToSend.getName());
    out.println(fileSize);

    byte[] buffer = new byte[2048];
    int totalRead = 0;
    while (totalRead < fileSize) {
        int bytesRead = fileIn.read(buffer);
        if (bytesRead == -1) {
            break;
        }
        socketOut.write(buffer, 0, bytesRead);
        totalRead += bytesRead;
    }

    fileIn.close();
    System.out.println(fileName + " sent to client.");
}



private static void getFile(BufferedReader in) throws IOException {
    String fileName = in.readLine();
    long fileSize = Long.parseLong(in.readLine());

    FileOutputStream fileOut = new FileOutputStream("uploaded_" + fileName);
    char[] buffer = new char[1024];
    int totalRead = 0;
    while (totalRead < fileSize) {
        int charsRead = in.read(buffer);
        if (charsRead == -1) {
            break;
        }
        byte[] byteBuffer = new String(buffer, 0, charsRead).getBytes("UTF-8");

        fileOut.write(byteBuffer);
        totalRead += byteBuffer.length;
    }
    fileOut.close();
    System.out.println("File uploaded successfully: " + fileName);
}
}
