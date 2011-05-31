/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.tree;

import java.util.ArrayList;
import java.util.List;

import de.kanwas.audio.commons.MP3File;
import de.kanwas.audio.commons.MP3Folder;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3FolderNode extends MP3ContentNode {
  /** version number */
  @SuppressWarnings("hiding")
  public static final String VER = "$Revision$";

  private final List<MP3File> mp3s;

  private MP3Folder file;

  public MP3FolderNode(MP3Folder file) {
    super(file, true);
    this.file = file;
    this.mp3s = new ArrayList<MP3File>();
  }

  public List<MP3File> getMP3Files() {
    return this.mp3s;
  }

  public void addMP3File(MP3File mp3file) {
    if (!this.mp3s.contains(mp3file)) {
      this.mp3s.add(mp3file);
    }
  }

  public MP3Folder getFolder() {
    return this.file;
  }

  @Override
  public String toString() {
    return this.file.getFolder().getName();
  }

}
