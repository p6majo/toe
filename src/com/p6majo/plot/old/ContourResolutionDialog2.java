package com.p6majo.plot.old;

import java.awt.*;

public class ContourResolutionDialog2 extends Dialog {


	int xPanelCount;
	int yPanelCount;

	ColorContourPlot2 Client;

	void resolutionSlider_ScrollAbsolute(Event event) {


		//{{CONNECTION
		// Set the text for TextField...
		panelCountField.setText(String.valueOf(resolutionSlider.getValue()));
		//}}
	}

	void OKbutton_Clicked(Event event)
	{
        Client.dialogDismissed(this);
		hide();
	}

	void ApplyButton_Clicked(Event event)
	{
        Client.dialogDismissed(this);
	}


	public ContourResolutionDialog2(Frame parent, boolean modal) {

	    super(parent, modal);

		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(getInsets().left + getInsets().right + 172,getInsets().top + getInsets().bottom + 271);
		OKbutton = new Button("OK");
		OKbutton.setBounds(getInsets().left + 108,getInsets().top + 132,48,29);
		add(OKbutton);
		ApplyButton = new Button("Apply");
		ApplyButton.setBounds(getInsets().left + 108,getInsets().top + 84,48,29);
		add(ApplyButton);
		panelCountField = new TextField();
		panelCountField.setEditable(false);
		panelCountField.setBounds(getInsets().left + 28,getInsets().top + 192,52,35);
		add(panelCountField);
		label2 = new Label("# of panels");
		label2.setBounds(getInsets().left + 12,getInsets().top + 228,92,20);
		add(label2);
		resolutionSlider = new Scrollbar(Scrollbar.VERTICAL,0,0,5,1200);
		resolutionSlider.setBlockIncrement(100);
		resolutionSlider.setBounds(getInsets().left + 48,getInsets().top + 16,20,160);
		add(resolutionSlider);
		setTitle("Contour Resolution");
		//}}
		Client = null;
	}

    public ContourResolutionDialog2(Frame parent, ColorContourPlot2 client, boolean modal)
	{
	    this(parent, modal);
	    Client = client;
	}

    public synchronized void show() {
    	Rectangle bounds = getParent().getBounds();
    	Rectangle abounds = getBounds();

    	move(bounds.x + (bounds.width - abounds.width)/ 2,
    	     bounds.y + (bounds.height - abounds.height)/2);

    	super.show();
    }

	public boolean handleEvent(Event event) {
	    if(event.id == Event.WINDOW_DESTROY) {
	        this.setVisible(false);
	        return true;
	    }
		if (event.target == OKbutton && event.id == Event.ACTION_EVENT) {
			OKbutton_Clicked(event);
			return true;
		}
		if (event.target == ApplyButton && event.id == Event.ACTION_EVENT) {
			ApplyButton_Clicked(event);
			return true;
		}
		if (event.target == resolutionSlider) {
			resolutionSlider_ScrollAbsolute(event);
			return true;
		}
		return super.handleEvent(event);
	}

	//{{DECLARE_CONTROLS
	Button OKbutton;
	Button ApplyButton;
	TextField panelCountField;
	Label label2;
	Scrollbar resolutionSlider;
	//}}
	
	
	
	public void setResolution(int panelCount)
	{
	    xPanelCount = panelCount;
	    yPanelCount = panelCount;
	    panelCountField.setText(String.valueOf(panelCount));
	    resolutionSlider.setValue(panelCount);
	}
	
	public void captureResolution()
	{
	   xPanelCount = (new Integer(panelCountField.getText())).intValue();
	   yPanelCount = (new Integer(panelCountField.getText())).intValue();  
	}
	
    public int  getResolution()
	{
	   captureResolution();
	   return xPanelCount;    
	}
	

}
