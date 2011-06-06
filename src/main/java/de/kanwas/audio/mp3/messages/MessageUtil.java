/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.messages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MessageUtil {
  /** version number */
  public static final String VER = "$Revision$";

  private static MessageUtil instance;

  private Properties messages;

  public static MessageUtil getInstance() {
    if (instance == null) {
      instance = new MessageUtil();
      instance.readMessages();
    }
    return instance;
  }

  public void readMessages() {
    messages = new Properties();
    try {
      messages.load(new FileInputStream("messages.txt"));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String getMessage(String name) {
    if (messages == null) {
      return "";
    }
    return messages.getProperty(name);
  }

}
