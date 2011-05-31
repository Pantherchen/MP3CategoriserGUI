package de.kanwas.audio.mp3;

/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import de.kanwas.audio.mp3.dialog.ExportPlaylistDialog;

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

  private JMenuItem openMenuItem;

  private MP3CatMain mp3CatMain;

  private JMenuItem exportPlaylistMenuItem;

  private JMenuItem saveMenuItem;

  private JMenuItem exitMenuItem;

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
          window.getFrame().setSize(800, 800);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

  }

  public MP3Categoriser() {}

  public JFrame getFrame() {
    if (this.frame == null) {
      this.frame = new JFrame();
      this.frame.setJMenuBar(getMenuBar());
      mp3CatMain = new MP3CatMain();
      this.frame.add(mp3CatMain);
      this.frame.addWindowListener(new WindowAdapter(){

        @Override
        public void windowClosing(WindowEvent e) {
          super.windowClosing(e);
          MP3Categoriser.this.frame.setVisible(false);
          System.exit(0);
        }

      });
    }
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
      this.fileMenu.add(getOpenMenuItem());
      this.fileMenu.add(new JSeparator());
      this.fileMenu.add(getExportPlaylistMenuItem());
      this.fileMenu.add(new JSeparator());
      this.fileMenu.add(getSaveMenuItem());
      this.fileMenu.add(new JSeparator());
      this.fileMenu.add(getExitMenuItem());
    }
    return this.fileMenu;
  }

  /**
   * @return
   */
  private JMenuItem getExportPlaylistMenuItem() {
    if (exportPlaylistMenuItem == null) {
      this.exportPlaylistMenuItem = new JMenuItem("Export Playlist");
      this.exportPlaylistMenuItem.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          ExportPlaylistDialog dialog = new ExportPlaylistDialog(MP3Categoriser.this.getFrame(), mp3CatMain
            .getMP3DataBroker());
          dialog.setVisible(true);

        }
      });
    }
    return this.exportPlaylistMenuItem;
  }

  private JMenuItem getOpenMenuItem() {
    if (this.openMenuItem == null) {
      this.openMenuItem = new JMenuItem("MP3 Ordner öffnen");
      this.openMenuItem.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          FileDialog fd = new FileDialog(MP3Categoriser.this.getFrame());
          String mp3Dir = fd.getDirectory();
          MP3Categoriser.this.mp3CatMain.setMP3Path(mp3Dir);
        }
      });
    }
    return this.openMenuItem;
  }

  private JMenuItem getSaveMenuItem() {
    if (this.saveMenuItem == null) {
      this.saveMenuItem = new JMenuItem("Zuordnungen speichern ");
      this.saveMenuItem.setMnemonic('s');
      this.saveMenuItem.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          mp3CatMain.saveContent();
        }
      });
    }
    return this.saveMenuItem;

  }

  private JMenuItem getExitMenuItem() {
    if (exitMenuItem == null) {
      exitMenuItem = new JMenuItem("Beenden");
      exitMenuItem.setMnemonic('x');
      exitMenuItem.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          getFrame().dispose();
        }
      });
    }
    return exitMenuItem;
  }
}
