/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.tree;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import de.kanwas.audio.commons.MP3Content;
import de.kanwas.audio.commons.MP3File;
import de.kanwas.audio.commons.MP3Folder;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class Mp3TreeModel extends DefaultTreeModel {
  /** version number */
  public static final String VER = "$Revision$";

  /**
   * @param root
   */
  public Mp3TreeModel(TreeNode root) {
    super(root);
  }

  public void setMP3Data(List<MP3Content> collection) {

    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)getRoot();
    DefaultMutableTreeNode node = null;
    MP3FolderNode folderNode = null;
    MP3Folder folder = null;
    for (MP3Content content : collection) {
      if (content instanceof MP3Folder) {
        folder = (MP3Folder)content;
        folderNode = new MP3FolderNode(folder);
        if (!containsNode(parent, folderNode)) {
          for (MP3File file : folder.getMp3Files()) {
            node = new Mp3FileNode(file, folderNode);
            folderNode.addMP3File(file);
            folderNode.add(node);
          }
          parent.add(folderNode);
        }
      }
    }
  }

  private boolean containsNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode node) {
    for (int i = 0; i < parent.getChildCount(); i++) {
      if (parent.getChildAt(i).equals(node)) {
        return true;
      }
    }
    return false;
  }

}
