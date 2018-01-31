package com.nr.stat;

import org.netlib.util.doubleW;

public interface QuadvlInf {
  void quadvl(final double x, final double y,
              doubleW fa, doubleW fb, doubleW fc, doubleW fd);
}
