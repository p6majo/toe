package com.p6majo.plot;

import com.princeton.Out;

public class PlotOptions {

    public static enum OutputOption {DEFAULT,FILE,DRAW,EPS,GEOGEBRA,POVRAY};
    public static enum StyleOption {DEFAULT,SOLID,DOTTED,DASHED};
    public static enum TypeOption {DEFAULT};

    private OutputOption outputOption = OutputOption.DEFAULT;
    private StyleOption styleOption = StyleOption.DEFAULT;
    private TypeOption typeOption = TypeOption.DEFAULT;

    private int contourLines =0;
    private boolean logScaleZ = false;


    public OutputOption getOutputOption() {
        return outputOption;
    }

    public void setOutputOption(OutputOption outputOption) {
        this.outputOption = outputOption;
    }

    public StyleOption getStyleOption() {
        return styleOption;
    }

    public void setStyleOption(StyleOption styleOption) {
        this.styleOption = styleOption;
    }

    public TypeOption getTypeOption() {
        return typeOption;
    }

    public void setTypeOption(TypeOption typeOption){
        this.typeOption=typeOption;
    }

    public int getContourLines() {
        return contourLines;
    }

    public void setContourLines(int contourLines) {
        this.contourLines = contourLines;
    }

    public boolean isLogScaleZ() {
        return logScaleZ;
    }

    public void setLogScaleZ(boolean logScaleZ) {
        this.logScaleZ = logScaleZ;
    }

}
