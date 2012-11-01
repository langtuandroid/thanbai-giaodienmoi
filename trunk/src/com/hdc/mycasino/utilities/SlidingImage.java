package com.hdc.mycasino.utilities;

/***
 * 
 * Copyright (C) 2008 Alessandro La Rosa
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: alessandro.larosa@gmail.com
 *
 * Author: Alessandro La Rosa
 */

import com.danh.standard.Graphics;
import com.danh.standard.Image;

public class SlidingImage {
	// constants defining the 2 types of sliding
	public static final int SLIDE_IN = 0;
	public static final int SLIDE_OUT = 1;

	// image type of sliding
	public int slideType = SLIDE_OUT;

	// pieces of the sliding image
	public int pieces = 4;

	// direction of sliding (up, right, down or left)
	public int direction = 0;

	// image object and properties
	Image image = null;
	int imageWidth = 0;
	int imageHeight = 0;

	// transition time properties
	int duration = 0;
	long startTime = 0;

	// transition state
	public boolean sliding = false;
	public boolean ended = false;

	public SlidingImage(Image image, int pieces, int slideType) {
		this.image = image;

		this.imageWidth = image.getWidth();
		this.imageHeight = image.getHeight();

		this.slideType = slideType;
		this.pieces = pieces;
	}

	public void reset() {
		ended = false;
		sliding = false;
	}

	public void reset(int pieces, int slideType) {
		reset();

		this.pieces = pieces;
		this.slideType = slideType;
	}

	public void slide(int direction, int duration) {
		this.direction = direction;

		this.duration = duration;
		this.startTime = System.currentTimeMillis();

		ended = false;
		sliding = true;
	}

	public void paint(Graphics g, int x, int y, int anchor) {
		if (!sliding && ((ended && slideType == SLIDE_IN) || (!ended && slideType == SLIDE_OUT))) {
			g.drawImage(image, x, y, anchor);
		} else if (sliding) {
			// we translate accordingly to coordinates and anchor point
			int translateX = (anchor & Graphics.RIGHT) > 0 ? x - imageWidth
					: ((anchor & Graphics.HCENTER) > 0 ? x - imageWidth / 2 : x);
			int translateY = (anchor & Graphics.BOTTOM) > 0 ? y - imageHeight
					: ((anchor & Graphics.VCENTER) > 0 ? y - imageHeight / 2 : y);

			g.translate(translateX, translateY);

			// now we get current clipping properties
			int cx, cy, cw, ch;

			cx = g.getClipX();
			cy = g.getClipY();
			cw = g.getClipWidth();
			ch = g.getClipHeight();

			// let's get current duration and check if effect has ended
			int timeDiff = (int) (System.currentTimeMillis() - startTime);

			if (timeDiff > duration) {
				timeDiff = duration;

				ended = true;

				sliding = false;
			}
			// if it's a SLIDE_IN effect, we reverse the current time
			if (slideType == SLIDE_IN) {
				timeDiff = duration - timeDiff;
			}

			// now, let's pre-calculate some coords/properties
			int pieceEnd = 0;
			int pieceCoord = 0;
			int pieceSize = (direction == 0 || direction == 1) ? imageHeight / pieces : imageWidth
					/ pieces;

			int singleSlideDuration = (int) duration / pieces;

			switch (direction) {
			case 0:
				pieceEnd = cx - imageWidth;
				break;
			case 1:
				pieceEnd = cx + cw;
				break;
			case 2:
				pieceEnd = cy - imageHeight;
				break;
			case 3:
				pieceEnd = cy + ch;
				break;
			}

			// finally, we can paint single slide pieces
			for (int i = 0; i < pieces; i++) {
				// check if this piece has not to be painted (is out of clipping
				// region)
				if (timeDiff > (i + 1) * singleSlideDuration) {
					continue;
				} else {
					pieceCoord = (timeDiff < i * singleSlideDuration) ? 0 : pieceEnd
							* (timeDiff - i * singleSlideDuration) / singleSlideDuration;

					if (direction == 0 || direction == 1) {
						g.setClip(pieceCoord, i * pieceSize, imageWidth, pieceSize);
						g.drawImage(image, pieceCoord, 0, Graphics.TOP | Graphics.LEFT);
					} else {
						g.setClip(i * pieceSize, pieceCoord, pieceSize, imageHeight);
						g.drawImage(image, 0, pieceCoord, Graphics.TOP | Graphics.LEFT);
					}
				}
			}
			// restore clipping properties and translate back
			g.setClip(cx, cy, cw, ch);

			g.translate(-translateX, -translateY);
		}
	}
}