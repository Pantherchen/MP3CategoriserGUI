/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.dialog;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

  private CategoryListModel categoryListModel;

  public ExportPlaylistDialog(JFrame owner, MP3DataBroker dataBroker) {
    super(owner, "Playliste einer Kategorie exportieren", true);
    this.dataBroker = dataBroker;
    initComponents();
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setAlwaysOnTop(true);
    setFocusable(true);
    setResizable(true);
    setSize(350, 250);
  }

  /**
   * 
   */
  private void initComponents() {
    getContentPane().setLayout(new MigLayout("",
                                             "12lp![grow,fill][fill]12lp!",
                                             "12lp![fill][grow,fill][fill][fill]12lp!"));
    getContentPane().add(getCategoryList(), "cell 0 0 2 0");
    getContentPane().add(getPlaylistExportTextField(), "cell 0 2");
    getContentPane().add(getPlaylistExportPathDialog(), "cell 1 2");
    getContentPane().add(getButtonPanel(), "cell 0 3 2 0");
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
      playlistExportDialogBtn = new JButton("...");
      playlistExportDialogBtn.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
          JFileChooser fd = new JFileChooser();
          fd.setDialogTitle("Exportpfad der Playliste");
          fd.setDialogType(JFileChooser.OPEN_DIALOG);
          fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          fd.showOpenDialog(ExportPlaylistDialog.this);
          if (fd.getSelectedFile() != null) {
            ExportPlaylistDialog.this.getPlaylistExportTextField().setText(fd.getSelectedFile().getAbsolutePath());
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
      categoryListModel = new CategoryListModel(categories);
      this.categoryList = new JList(categoryListModel);
      // this.categoryList.set
      this.categoryList.setCellRenderer(new CategoryCellRenderer());
      this.categoryList.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e) {
          Point p = e.getPoint();
          int index = categoryList.locationToIndex(p);
          if (index == -1) {
            e.consume();
            return;
          }
          Object[] object = (Object[])categoryListModel.getElementAt(index);
          if (object[0] != null) {
            JCheckBox chbox = (JCheckBox)object[0];
            if (chbox != null) {
              if (chbox.isSelected()) {
                chbox.setSelected(false);
              } else {
                chbox.setSelected(true);
              }
            }
          }
          ExportPlaylistDialog.this.categoryList.repaint();
          e.consume();
        }
      });
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
          CategoryListModel listmodel = (CategoryListModel)ExportPlaylistDialog.this.getCategoryList().getModel();
          List<Category> selectedCategories = listmodel.getSelectedCategories();
          if (selectedCategories != null && selectedCategories.size() > 0
              && ExportPlaylistDialog.this.getPlaylistExportTextField().getText() != null) {
            ExportPlaylistDialog.this.dataBroker.exportPlaylist(selectedCategories, ExportPlaylistDialog.this
              .getPlaylistExportTextField().getText());
            StringBuilder catBuilder = new StringBuilder();
            for (int i = 0; i < selectedCategories.size(); i++) {
              if (i > 0) {
                catBuilder.append(",");
              }
              catBuilder.append(selectedCategories.get(i).getName());
            }
            JOptionPane.showMessageDialog(ExportPlaylistDialog.this,
                                          "Dateien mit der Kategorie " + catBuilder.toString()
                                                  + " wurden erfolgreich exportiert. ",
                                          "Playlistexport",
                                          JOptionPane.INFORMATION_MESSAGE);
            ExportPlaylistDialog.this.dispose();
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
