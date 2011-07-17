package de.kanwas.audio.mp3;

/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import de.kanwas.audio.mp3.dialog.ExportPlaylistDialog;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3Categoriser {
  /**
   * 
   */
  private static final String OPEN_MP3_FOLDER = "MP3 Ordner öffnen";

  /** version number */
  public static final String VER = "$Revision$";

  private JFrame frame;

  private JMenuBar menuBar;

  private JMenu fileMenu;

  private JMenuItem openMenuItem;

  private JMenuItem exportPlaylistMenuItem;

  private JMenuItem saveMenuItem;

  private JMenuItem exitMenuItem;

  private MP3CatMain mp3Main;

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

  private JFrame getFrame() {
    if (this.frame == null) {
      this.frame = new JFrame();
      this.frame.setJMenuBar(getMenuBar());
      this.frame.add(getMain());
      this.frame.addWindowListener(new WindowAdapter(){

        @Override
        public void windowClosing(WindowEvent e) {
          if (checkDirty()) {
            super.windowClosing(e);
            MP3Categoriser.this.frame.setVisible(false);
            System.exit(0);
          }
        }

      });
    }
    return this.frame;
  }

  private MP3CatMain getMain() {
    if (mp3Main == null) {
      mp3Main = new MP3CatMain();
    }
    return mp3Main;
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
      this.fileMenu.setMnemonic(KeyEvent.VK_D);
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
      this.exportPlaylistMenuItem.setMnemonic(KeyEvent.VK_E);
      this.exportPlaylistMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK));
      this.exportPlaylistMenuItem.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          if (checkDirty()) {
            ExportPlaylistDialog dialog = new ExportPlaylistDialog(MP3Categoriser.this.getFrame(), getMain()
              .getMP3DataBroker());
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(MP3Categoriser.this.getFrame());
          }
        }
      });
    }
    return this.exportPlaylistMenuItem;
  }

  private JMenuItem getOpenMenuItem() {
    if (this.openMenuItem == null) {
      this.openMenuItem = new JMenuItem(OPEN_MP3_FOLDER);
      this.openMenuItem.setMnemonic(KeyEvent.VK_O);
      this.openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK));
      this.openMenuItem.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          if (checkDirty()) {
            JFileChooser fd = new JFileChooser(PropertyHandler.getInstance().getMusicPath());
            fd.setDialogType(JFileChooser.OPEN_DIALOG);
            fd.setDialogTitle(OPEN_MP3_FOLDER);
            fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fd.showOpenDialog(MP3Categoriser.this.getFrame());
            File mp3Dir = fd.getSelectedFile();
            if (mp3Dir != null) {
              MP3Categoriser.this.getMain().setMP3Path(mp3Dir.getAbsolutePath());
            }
          }
        }
      });
    }
    return this.openMenuItem;
  }

  private JMenuItem getSaveMenuItem() {
    if (this.saveMenuItem == null) {
      this.saveMenuItem = new JMenuItem("Zuordnungen speichern ");
      this.saveMenuItem.setMnemonic(KeyEvent.VK_S);
      this.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK));
      this.saveMenuItem.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          getMain().saveContent();
        }
      });
    }
    return this.saveMenuItem;

  }

  private JMenuItem getExitMenuItem() {
    if (exitMenuItem == null) {
      exitMenuItem = new JMenuItem("Beenden");
      exitMenuItem.setMnemonic(KeyEvent.VK_Q);
      exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK));
      exitMenuItem.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          if (checkDirty()) {
            getFrame().dispose();
          }
        }
      });
    }
    return exitMenuItem;
  }

  /**
   * @return true to continue with the next operation, else false
   */
  private boolean checkDirty() {
    if (getMain().isDirty()) {
      int option = getMain().showWantSaveDialog();
      return getMain().checkUserSelection(option, getMain().getCurrentTreeSelection());
    }
    return true;
  }
}
