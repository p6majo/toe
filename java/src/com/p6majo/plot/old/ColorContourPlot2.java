package com.p6majo.plot.old;
import java.awt.*;

//
//###################################################################
//                   Class ColorContourPlot
//###################################################################
//
//   
//  Creator               : Chris Anderson
//  (C) UCLA 1997
//
//###################################################################
//
public class ColorContourPlot2 extends Frame implements Runnable
{

    boolean RunningAsThread;
    boolean captureMode;

    public ColorContourCanvas2 contourCanvas;
    ContourResolutionDialog2   resDialog;

    String javaVersion = new String();

    java.util.Vector contourData;
    int currentFrame;

public ColorContourPlot2(int width, int height)
{
		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		setSize(getInsets().left + getInsets().right + width,getInsets().top + getInsets().bottom + height);
		BackButton = new Button("<<");
		BackButton.setBounds(getInsets().left + 40,getInsets().top + 288,50,29);
		add(BackButton);
		ForButton = new Button(">>");
		ForButton.setBounds(getInsets().left + 116,getInsets().top + 288,50,29);
		add(ForButton);
		FrameLabel = new Label("Frame",Label.CENTER);
		FrameLabel.setBounds(getInsets().left + 196,getInsets().top + 288,48,29);
		add(FrameLabel);
		FrameCountLabel = new Label("",Label.CENTER);
		FrameCountLabel.setBounds(getInsets().left + 260,getInsets().top + 288,56,29);
		add(FrameCountLabel);
		setTitle("Color Contour Plot");
		//}}

		//{{INIT_MENUS
		menuBar1 = new MenuBar();

		menu1 = new Menu("Options");
		menu1.add("Contour Resolution");
		menuBar1.add(menu1);
		setMenuBar(menuBar1);
		//$$ menuBar1.move(0,0);
		//}}


		show();

		captureMode     = false;
        javaVersion     = new String(getJavaVersion());

		Rectangle  R = getBounds();
		contourCanvas = new ColorContourCanvas2(this);
	    int yoffset = getInsets().top + getInsets().bottom;
        int xoffset = getInsets().left + getInsets().right;

        if(!captureMode)
        {
		contourCanvas.setBounds(getInsets().left + 5,getInsets().top + 5,
		R.width  - xoffset - 10,R.height - yoffset - 10);
		BackButton.setVisible(false);
		ForButton.setVisible(false);
		FrameLabel.setVisible(false);
		FrameCountLabel.setVisible(false);
		}
		else
		{
		contourCanvas.setBounds(getInsets().left + 5,getInsets().top + 5,
		R.width  - xoffset - 10,R.height - yoffset - 60);
		if(javaVersion.equals("1.1")){yoffset = 5;}
		BackButton.setBounds(getInsets().left + 40,R.height - yoffset - 35,50,29);
		ForButton.setBounds(getInsets().left + 116,R.height - yoffset - 35,50,29);
		FrameLabel.setBounds(getInsets().left + 196,R.height - yoffset - 35,48,29);
		FrameCountLabel.setBounds(getInsets().left + 260,R.height - yoffset - 35,56,29);
		}

		add(contourCanvas);

		resDialog = new ContourResolutionDialog2(this, this, false);
		resDialog.setVisible(false);

        contourData  = new java.util.Vector(10);
        currentFrame = 0;
		RunningAsThread = false;
	}

	//{{DECLARE_CONTROLS
	Button BackButton;
	Button ForButton;
	Label FrameLabel;
	Label FrameCountLabel;
	//}}

	//{{DECLARE_MENUS
	MenuBar menuBar1;
	Menu menu1;
	//}}
	
public synchronized void show()
{
    move(50, 50);
    super.show();
}

@Override
public void repaint()
{
    Rectangle  R = getBounds();
    int yoffset = getInsets().top + getInsets().bottom;
    int xoffset = getInsets().left + getInsets().right;
    
    
    if(!captureMode)
    {
	 if(contourCanvas != null)
	 {
        contourCanvas.setBounds(getInsets().left + 5,getInsets().top + 5,
        R.width  - xoffset - 10,R.height - yoffset - 10);
     }
    }
    else
    { 
     if(contourCanvas != null)
	 {
        contourCanvas.setBounds(getInsets().left + 5,getInsets().top + 5,
        R.width  - xoffset - 10,R.height - yoffset - 60);
		if(javaVersion.equals("1.1")){yoffset = 5;}
		BackButton.setBounds(getInsets().left + 40,R.height - yoffset - 35,50,29);
		ForButton.setBounds(getInsets().left + 116,R.height - yoffset - 35,50,29);
		FrameLabel.setBounds(getInsets().left + 196,R.height - yoffset - 35,48,29);
		FrameCountLabel.setBounds(getInsets().left + 260,R.height - yoffset - 35,56,29);
        
      }    
    }
    super.repaint();
}

public boolean handleEvent(Event event)
{
    if (event.id == Event.WINDOW_DESTROY)
    {
    if(!RunningAsThread)
    {
            setVisible(false);         // hide the Frame
            dispose();      // free the system resources
            System.exit(0); // close the application
            return true;
     }
    else
    {
        setVisible(false);
        return true;
    }
    }

		if (event.target == BackButton && event.id == Event.ACTION_EVENT) {
			BackButton_Clicked(event);
			return true;
		}
		if (event.target == ForButton && event.id == Event.ACTION_EVENT) {
			ForButton_Clicked(event);
			return true;
		}
		return super.handleEvent(event);
}
public boolean action(Event event, Object arg) 
{
    if (event.target instanceof MenuItem)
    {
	String label = (String) arg;
	  if (label.equalsIgnoreCase("Contour Resolution")) 
	  {
        ContourResolution_Action(event);
        return true;
      }
    }
    return super.action(event, arg);
	}


void ForButton_Clicked(Event event) 
{
    if(currentFrame < contourData.size()) 
    {
        currentFrame++;
        contourCanvas.setData((double[][])(contourData.elementAt(currentFrame-1)));
        FrameCountLabel.setText(new String(String.valueOf(currentFrame)));
    }
}

void BackButton_Clicked(Event event) 
{    
    if(currentFrame > 1) 
    {
        currentFrame--;
        contourCanvas.setData((double[][])(contourData.elementAt(currentFrame-1)));
        FrameCountLabel.setText(new String(String.valueOf(currentFrame)));
    }
}
	
void ContourResolution_Action(Event event)
{
		resDialog.setResolution(contourCanvas.getResolution());
		resDialog.show();
}

public void dialogDismissed(Dialog d)
{
    if(d == resDialog )
    {
        int panelCount =  resDialog.getResolution();
        contourCanvas.setResolution(panelCount);
        contourCanvas.repaint();
    }
}

public void setCaptureMode(boolean M)
{
    captureMode = M;
    contourData.removeAllElements();
    currentFrame = 0;
}

public void reset()
{
    contourData.removeAllElements();
    currentFrame = 0;  
}

public void setDataRange(double dMin, double dMax)
{
    contourCanvas.setDataRange(dMin, dMax);
}

public void setData(double [][] inputData)
{
    if(captureMode)
    {
     contourData.addElement(inputData);
     if(contourData.size() == 1)
     {
      contourCanvas.setData(inputData);
      FrameCountLabel.setText("1");
      currentFrame = 1;
     }
    }
    else
    {
    contourCanvas.setData(inputData);
    }
}

public void setData(double [] inputData, int xDataCount, int yDataCount)
{
    double [][] data = new double[xDataCount][yDataCount];
    int i; int j;
    for(i =0; i < xDataCount; i++)
    {
    for(j = 0; j < yDataCount; j++)
    {
        data[i][j] = inputData[j + i*yDataCount];
    }}
    setData(data);
}



public void run()
{
    RunningAsThread = true;
    show();
}

public String getJavaVersion()
{
    java.util.Properties P = System.getProperties();
    
    java.util.StringTokenizer Tok 
    = new java.util.StringTokenizer(P.getProperty("java.version", null),"."); 

    String version;
    String p1, p2;
    if(Tok.countTokens() > 1)
    {
       p1 = Tok.nextToken();
       p2 = Tok.nextToken();
       version = p1 + "." + p2;
    }
    else
    {
       version = "1.0";
    }
    return version;
}

}
