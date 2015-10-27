package io.miti.shortstop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public final class Shortstop {
  
  /** Default for line-endings. */
  public static final String CRLF = "\r\n";
  
  /**
   * Default constructor.
   */
  public Shortstop() {
    super();
  }
  
  /**
   * Start a server on the specified port.
   * 
   * @param port the port number
   */
  private void startServer(final int port) {
    
    // Try to start the server
    ServerSocket s = null;
    try {
      s = new ServerSocket(port);
      System.out.println("Started server on port " + port);
    } catch (IOException ioe) {
      System.err.println("Error running server: " + ioe.getMessage());
      s = null;
    }
    
    // If an error happened, return
    if (s == null) {
      return;
    }
    
    // Listen for connections from clients
    while (true) {
      try {
        // Block for a connection
        Socket socket = s.accept();
        
        // We got a connection, so handle it
        handleConn(socket);
      } catch(IOException e) {
        System.err.println(e);
        return;
      }
    }
  }
  
  private void handleConn(final Socket socket) {
    
    // Define the input stream reader and output stream writer
    BufferedReader is = null;
    PrintWriter os = null;
    try {
//      String host = socket.getInetAddress().toString();
//      System.out.println("Accepted connection from " + host);
      
      // Read from the socket
      is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      
      // Get the HTTP request
      final String request = is.readLine();
      // System.out.println("Request: " + request);
      final Request msg = new Request(request);
      System.out.println(msg.toString());
      if (msg.isValidProtocol()) {
      
        // Read the header
        final Map<String, String> header = readHeader(is);
        
        // Populate the query string parameters based on the URL
        msg.parseURLandParameters();
        
        // Check if there is any content (message body) using the Content-Length
        // field; if it's not in the header, assume there is no body
        if (header.containsKey("Content-Length")) {
          final int conLen = Integer.parseInt(header.get("Content-Length"));
          if (conLen > 0) {
            // Allocate our buffer and read the message body
            char[] buffer = new char[conLen];
            is.read(buffer, 0, conLen);
            msg.setMessageBody(buffer);
          }
        }
        
        // Handle the request here
      }
      
      // Clear out the request
      msg.cleanup();
      
      // Write a response
      os = new PrintWriter(socket.getOutputStream(), true);
      os.print("HTTP/1.0 200 OK" + CRLF);
      os.print("Content-type: text/html" + CRLF);
      os.print("Server-name: Shortstop Web Server" + CRLF);
      os.print("Content-length: 0" + CRLF);
      os.print(CRLF);
      os.flush();
      
      // Close the socket
      socket.close();
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } finally {
      // Close the input stream
      if (is != null) {
        try {
          is.close();
          is = null;
        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        }
      }
      
      // Close the output stream
      if (os != null) {
        os.close();
        os = null;
      }
    }
  }
  
  
  /**
   * Read the HTTP header into a map.
   * 
   * @param is the input stream reader
   * @return a map of key/value pairs
   * @throws IOException thrown when reading from the input stream
   */
  private Map<String, String> readHeader(final BufferedReader is) throws IOException {
    
    // Build the header map
    final Map<String, String> header = new HashMap<String, String>(20);
    
    // Read the header until we hit null or an empty line
    String line = is.readLine();
    while ((line != null) && (!line.isEmpty())) {
      
      // Parse the key and value pair
      final int index = line.indexOf(':');
      if (index > 0) {
        header.put(line.substring(0, index), line.substring(index + 1).trim());
      }
      
      // Read the next line
      line = is.readLine();
    }
    
    // Print the header
//    System.out.println("Completed reading the header");
//    for (java.util.Map.Entry<String, String> entry : header.entrySet()) {
//      System.out.println("Key: " + entry.getKey() + " / Value: " + entry.getValue());
//    }
    
    // Return the key/value pairs map
    return header;
  }
  
  
  /**
   * Entry point for the application.
   * 
   * @param args arguments to the program
   */
  public static void main(final String[] args) {
    // Define a default port number
    final int port = 8000;
    
    // Start the server
    new Shortstop().startServer(port);
  }
}
