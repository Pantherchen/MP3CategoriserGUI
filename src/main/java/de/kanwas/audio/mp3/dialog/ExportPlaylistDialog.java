/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import de.kanwas.audio.commons.Category;
import de.kanwas.audio.mp3.MP3DataBroker;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class ExportPlaylistDialog extends JDialog {
  /** version number */
  public static final String VER = "$Revision$";

  private final MP3DataBroker dataBroker;

  private JList categoryList;

  private JButton exportButton;

  private JButton cancelButton;

  private JTextField exportFilePathTxtField;

  private JButton playlistExportDialogBtn;

  private JPanel buttonPanel;

  public ExportPlaylistDialog(JFrame owner, MP3DataBroker dataBroker) {
    super(owner, "Playliste einer Kategorie exportieren", true);
    this.dataBroker = dataBroker;
    initComponents();
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setAlwaysOnTop(true);
    setFocusable(true);
    setResizable(true);
    setSize(400, 200);
  }

  /**
   * 
   */
  private void initComponents() {
    getContentPane().setLayout(new MigLayout("", "12lp![grow,fill][fill]12lp!", "12lp![grow,fill][fill][fill]12lp!"));
    getContentPane().add(getCategoryList(), "cell 0 0 2 0");
    getContentPane().add(getPlaylistExportTextField(), "cell 0 1");
    getContentPane().add(getPlaylistExportPathDialog(), "cell 1 1");
    getContentPane().add(getButtonPanel(), "cell 0 2 2 0");
  }

  /**
   * @return
   */
  private JTextField getPlaylistExportTextField() {
    if (this.exportFilePathTxtField == null) {
      this.exportFilePathTxtField = new JTextField();
    }
    return this.exportFilePathTxtField;
  }

  /**
   * @return
   */
  private JButton getPlaylistExportPathDialog() {
    if (playlistExportDialogBtn == null) {
      playlistExportDialogBtn = new JButton();
      playlistExportDialogBtn.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          JFileChooser fd = new JFileChooser();
          fd.setDialogTitle("Exportpfad der Playliste");
          fd.setDialogType(JFileChooser.OPEN_DIALOG);
          fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          fd.showOpenDialog(ExportPlaylistDialog.this);
          if (fd.getSelectedFile() != null) {
            ExportPlaylistDialog.this.getPlaylistExportTextField().setText(fd.getSelectedFile().getName());
          }
        }
      });
    }
    return playlistExportDialogBtn;
  }

  /**
   * @return
   */
  private JList getCategoryList() {
    if (this.categoryList == null) {
      List<Category> categories = this.dataBroker.getCategories();
      String[] catNames = new String[categories.size()];
      int i = 0;
      for (Category c : categories) {
        catNames[i++] = c.getName();
      }
      this.categoryList = new JList(catNames);
      // this.categoryList.setCellRenderer(new CategoryCellRenderer());
    }
    return this.categoryList;
  }

  private JPanel getButtonPanel() {
    if (buttonPanel == null) {
      buttonPanel = new JPanel();
      buttonPanel.setLayout(new MigLayout("", "[grow,fill][fill, sg btn][fill, sg btn]", "[fill]"));
      buttonPanel.add(getExportButton(), "cell 1 0");
      buttonPanel.add(getCancelButton(), "cell 2 0");
    }
    return buttonPanel;
  }

  /**
   * @return
   */
  private JButton getExportButton() {
    if (this.exportButton == null) {
      exportButton = new JButton("Export starten");
      exportButton.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          Object o = ExportPlaylistDialog.this.getCategoryList().getSelectedValue();
          if (o instanceof Category && ExportPlaylistDialog.this.getPlaylistExportTextField().getText() != null) {
            Category category = (Category)o;
            ExportPlaylistDialog.this.dataBroker.exportPlaylist(category, ExportPlaylistDialog.this
              .getPlaylistExportTextField().getText());
          }
        }
      });
    }
    return this.exportButton;
  }

  /**
   * @return
   */
  private JButton getCancelButton() {
    if (this.cancelButton == null) {
      this.cancelButton = new JButton("Abbrechen");
      this.cancelButton.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          ExportPlaylistDialog.this.dispose();
        }
      });
    }
    return this.cancelButton;
  }

}
