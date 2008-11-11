/*
 * $Id$
 *
 * Copyright (c) 2008 by Joel Uckelman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available 
 * at http://www.opensource.org.
 */

package VASSAL.tools.memmap;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;

import VASSAL.build.GameModule;
import VASSAL.tools.DataArchive;
import VASSAL.tools.ImageUtils;
import VASSAL.tools.IOUtils;
import VASSAL.tools.TempFileManager;

/**
 * @author Joel Uckelman
 * @since 3.1.0
 */ 
public class MappedImageUtils {

  private MappedImageUtils() {}

  public static MappedBufferedImage getImage(String name, DataArchive archive)
                                                           throws IOException {
    MappedBufferedImage img = null;
    InputStream in = null;
    try {
      in = archive.getImageInputStream(name);
      img = loadWithImageIOIfOk(in);
      in.close();

      if (img == null) {
        in = archive.getImageInputStream(name);
        img = loadWithToolkit(in);
        in.close();
      }
    }
    finally {
      IOUtils.closeQuietly(in);
    }

    switch (img.getType()) {
    case BufferedImage.TYPE_INT_RGB:
    case BufferedImage.TYPE_INT_ARGB:
      return img;
    default:
      return toType(img, img.getTransparency() == BufferedImage.OPAQUE ?
        BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
    }
  }

  private static MappedBufferedImage loadWithImageIOIfOk(InputStream in)
                                                          throws IOException {
    final ImageInputStream iis = new FileCacheImageInputStream(in,
      TempFileManager.getInstance().getSessionRoot());

    final ImageReader reader = ImageUtils.checkImageIOCanRead(iis);
    if (reader == null) return null;

    try {
      final int w = reader.getWidth(0);
      final int h = reader.getHeight(0);

      final ImageTypeSpecifier type = reader.getImageTypes(0).next();

      // get our ColorModel and SampleModel
     final ColorModel cm = type.getColorModel();
      final SampleModel sm =
        type.getSampleModel().createCompatibleSampleModel(w,h);
      
      final MappedBufferedImage img = new MappedBufferedImage(cm, sm);
      
      final ImageReadParam param = reader.getDefaultReadParam();
      param.setDestination(img);
      reader.read(0, param);
      return img;
    }
    finally {
      reader.dispose();
    }
  }
  
  private static MappedBufferedImage loadWithToolkit(InputStream in)
                                                          throws IOException {
    final Image src = ImageUtils.forceLoad(
      Toolkit.getDefaultToolkit().createImage(IOUtils.getBytes(in)));

    final MappedBufferedImage dst = new MappedBufferedImage(
      src.getWidth(null), src.getHeight(null),
      ImageUtils.isTransparent(src) ?
        BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB
    );

    final Graphics2D g = dst.createGraphics();
    g.drawImage(src, 0, 0, null);
    g.dispose();

    return dst;
  }

  private static MappedBufferedImage toType(MappedBufferedImage src, int type)
                                                           throws IOException {
    return rowByRowCopy(
      src, 
      new MappedBufferedImage(src.getWidth(), src.getHeight(), type)
    );
  }

  private static MappedBufferedImage rowByRowCopy(MappedBufferedImage src,
                                                  MappedBufferedImage dst) {
    final int h = src.getHeight();
    final int[] row = new int[src.getWidth()];
    for (int y = 0; y < h; ++y) {
      src.getRGB(0, y, row.length, 1, row, 0, row.length);
      dst.setRGB(0, y, row.length, 1, row, 0, row.length);
    }
    return dst;
  }
}
