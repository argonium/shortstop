package io.miti.shortstop.server;

import io.miti.shortstop.util.ContentTypeCache;
import io.miti.shortstop.util.HeaderField;
import io.miti.shortstop.util.Utility;

import io.miti.shortstop.model.*;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

public final class Shortstop {
  
  /** Default for line-endings. */
  public static final String CRLF = "\r\n";
  
  /** The configuration. */
  private Config cfg = null;
  
  
  /**
   * Default constructor.
   */
  @SuppressWarnings("unused")
  private Shortstop() {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param cfg the configuration
   */
  public Shortstop(final Config cfg) {
    this.cfg = cfg;
  }
  
  
  /**
   * Generate a readable version of the URL.
   * 
   * @param isSecure whether the server is using HTTPS
   * @param port the port number
   * @return the URL as a string
   */
  private static String getServerURL(final boolean isSecure, final int port) {
    
    // Build the URL
    StringBuilder sb = new StringBuilder(100);
    sb.append(isSecure ? "https" : "http").append("://");
    
    // Add the IP address
    try {
      final String ipAddr = InetAddress.getLocalHost().getHostAddress();
      sb.append(ipAddr);
    } catch (UnknownHostException e) {
      sb.append("localhost");
    }
    
    // Add the port if it's not the standard one for the protocol (http / https)
    if (isSecure) {
      if (port != 443) {
        sb.append(":").append(Integer.toString(port));
      }
    } else if (port != 80) {
      sb.append(":").append(Integer.toString(port));
    }
    
    // Close out the URL
    sb.append("/");
    return sb.toString();
  }
  
  
  /**
   * Start a server.
   */
  private void startServer() {
    
    // Try to start the server
    ServerSocket s = null;
    try {
      s = new ServerSocket(cfg.getPort());
      final String url = getServerURL(false, cfg.getPort());
      System.out.println("Server running: " + url);
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
  
  /**
   * We received a request from a client, so handle it now.
   * 
   * @param socket the connection socket
   */
  private void handleConn(final Socket socket) {
    
    // Define the input stream reader and output stream writer
    BufferedReader is = null;
    BufferedOutputStream os = null;
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
      os = new BufferedOutputStream(socket.getOutputStream());
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
        try {
          os.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
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
  private Response handleRequest(final Request msg) throws IOException {
    
    // If the request has an MD5 value for the content, evaluate it now
    final String reqMD5 = msg.headerGetKey(HeaderField.REQ_CONTENT_MD5);
    if ((reqMD5 != null) && (msg.hasBody())) {
      // Compute the MD5 hash for the content and see if they match
      StringBuilder sb = new StringBuilder().append(msg.getMessageBody());
      final String md5 = Utility.getMD5(sb.toString().getBytes(StandardCharsets.UTF_8));
      if (!md5.equals(reqMD5)) {
        Response resp = new Response(400);
        return resp;
      }
    }
    
    // Handle requests by looking up handlers for the verb and URL in msg
    Response response = Registrar.process(msg, cfg);
    
    // See if we got a null response
    if (response == null) {
      
      // We get a null response, so see if we can download a matching filename
      if (cfg.canDownloadFiles()) {

        // We support downloading files. Get the extension and check that.
        final String url = msg.getURL();
        final int index = url.lastIndexOf('.');
        boolean canContinue = false;
        String ext = null;
        if (index < 0) {
          // No extension.  Stop processing.
          canContinue = false;
        } else {
          // See if the extension is in the list of allowed extensions
          ext = url.substring(index + 1).toLowerCase(Locale.US);
          canContinue = cfg.canDownloadExtension(ext);
        }

        // Instantiate the response now
        response = new Response();
        
        // Check for .. in the file name, and disallow those, since
        // we want to ensure the user can only retrieve files from the
        // file directory
        if (canContinue) {
          canContinue = (!url.contains(".."));
        }

        // Passed the tests so far
        if (canContinue) {
          // Verify the file exists (as a file)
          final File file = new File(cfg.getFileDirectory(), url);
          if (!fileCanBeDownloaded(file)) {
            canContinue = false;
          } else {
            final Path path = file.toPath();
            byte[] data = Files.readAllBytes(path);
            response.setCode(200);
            response.setBodyAsBytes(data);
            response.addToHeader(HeaderField.RES_CONTENT_TYPE, ContentTypeCache.getCache().getContentTypeMIMEType(ext));
            canContinue = true;
          }
        }

        // If an error occurred, mark the response as 404
        if (!canContinue) {
          response.setAs404();
        }
      } else {
        response = new Response().setAs404();
      }
    }
    
    // Return the generated response
    return response;
  }
  
  
  /**
   * Return whether the specified file can be downloaded.
   * 
   * @param file the requested file
   * @return whether the user can download it
   */
  private boolean fileCanBeDownloaded(final File file) {
    if (!file.exists() || !file.isFile()) {
      return false;
    }
    
    return true;
  }


  /**
   * Return whether this class is running inside a JAR or not.
   * 
   * @return whether the application is running in a jar (default is true)
   */
  public static boolean isRunningInJar() {
    // Get a URL to this class so we can check how it's loaded (jar or file)
    final java.net.URL url = Shortstop.class.getResource("Shortstop.class");
    
    // Check the start of the string.  If it's a file, it'll start with "file:".
    final boolean inJar = ((url == null) ? true : url.toString().startsWith("jar:"));
    return inJar;
  }
  
  /**
   * Write the response to the client.
   * 
   * @param response the response
   * @param os the output writer
   * @throws IOException thrown when writing
   */
  private void writeResponse(final Response response, final OutputStream os)
      throws IOException {
    
    // If we need to include the MD5 value in the response do so now
    if (cfg.shouldComputeMD5Response()) {
      response.addMD5();
    }
    
    // Write a response
    StringBuilder sb = new StringBuilder(100);
    sb.append("HTTP/1.1 ").append(response.getCode()).append(" ").append(response.getMessage()).append(CRLF);
    
    // Write the header
    Set<Entry<String, String>> entries = response.getHeaderIterator();
    if (entries != null) {
      for (Entry<String, String> entry : entries) {
        final String key = entry.getKey();
        final String value = entry.getValue();
        sb.append(key).append(": ").append(value).append(CRLF);
      }
    }
    
    // Add an extra linefeed (to mark the end of the header)
    sb.append(CRLF);
    
    // Write the header
    os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
    
    // Clear the buffer
    sb.setLength(0);
    
    // Print any response here, plus CRLF
    if (response.hasBody()) {
      os.write(response.getBody());
      os.write(CRLF.getBytes(StandardCharsets.UTF_8));
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
    if (msg.headerContainsKey(HeaderField.RES_CONTENT_LENGTH)) {
      final int conLen = Integer.parseInt(msg.headerGetKey(HeaderField.RES_CONTENT_LENGTH));
      if (conLen > 0) {
        // Allocate our buffer and read the message body
        final char[] buffer = new char[conLen];
        final int numRead = is.read(buffer, 0, conLen);
        if (numRead != conLen) {
          System.out.println(String.format("Error: Expected %d bytes but found %d in the body", conLen, numRead));
        }
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
    // Get the properties
    Config cfg = new Config();
    
    // Register some handlers
    Registrar.registerHandlers();
    
    // Start the server
    new Shortstop(cfg).startServer();
  }
}
