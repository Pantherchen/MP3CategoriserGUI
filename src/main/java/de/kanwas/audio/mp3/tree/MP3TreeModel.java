/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.tree;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import de.kanwas.audio.commons.MP3Content;
import de.kanwas.audio.commons.MP3File;
import de.kanwas.audio.commons.MP3Folder;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3TreeModel extends DefaultTreeModel {
  /** version number */
  public static final String VER = "$Revision$";

  /**
   * @param root
   */
  public MP3TreeModel(TreeNode root) {
    super(root);
  }

  public void setMP3Data(List<MP3Content> collection) {
    // looking for root node
    MP3FolderNode rootNode = null;
    for (MP3Content c : collection) {
      if (c instanceof MP3Folder) {
        MP3Folder f = (MP3Folder)c;
        if (f.getParentFolder() == null) {
          this.root = rootNode = new MP3FolderNode(f);
          break;
        }
      }
    }
    // looking for the child of the root node and take this as parent
    MP3FolderNode parent = null;
    if (rootNode != null && rootNode.getFolder() != null) {
      for (MP3Content c : collection) {
        if (c instanceof MP3Folder) {
          MP3Folder f = (MP3Folder)c;
          if (rootNode.getFolder().equals(f.getParentFolder())) {
            parent = new MP3FolderNode(f);
            rootNode.add(parent);
            break;
          }
        }
      }
    }
    createTree(parent);
  }

  /**
   * 
   */
  private void createTree(MP3FolderNode parent) {
    if (parent == null || parent.getFolder() == null) {
      return;
    }
    for (MP3Content c : parent.getFolder().getMp3Content()) {
      if (c instanceof MP3Folder) {
        MP3Folder currentFolder = (MP3Folder)c;
        MP3FolderNode mp3FolderNode = new MP3FolderNode(currentFolder);
        parent.add(mp3FolderNode);
        createTree(mp3FolderNode);
      } else if (c instanceof MP3File) {
        MP3File file = (MP3File)c;
        Mp3FileNode mp3fileNode = new Mp3FileNode(file, parent);
        parent.add(mp3fileNode);
      }
    }
  }

}
