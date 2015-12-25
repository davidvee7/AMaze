/**
 *
 */
package edu.wm.cs.cs301.UI;

import java.io.Serializable;

/**
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class Seg implements Serializable {
    public int x, y, dx, dy, dist;
    //public GraphicsWrapper gw;
    public boolean partition, seen;
    public int[] colorArray;
//	private GraphicsWrapper gw;
    
    /**
     * Constructor
     * @param psx
     * @param psy
     * @param pdx
     * @param pdy
     * @param cl
     * @param cc
     * @param gw 
     */
    Seg(int psx, int psy, int pdx, int pdy, int cl, int cc) {
        x = psx;
        y = psy;
        dx = pdx;
        dy = pdy;
        dist = cl; 
        seen = false;
    //    this.gw = gw;
        this.colorArray = new int[3];
        cl /= 4;
        int add = (dx != 0) ? 1 : 0; 
        int part1 = cl & 7;
        int part2 = ((cl >> 3) ^ cc) % 6;
        int val1 = ((part1 + 2 + add) * 70)/8 + 80;
        switch (part2) {
//        case 0: gw.col = gw.createColor(val1, 20, 20); break;
//        case 1: gw.col = gw.createColor(20, val1, 20); break;
//        case 2: gw.col = gw.createColor(20, 20, val1); break;
//        case 3: gw.col = gw.createColor(val1, val1, 20); break;
//        case 4: gw.col = gw.createColor(20, val1, val1); break;
//        case 5: gw.col = gw.createColor(val1, 20, val1); break;
        case 0: colorArray[0] = val1; colorArray[1] = 20; colorArray[2] = 20; break;
        case 1: colorArray[0] = 20; colorArray[1] = val1; colorArray[2] = 20; break;
        case 2: colorArray[0] = 20; colorArray[1] = 20; colorArray[2] = val1; break;
        case 3: colorArray[0] = val1; colorArray[1] = val1; colorArray[2] = 20; break;
        case 4: colorArray[0] = 20; colorArray[1] = val1; colorArray[2] = val1; break;
        case 5: colorArray[0] = val1; colorArray[1] = 20; colorArray[2] = val1; break;
        }
    }

    int getDir() {
        if (dx != 0)
            return (dx < 0) ? 1 : -1;
        return (dy < 0) ? 2 : -2;
    }
}