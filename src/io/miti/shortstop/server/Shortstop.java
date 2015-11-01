package io.miti.shortstop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Set;

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
      // String host = socket.getInetAddress().toString();
      // System.out.println("Accepted connection from " + host);
      
      // Read from the socket
      is = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      
      // Get the HTTP request
      final String request = is.readLine();
      // System.out.println("Request: " + request);
      
      // If there is input in the buffer, read and process it now
      Response response = null;
      if (request != null) {
        // Create our Request object
        final Request msg = new Request(request);
        System.out.println(msg.toString());
        if (msg.isValidProtocol()) {
          
          // Read the remainder of the request (header and any message body)
          readRemainderOfRequest(is, msg);
          
          // Handle the request
          response = handleRequest(msg);
        }
        
        // Clear out the request
        msg.cleanup();
      }
      
      // If the response object is null, set it now
      if (response == null) {
        response = new Response();
      }
      
      // Write the response
      os = new PrintWriter(new OutputStreamWriter(
          socket.getOutputStream(), StandardCharsets.UTF_8), true);
      writeResponse(response, os);
      
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
   * Handle requests.
   * 
   * @param msg the request
   * @return the response
   */
  private Response handleRequest(final Request msg) {
    // TODO Handle requests by looking up handlers for the verb and URL in msg
    return null;
  }
  
  /**
   * Write the response to the client.
   * 
   * @param response the response
   * @param os the output writer
   * @throws IOException thrown when writing
   */
  private void writeResponse(final Response response, final PrintWriter os)
      throws IOException {
    
    // Write a response
    StringBuilder sb = new StringBuilder(100);
    sb.append("HTTP/1.1 ").append(response.getCode()).append(" ").append(response.getMessage()).append(CRLF);
    os.print(sb.toString());
    sb.setLength(0);
    
    // Write the header
    Set<Entry<String, String>> entries = response.getHeaderIterator();
    if (entries != null) {
      for (Entry<String, String> entry : entries) {
        final String key = entry.getKey();
        final String value = entry.getValue();
        os.print(key + ": " + value + CRLF);
      }
    }
    
    // Add an extra linefeed (to mark the end of the header)
    os.print(CRLF);
    
    // Print any response here, plus CRLF
    if (response.hasBody()) {
      os.print(response.getBody());
      os.print(CRLF);
    }
    
    os.flush();
  }
  
  
  /**
   * Read the remainder of the request.
   * 
   * @param is the input stream
   * @param msg the request object
   * @throws IOException thrown exception from reading the input stream
   */
  private void readRemainderOfRequest(final BufferedReader is, final Request msg)
    throws IOException {
    
    // Read the header and store in the Request object
    readHeader(is, msg);
    
    // Populate the query string parameters based on the URL
    msg.parseURLandParameters();
    
    // Check if there is any content (message body) using the Content-Length
    // field; if it's not in the header, assume there is no body
    if (msg.headerContainsKey("Content-Length")) {
      final int conLen = Integer.parseInt(msg.headerGetKey("Content-Length"));
      if (conLen > 0) {
        // Allocate our buffer and read the message body
        char[] buffer = new char[conLen];
        final int numRead = is.read(buffer, 0, conLen);
        msg.setMessageBody(buffer);
      }
    }
  }
  
  
  /**
   * Read the HTTP header into a map.
   * 
   * @param is the input stream reader
   * @param msg the request object
   * @return a map of key/value pairs
   * @throws IOException thrown when reading from the input stream
   */
  private void readHeader(final BufferedReader is, final Request msg) throws IOException {
    
    // Read the header until we hit null or an empty line
    String line = is.readLine();
    while ((line != null) && (!line.isEmpty())) {
      
      // Parse the key and value pair
      final int index = line.indexOf(':');
      if (index > 0) {
        msg.addHeaderRow(line.substring(0, index), line.substring(index + 1).trim());
      }
      
      // Read the next line
      line = is.readLine();
    }
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
