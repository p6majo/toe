package com.princeton;

import com.p6majo.info.aachen.Kon;
import org.junit.Test;

public class StdDrawTest {

@Test
public  void start() {
    StdDraw.setPenRadius(0.05);
    StdDraw.setPenColor(StdDraw.BLUE);
    StdDraw.point(0.5, 0.5);
    StdDraw.setPenColor(StdDraw.MAGENTA);
    StdDraw.line(0.2, 0.2, 0.8, 0.2);
    Kon.readInt();
}

}
