/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import de.kanwas.audio.commons.MP3Content;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3ContentNode extends DefaultMutableTreeNode {
  /** version number */
  public static final String VER = "$Revision$";
  private MP3Content content;

  /**
   * 
   */
  public MP3ContentNode(MP3Content c, boolean allowChildren) {
    super(c, allowChildren);
    this.content = c;
  }

  public MP3Content getContent() {
    return content;
  }
}
