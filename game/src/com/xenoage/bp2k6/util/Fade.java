/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.xenoage.bp2k6.util;

import com.xenoage.bp2k6.GameEngine;
import net.sf.jirr.*;


/**
 * Class for nice fade effects.
 *
 * @author Markus Madeja
 * @author Andreas Wenger
 */
public class Fade
{

  private static int ScreenWidth = 0;
  private static int ScreenHeight = 0;

  private static ITexture rt = null;
  private static Boolean fillrt = false;
  private static Boolean fillrt2 = false;
  private static float rttime = 0;
  private static float rtstarttime = 0;
  private static int rtart = 0;
  private static int TextureSize = 512;

  private static recti rect1 = new recti(), rect2 = new recti();
  private static recti rectTexture = new recti();
  private static recti rectScreen = new recti();


  public Fade(IVideoDriver videoDriver)
  {
    dimension2di d = GameEngine.getNativeScreenDimension();
    //Creat render target
    ScreenHeight = d.getHeight();
    ScreenWidth = d.getWidth();
    if (ScreenHeight >= 1024)
      TextureSize = 1024;
    if (videoDriver.queryFeature(E_VIDEO_DRIVER_FEATURE.EVDF_RENDER_TO_TARGET))
    {
      try
      {
        rt = videoDriver.createRenderTargetTexture(new dimension2di(TextureSize,
          TextureSize));
      }
      catch (Exception Ex)
      {
        rt = null;
      }
    }
    rectTexture = new recti(0, 0, TextureSize, TextureSize);
    rectScreen = new recti(0, ScreenHeight, ScreenWidth, 0);
  }


  /**
   * Will fade this frame to the frames after
   *
   * @param art
   * 0 = random
   * 1 = falling picture
   * 2 = lines
   * 3 = lines 2
   * 8 = fade (doesn't work yet)
   * 9 = fade to white
   * 10 = fade to black
   * default = falling stones
   * @param time
   * how much time should the fade take?
   */

  public static void fade(int art, float time)
  {
    rtstarttime = GameEngine.getTime(false);
    rttime = GameEngine.getTime(false) + time;
    rtart = (rt == null) ? 9 : art;
    fillrt = (rt != null && art < 9);
  }


  /**
   * Do the fade
   *
   * @see fade
   */
  public static void doFade()
  {
    if (fillrt)
      GameEngine.setRenderTarget(rt);
  }


  /**
   * Do the fade part2
   *
   * @see fade
   */
  public static void doFade2(IVideoDriver videoDriver)
  {
    int a;
    position2di p1 = new position2di(0, 0);
    float gettime;
    int d = 32;
    float w = ((float) TextureSize * d) / ScreenWidth, h = ((float) TextureSize * d)
      / ScreenHeight;

    if (fillrt)
    {
      GameEngine.setRenderTarget(null);
      videoDriver.draw2DImage(rt, rectScreen, rectTexture);
      fillrt = false;
    }
    else
    {
      if (rttime != 0 && !fillrt2)
      {
        gettime = GameEngine.getFrameTime(false);
        if (rttime <= gettime)
        {
          rttime = 0;
        }
        else
        {
          //massiver Zeitgewinn
          switch (rtart)
          {
            case 0:
              rtart = (new java.util.Random()).nextInt(3) + 1; //ANDI: only small image and falling stones
            case 102:
              videoDriver.draw2DRectangle(new SColor(
                255 - (int) (255 * (gettime - rtstarttime) / (rttime - rtstarttime)), 0,
                0, 0), rectScreen);
              break;
            case 101:
              videoDriver.draw2DRectangle(new SColor(
                255 - (int) (255 * (gettime - rtstarttime) / (rttime - rtstarttime)),
                255, 255, 255), rectScreen);
              break;
            //Black
            case 10:
              videoDriver.draw2DRectangle(new SColor(128, 0, 0, 0), rectScreen);
              rtart = 102;
              break;

            //White
            case 9:
              videoDriver.draw2DRectangle(new SColor(128, 255, 255, 255), rectScreen);
              rtart = 101;
              break;

            //Einfaches Überblenden, buggy wegen fehlerhafter draw2DImage Implementation (diese schweine)
            case 8:
              //TODO: Create SColor instances one time, then use set(...)
              SColor color = new SColor(
                255 - (int) (255 * (gettime - rtstarttime) / (rttime - rtstarttime)),
                  255, 255, 255);
              SColor[] colorArray = new SColor[]{
                color, color, color, color};
              videoDriver.draw2DImage(rt, rectScreen, rectTexture,
                null, colorArray);
              break;
            //Linien wegziehen
            case 4:
              for (int y = ScreenHeight / d; y >= 0; y--)
              {
                a = (int) (java.lang.Math.cos(0.5 * java.lang.Math.PI
                  * (gettime - rtstarttime) / (rttime - rtstarttime)) * ScreenWidth);
                //bescheuerte Zuweisung wegen Garbagecollector und anzeigen
                if ((y % 2) == 0)
                {
                  p1.setX(0);
                  p1.setY(y * d);
                  rect1.setUpperLeftCorner(p1);
                  p1.setX(a);
                  p1.setY(y * d + d);
                  rect1.setLowerRightCorner(p1);
                  p1.setX((int) ((ScreenWidth - a) * w / d));
                  p1.setY((int) (TextureSize - y * h));
                  rect2.setUpperLeftCorner(p1);
                  p1.setX(TextureSize);
                  p1.setY((int) (TextureSize - (y + 1) * h));
                  rect2.setLowerRightCorner(p1);
                }
                else
                {
                  p1.setX(ScreenWidth - a);
                  p1.setY(y * d);
                  rect1.setUpperLeftCorner(p1);
                  p1.setX(ScreenWidth);
                  p1.setY(y * d + d);
                  rect1.setLowerRightCorner(p1);
                  p1.setX(0);
                  p1.setY((int) (TextureSize - y * h));
                  rect2.setUpperLeftCorner(p1);
                  p1.setX((int) (a * w / d));
                  p1.setY((int) (TextureSize - (y + 1) * h));
                  rect2.setLowerRightCorner(p1);

                }
                videoDriver.draw2DImage(rt, rect1, rect2);
              }

              break;
            //Linien weg
            case 20000: //ANDI; 2
              for (int y = ScreenHeight / d; y >= 0; y--)
              {
                a = (int) (java.lang.Math.cos(0.5 * java.lang.Math.PI
                  * (gettime - rtstarttime) / (rttime - rtstarttime)) * ScreenWidth);
                //bescheuerte Zuweisung wegen Garbagecollector und anzeigen
                if ((y % 2) == 0)
                {
                  p1.setX(0);
                  p1.setY(y * d);
                  rect1.setUpperLeftCorner(p1);
                  p1.setX(a);
                  p1.setY(y * d + d);
                  rect1.setLowerRightCorner(p1);
                  p1.setX(0);
                  p1.setY((int) (TextureSize - y * h));
                  rect2.setUpperLeftCorner(p1);
                  p1.setX((int) (a * w / d));
                  p1.setY((int) (TextureSize - (y + 1) * h));
                  rect2.setLowerRightCorner(p1);
                }
                else
                {
                  p1.setX(ScreenWidth - a);
                  p1.setY(y * d);
                  rect1.setUpperLeftCorner(p1);
                  p1.setX(ScreenWidth);
                  p1.setY(y * d + d);
                  rect1.setLowerRightCorner(p1);
                  p1.setX((int) ((ScreenWidth - a) * w / d));
                  p1.setY((int) (TextureSize - y * h));
                  rect2.setUpperLeftCorner(p1);
                  p1.setX(TextureSize);
                  p1.setY((int) (TextureSize - (y + 1) * h));
                  rect2.setLowerRightCorner(p1);

                }
                videoDriver.draw2DImage(rt, rect1, rect2);
              }

              break;
            //kleines Bild
            case 1:

              //kleiner
              a = (int) (ScreenWidth * 0.5 * (gettime - rtstarttime) / (rttime - rtstarttime));

              //Zuweisung
              p1
                .setX((int) (ScreenHeight * 0.25 * (gettime - rtstarttime) / (rttime - rtstarttime))
                  + a);

              //Newton:
              //s=1/2*a*t^2+b*t
              p1
                .setY((int) ((2 * (200 + ScreenHeight) / ((rttime - rtstarttime) * (rttime - rtstarttime)))
                  * 0.5
                  * (gettime - rtstarttime)
                  * (gettime - rtstarttime)
                  - 200
                  * (gettime - rtstarttime) / (rttime - rtstarttime) + 0.75 * a));
              rect1.setUpperLeftCorner(p1);
              p1.setX(p1.getX() - a + ScreenWidth - a);
              p1.setY((int) (p1.getY() - a * 0.75 + ScreenHeight - a * 0.75));
              rect1.setLowerRightCorner(p1);
              p1.setX(0);
              p1.setY(TextureSize);
              rect2.setUpperLeftCorner(p1);
              p1.setX(TextureSize);
              p1.setY(0);
              rect2.setLowerRightCorner(p1);
              videoDriver.draw2DImage(rt, rect1, rect2);

              break;
            //Fallende Steine

            case 2: //ANDI; default:
              for (int x = 0; x < ScreenWidth / d; x++)
              {
                for (int y = ScreenHeight / d; y >= 0; y--)
                {
                  //Fallgeschwindigkeit berechnen
                  a = (int) ((2 * (ScreenWidth - y * d + (((x * x + y) << 1) ^ (x * x + y)) * 4) / ((rttime - rtstarttime) * (rttime - rtstarttime)))
                    * 0.5 * (gettime - rtstarttime) * (gettime - rtstarttime));
                  if (y * d + a < ScreenHeight)
                  {
                    //bescheuerte Zuweisung wegen Garbagecollector und anzeigen
                    p1.setX(x * d);
                    p1.setY(y * d + a);
                    rect1.setUpperLeftCorner(p1);
                    p1.setX(x * d + d);
                    p1.setY(y * d + d + a);
                    rect1.setLowerRightCorner(p1);
                    p1.setX((int) (x * w));
                    p1.setY((int) (((ScreenHeight / d) - y + 1) * h));
                    rect2.setUpperLeftCorner(p1);
                    p1.setX((int) ((x + 1) * w));
                    p1.setY((int) (((ScreenHeight / d) - y) * h));
                    rect2.setLowerRightCorner(p1);
                    videoDriver.draw2DImage(rt, rect1, rect2);
                  }
                }
              }
              break;

            //Verwehende Steine

            case 3:
              for (int x = 0; x < ScreenWidth / d; x++)
              {
                for (int y = ScreenHeight / d; y >= 0; y--)
                {
                  //Fallgeschwindigkeit berechnen
                  a = (int) ((2 * (ScreenWidth - y * d + (((x * x + y) << 1) ^ (x * x + y)) * 4) / ((rttime - rtstarttime) * (rttime - rtstarttime)))
                    * 0.5 * (gettime - rtstarttime) * (gettime - rtstarttime));
                  if (y * d + a < ScreenHeight)
                  {
                    //Halbe Fallgeschwindigkeit auch zur Seite
                    p1.setX(x * d + (a >> 1));
                    p1.setY(y * d + a);
                    rect1.setUpperLeftCorner(p1);
                    p1.setX(x * d + d + (a >> 1));
                    p1.setY(y * d + d + a);
                    rect1.setLowerRightCorner(p1);
                    p1.setX((int) (x * w));
                    p1.setY((int) (((ScreenHeight / d) - y + 1) * h));
                    rect2.setUpperLeftCorner(p1);
                    p1.setX((int) ((x + 1) * w));
                    p1.setY((int) (((ScreenHeight / d) - y) * h));
                    rect2.setLowerRightCorner(p1);
                    videoDriver.draw2DImage(rt, rect1, rect2);
                  }
                }
              }
              break;
          }
        }
      }
    }
    /*
     //Puffer übertragen
     fillrt = fillrt2;
     fillrt2 = false;
     */
    fillrt = false;

  }


  public static int getTextureSize()
  {
    return TextureSize;
  }

}
