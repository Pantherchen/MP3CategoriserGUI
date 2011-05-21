package de.kanwas.audio.mp3;

/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3Categoriser {
  /** version number */
  public static final String VER = "$Revision$";

  private JFrame frame;

  private JMenuBar menuBar;

  private JMenu fileMenu;

  /**
   * @param args
   */
  public static void main(final String[] args) {
    EventQueue.invokeLater(new Runnable(){
      @Override
      public void run() {
        try {
          MP3Categoriser window = new MP3Categoriser();
          window.getFrame().setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

  }

  public MP3Categoriser() {
    initComponents();
  }

  protected void initComponents() {
    this.frame = new JFrame();
    this.frame.setJMenuBar(getMenuBar());
    this.frame.add(new MP3CatMain());

  }

  public JFrame getFrame() {
    return this.frame;
  }

  private JMenuBar getMenuBar() {
    if (this.menuBar == null) {
      this.menuBar = new JMenuBar();
      this.menuBar.add(getFileMenu());
      this.menuBar.setVisible(true);
    }
    return this.menuBar;
  }

  private JMenu getFileMenu() {
    if (this.fileMenu == null) {
      this.fileMenu = new JMenu("Datei");
      this.fileMenu.add(new JMenuItem());
    }
    return this.fileMenu;
  }
}
