/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.tree;

import java.io.File;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class Mp3TreeModel extends DefaultTreeModel {
  /** version number */
  @SuppressWarnings("hiding")
  public static final String VER = "$Revision$";

  /**
   * @param root
   */
  public Mp3TreeModel(TreeNode root) {
    super(root);
  }

}
