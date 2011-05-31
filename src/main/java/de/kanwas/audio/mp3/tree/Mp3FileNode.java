/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.tree;

import de.kanwas.audio.commons.MP3File;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class Mp3FileNode extends MP3ContentNode {
  /** version number */
  @SuppressWarnings("hiding")
  public static final String VER = "$Revision$";

  private final MP3File mp3;

  private MP3FolderNode folderNode;

  public Mp3FileNode(MP3File mp3, MP3FolderNode folder) {
    super(mp3, true);
    this.mp3 = mp3;
    this.folderNode = folder;

  }

  public MP3File getMP3File() {
    return this.mp3;
  }

  public MP3FolderNode getFolderNode() {
    return this.folderNode;
  }

  @Override
  public String toString() {
    return this.mp3.getFile().getName();
  }
}
