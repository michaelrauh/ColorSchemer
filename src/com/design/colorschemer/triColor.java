package com.design.colorschemer;

public class triColor implements Comparable<triColor> {

    public int R;
    public int G;
    public int B;

    public int compareTo(triColor other) {
        if (R != other.R) {
            if (R < other.R) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (G != other.G) {
                if (G < other.G) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (B == other.B) {
                    return 0;
                } else if (B < other.B) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

}
