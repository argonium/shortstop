package io.miti.shortstop.model;

import io.miti.shortstop.util.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

public final class Config
{
  private static final int DEFAULT_PORT = 8000;
  
  private static final String PROPS_FILENAME = "shortstop.props";
  
  private int port = DEFAULT_PORT;
  private boolean canDownloadFiles = false;
  private boolean canDownloadAllExtensions = false;
  private Set<String> allowedExtensions = null;
  private String fileDirectory = null;
  private boolean computeMD5Response = false;
  
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
    
    // Check if the user can download files
    canDownloadFiles = Utility.parseStringAsBoolean(
        props.getProperty("file.download.allowed"), false);
    
    // Check if the user can download all extensions, or just some
    final String exts = props.getProperty("file.extensions.filter");
    if ((exts == null) || exts.trim().isEmpty()) {
      canDownloadFiles = false;
    } else {
      if (exts.equals("*")) {
        canDownloadAllExtensions = true;
      } else {
        final StringTokenizer st = new StringTokenizer(exts, ",");
        if (st.hasMoreTokens()) {
          allowedExtensions = new HashSet<String>(10);
          while (st.hasMoreTokens()) {
            final String str = st.nextToken().trim();
            if (!str.isEmpty()) {
              allowedExtensions.add(str.toLowerCase(Locale.US));
            }
          }
        }
      }
    }
    
    // Get the directory for downloading files
    fileDirectory = props.getProperty("file.directory");
    if ((fileDirectory == null) || fileDirectory.trim().isEmpty()) {
      // No directory specified, so users cannot download files
      canDownloadFiles = false;
      fileDirectory = null;
    }
    
    // Check if we should compute the MD5 for responses
    computeMD5Response = Utility.parseStringAsBoolean(
        props.getProperty("md5.response"), false);
  }
  
  
  /**
   * Return whether users can download any files at all.
   * 
   * @return whether the server supports file retrieval
   */
  public boolean canDownloadFiles() {
    // If these conditions are met, then it's possible that the user may be
    // able to download a given file
    return (canDownloadFiles && (fileDirectory != null) &&
            (canDownloadAllExtensions || ((allowedExtensions != null) && !allowedExtensions.isEmpty())));
  }
  
  
  /**
   * Return whether a user can download a particular extension.
   * 
   * @param ext the file extension
   * @return whether that extension can be downloaded
   */
  public boolean canDownloadExtension(final String ext) {
    // Null input returns false
    if (ext == null) {
      return false;
    }
    
    return (canDownloadFiles() && (canDownloadAllExtensions ||
            ((allowedExtensions != null) && allowedExtensions.contains(ext.toLowerCase(Locale.US)))));
  }
  
  
  /**
   * Return the file directory.
   * 
   * @return the file directory, or null if not in the properties file
   */
  public String getFileDirectory() {
    return fileDirectory;
  }
  
  
  /**
   * Whether to compute the MD5 hash for responses and include
   * it in the response header.
   * 
   * @return if we should compute MD5 hash values for response body
   */
  public boolean shouldComputeMD5Response() {
    return computeMD5Response;
  }
}
