package de.kanwas.audio.mp3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import net.miginfocom.swing.MigLayout;
import de.kanwas.audio.commons.Category;
import de.kanwas.audio.mp3.table.MP3TableModel;
import de.kanwas.audio.mp3.table.Mp3CategoryCellEditor;
import de.kanwas.audio.mp3.tree.Mp3TreeModel;

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

  private final static String CATEGORYPATH=""; 
  private final static String MUSICPATH=""; 
  private final static String DATABASEPATH=""; 
  
  private JTree dirTree;

  private JScrollPane treeScrollPane;

  private JTable mp3Table;

  private JScrollPane tableScrollPane;

  private TreeNode rootNode = new DefaultMutableTreeNode("Root");

  private List<Category> categories;

  public MP3CatMain() {

    this.categories = new ArrayList<Category>();
    this.categories.add(new Category("Party"));
    this.categories.add(new Category("Grillen"));
    this.categories.add(new Category("Slow"));    
    initComponents();
  }
  
  public void initData() {
    this.dataBroker = new MP3DataBroker(, VER, VER);
  }

  private void initComponents() {
    setLayout(new MigLayout("", "12lp![fill][grow,fill]12lp!", "[grow,fill]"));
    add(getTreeScrollPane(), "cell 0 0");
    add(getTableScrollPane(), "cell 1 0");
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
      this.dirTree = new JTree(new Mp3TreeModel(rootNode));
    }
    return this.dirTree;
  }

  private void initTree(File[] files) {
    TreeNode parent = this.rootNode;
    TreeNode node = null;
    for (File file : files) {
      node = new DefaultMutableTreeNode(file);
      if (file.isDirectory()) {
        parent = node;
      }
    }
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
      this.mp3Table = new JTable(new MP3TableModel(categories));
      this.mp3Table.setCellSelectionEnabled(true);
      this.mp3Table.setAutoscrolls(true);
      this.mp3Table.setAutoCreateRowSorter(true);
      this.mp3Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

      for (int i = 0; i < this.mp3Table.getColumnCount(); i++) {
        TableColumn col = this.mp3Table.getColumnModel().getColumn(i);
        col.setCellEditor(new Mp3CategoryCellEditor());
      }
    }
    return mp3Table;
  }
}
