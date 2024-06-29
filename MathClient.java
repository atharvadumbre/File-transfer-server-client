import java.io.*;
import java.net.*;

public class MathClient {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println(
                "Usage: java MathClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                
                if (userInput.equals("exit")) {
                    break;
                } 
                else if (userInput.startsWith("upload")) {
                    
                    String[] command = userInput.split(" ", 2);
                    if (command.length == 2) {
                        String filePath = command[1];
                        out.println(userInput);
                        sendFile(echoSocket, filePath);
                    } else {
                        System.out.println("Invalid upload command. Usage: upload <file path>");
                    }
                }
                
                else {
                    out.println(userInput);
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        if (serverResponse.equals("file_transfer_start")) {
                            getFile(in);
                            break;
                        }
                        System.out.println(serverResponse);
                        if (!in.ready()) {
                            break;
                        }   
                }
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
}



private static void getFile(BufferedReader in) throws IOException {
    String fileName = in.readLine();
    long fileSize = Long.parseLong(in.readLine());

    FileOutputStream fileOut = new FileOutputStream("downloaded_" + fileName);
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
    System.out.println("File downloaded successfully: " + fileName);
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
    System.out.println(fileName + " sent to server.");
}

}