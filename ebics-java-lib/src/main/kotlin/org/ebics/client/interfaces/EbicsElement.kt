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
package org.ebics.client.interfaces

import org.ebics.client.exception.EbicsException
import java.io.PrintStream
import java.io.Serializable

interface EbicsElement : Serializable {
    /**
     * Returns the name of this `EbicsElement`
     * @return the name of the element
     */
    val name: String

    /**
     * Builds the `EbicsElement` XML fragment
     * @throws EbicsException
     */
    @Throws(EbicsException::class)
    fun build()

    /**
     * Prints the `EbicsElement` into
     * the given stream.
     * @param stream the print stream
     */
    fun print(stream: PrintStream)
}