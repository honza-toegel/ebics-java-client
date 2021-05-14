/*
 * Copyright (c) 1990-2012 kopiLeft Development SARL, Bizerte, Tunisia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id$
 */

package org.ebics.client.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ebics.client.exception.EbicsException;
import org.ebics.client.interfaces.EbicsRootElement;
import org.ebics.client.interfaces.TraceManager;
import org.ebics.client.interfaces.Configuration;
import org.ebics.client.io.FileCache;
import org.ebics.client.io.IOUtils;
import org.ebics.client.user.EbicsSession;


/**
 * The <code>DefaultTraceManager</code> aims to trace an ebics
 * transferable element in an instance of <code>java.io.File</code>
 * then saved to a trace directory.
 * The manager can delete all traces file if the configuration does
 * not offer tracing support.
 * see {@link Configuration#isTraceEnabled() isTraceEnabled()}
 *
 * @author hachani
 *
 */
public class DefaultTraceManager implements TraceManager {

  /**
   * Constructs a new <code>TraceManger</code> to manage transfer traces.
   * @param isTraceEnabled is trace enabled?
   */
  public DefaultTraceManager(boolean isTraceEnabled) {
    cache = new FileCache(isTraceEnabled);
  }

  /**
   * Constructs a new <code>TraceManger</code> with trace option enabled.
   */
  public DefaultTraceManager() {
    this(true);
  }

  @Override
  public void trace(EbicsRootElement element, EbicsSession session) throws EbicsException {
    try {
      FileOutputStream		out;
      File			file;
      File        traceDir = new File(session.getConfiguration().getTransferTraceDirectory(session.getUser()));

      file = IOUtils.createFile(traceDir, element.getName());
      out = new FileOutputStream(file);
      element.save(out);
      cache.add(file);
    } catch (IOException e) {
      throw new EbicsException(e.getMessage());
    }
  }

  @Override
  public void remove(EbicsRootElement element) {
    cache.remove(element.getName());
  }

  @Override
  public void clear() {
    cache.clear();
  }

  @Override
  public void setTraceEnabled(boolean enabled) {
    cache.setTraceEnabled(enabled);
  }

  // --------------------------------------------------------------------
  // DATA MEMBERS
  // --------------------------------------------------------------------

  private FileCache			cache;
}
