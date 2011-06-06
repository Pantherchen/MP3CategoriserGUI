package de.kanwas.audio.mp3;

import java.awt.Component;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;
import de.kanwas.audio.commons.MP3File;
import de.kanwas.audio.mp3.messages.MessageIF;
import de.kanwas.audio.mp3.messages.MessageUtil;
import de.kanwas.audio.mp3.table.MP3CategoryCellEditor;
import de.kanwas.audio.mp3.table.MP3TableCellCenderer;
import de.kanwas.audio.mp3.table.MP3TableModel;
import de.kanwas.audio.mp3.tree.MP3ContentNode;
import de.kanwas.audio.mp3.tree.MP3TreeModel;

/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3CatMain extends JPanel {

  /** version number */
  public static final String VER = "$Revision$";

  /**
   * 
   */
  private static final String ROOT_NODE_NAME = "MP3 Sammlung";

  private JTree dirTree;

  private JScrollPane treeScrollPane;

  private JTable mp3Table;

  private JScrollPane tableScrollPane;

  private DefaultMutableTreeNode rootNode;

  private MP3DataBroker dataBroker;

  private MP3TableModel mp3TableModel;

  private JSplitPane splitPane;

  private MP3TreeModel treeModel;

  public MP3CatMain() {
    initData();
    initComponents();
  }

  public void initData() {
    PropertyHandler propertyHandler = PropertyHandler.getInstance();
    this.dataBroker = new MP3DataBroker(propertyHandler.getCategoryPath(),
                                        propertyHandler.getMusicPath(),
                                        propertyHandler.getDatabasePath());
  }

  public void setMP3Path(String mp3Path) {
    this.getMP3DataBroker().setMp3Path(mp3Path);
    clearTreeModel();
    getDirTree().setModel(getTreeModel());
    getTreeModel().reload();
    this.getDirTree().updateUI();
  }

  /**
   * 
   */
  private void clearTreeModel() {
    this.treeModel = null;
    rootNode = new DefaultMutableTreeNode(ROOT_NODE_NAME);
    getMP3DataBroker().clearCollection();
  }

  public MP3DataBroker getMP3DataBroker() {
    return this.dataBroker;
  }

  private void initComponents() {
    setLayout(new MigLayout("", "12lp![grow,fill]12lp!", "[grow,fill]"));
    add(getSplitPane(), "cell 0 0");
    // add(getTreeScrollPane(), "cell 0 0");
    // add(getTableScrollPane(), "cell 1 0");
  }

  /**
   * @return
   */
  private Component getSplitPane() {
    if (this.splitPane == null) {
      this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      this.splitPane.setLeftComponent(getTreeScrollPane());
      this.splitPane.setRightComponent(getTableScrollPane());
    }
    return splitPane;
  }

  public JScrollPane getTreeScrollPane() {
    if (this.treeScrollPane == null) {
      this.treeScrollPane = new JScrollPane();
      this.treeScrollPane.setViewportView(getDirTree());
    }
    return this.treeScrollPane;
  }

  private JTree getDirTree() {
    if (this.dirTree == null) {
      rootNode = new DefaultMutableTreeNode(ROOT_NODE_NAME);
      this.dirTree = new JTree(getTreeModel());
      this.dirTree.addTreeSelectionListener(new TreeSelectionListener(){

        @Override
        public void valueChanged(TreeSelectionEvent e) {
          TreePath path = e.getPath();
          Object o = path.getLastPathComponent();
          if (o instanceof MP3ContentNode) {
            MP3ContentNode c = (MP3ContentNode)o;
            int resetContent = 0;
            if (MP3CatMain.this.getTableModel().isDirty()) {
              resetContent = showWantSaveDialog();
            }
            if (resetContent == 0) {
              saveContent();
            } else if (resetContent == 1) {
              MP3CatMain.this.getTableModel().resetMP3Data();
            }
            if (resetContent == 0 || resetContent == 1) {
              MP3CatMain.this.getTableModel()
                .setMP3Data(c.getContent(), MP3CatMain.this.getMP3DataBroker().readFiles());
            }
          }
        }
      });
    }
    return this.dirTree;
  }

  private MP3TreeModel getTreeModel() {
    if (this.treeModel == null) {
      treeModel = new MP3TreeModel(rootNode);
      treeModel.setMP3Data(this.getMP3DataBroker().getMP3Collection());
    }
    return treeModel;
  }

  public JScrollPane getTableScrollPane() {
    if (this.tableScrollPane == null) {
      this.tableScrollPane = new JScrollPane();
      this.tableScrollPane.setViewportView(getMP3Table());
    }
    return this.tableScrollPane;
  }

  private JTable getMP3Table() {
    if (this.mp3Table == null) {
      this.mp3TableModel = new MP3TableModel(this.getMP3DataBroker().getCategories());
      this.mp3Table = new JTable(this.mp3TableModel);
      this.mp3Table.setCellSelectionEnabled(true);
      this.mp3Table.setAutoscrolls(true);
      this.mp3Table.setAutoCreateRowSorter(true);
      this.mp3Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

      for (int i = 0; i < this.mp3Table.getColumnCount(); i++) {
        TableColumn col = this.mp3Table.getColumnModel().getColumn(i);
        if (i < MP3TableModel.getIndexGeneralColumns()) {
          col.setPreferredWidth(200);
        }
        col.setCellEditor(new MP3CategoryCellEditor());
        col.setCellRenderer(new MP3TableCellCenderer());
      }
    }
    return mp3Table;
  }

  private int showWantSaveDialog() {
    String text = MessageUtil.getInstance().getMessage(MessageIF.SAVECHANGES);
    String title = MessageUtil.getInstance().getMessage(MessageIF.SAVECHANGESTITLE);
    Object[] options = new Object[3];
    options[0] = MessageUtil.getInstance().getMessage(MessageIF.SAVE);
    options[1] = "Verwerfen";
    options[2] = "Abbrechen";
    int result = JOptionPane.showOptionDialog(this,
                                              text,
                                              title,
                                              JOptionPane.DEFAULT_OPTION,
                                              JOptionPane.QUESTION_MESSAGE,
                                              null,
                                              options,
                                              options[0]);

    return result;
  }

  /**
   * 
   */
  public void saveContent() {
    List<MP3File> files = getTableModel().getMP3Files();
    this.getMP3DataBroker().writeFiles(files);

  }

  private MP3TableModel getTableModel() {
    return this.mp3TableModel;
  }
}
