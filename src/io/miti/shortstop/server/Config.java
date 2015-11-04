package io.miti.shortstop.server;

import io.miti.shortstop.util.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public final class Config
{
  private static final int DEFAULT_PORT = 8000;
  
  private static final String PROPS_FILENAME = "shortstop.props";
  
  private int port = DEFAULT_PORT;
  private boolean canDownloadFiles = false;
  private boolean canDownloadAllExtensions = false;
  private Set<String> allowedExtensions = null;
  private String fileDirectory = null;
  
  public Config() {
    readProperties();
  }
  
  public int getPort() {
    return port;
  }
  
  private void readProperties() {
    
    // Create a reference to the properties file and verify it exists
    File file = new File(PROPS_FILENAME);
    if (!file.exists() || file.isDirectory()) {
      return;
    }
    
    FileInputStream fileInput = null;
    try {
      fileInput = new FileInputStream(file);
      Properties props = new Properties();
      props.load(fileInput);
      loadProperties(props);
      fileInput.close();
      fileInput = null;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fileInput != null) {
        try {
          fileInput.close();
          fileInput = null;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  /**
   * Load data into the member variables.
   * 
   * @param props the contents of the properties file
   */
  private void loadProperties(Properties props) {
    // Get the port the server should run on
    final String sPort = props.getProperty("http.port");
    port = Utility.parseStringAsInt(sPort, DEFAULT_PORT);
    port = Math.max(20, Math.min(20_000, port));
  }
}
