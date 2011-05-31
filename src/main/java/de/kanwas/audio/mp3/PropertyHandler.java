/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class PropertyHandler {

  /** version number */
  public static final String VER = "$Revision$";

  /**  */
  private static final String PATH_CATEGORY = "path.category";

  private static final String PATH_MUSIC = "path.music";

  private static final String PATH_DATABASE = "path.export";

  private Properties properties;

  private static PropertyHandler propertyHandler;

  public static PropertyHandler getInstance() {
    if (propertyHandler == null) {
      propertyHandler = new PropertyHandler();
      propertyHandler.loadProperties();
    }
    return propertyHandler;
  }

  private PropertyHandler() {}

  private void loadProperties() {
    properties = new Properties();
    try {
      InputStream is = ClassLoader.getSystemResourceAsStream("config.xml");
      if (is == null) {
        throw new FileNotFoundException("Cant find config.xml");
      }
      properties.loadFromXML(is);
    } catch (InvalidPropertiesFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public Properties getProperties() {
    return this.properties;
  }

  public String getCategoryPath() {
    return this.properties.getProperty(PATH_CATEGORY);
  }

  public String getMusicPath() {
    return this.properties.getProperty(PATH_MUSIC);
  }

  public String getDatabasePath() {
    return this.properties.getProperty(PATH_DATABASE);
  }
}
