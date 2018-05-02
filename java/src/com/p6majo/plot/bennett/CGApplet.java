package com.p6majo.plot.bennett;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;


/**
 This applet provides an interface to a CompGraph Object which will
 graph a complex function using color to plot a 4th dimension.
 Last updated February 26, 2001
 (copyright 1997-2001)
 @version 1.0
 @author Andrew G. Bennett
 */
public class CGApplet extends Applet implements ItemListener {

    /**
     Initializations
     */
    public void init() {
        super.init();
        fzactive=true;
        helpactive=true;
        dpactive=true;

        // No Layout Manager
        setLayout(null);
        addNotify();
        resize(500,400);

        // Help Screen Panel
        helpPanel=new Panel(null);
        helpPanel.setBounds(70,25,360,310);
        helpPanel.setBackground(Color.lightGray);
        helpPanel.setVisible(false);
        add(helpPanel);
        helptextString=new String(
                "Graphing a complex function is difficult because you\n"
                        +"need 2 (real) dimensions for the domain and 2 (real)\n"
                        +"dimensions for the range - a total of 4 dimensions.\n"
                        +"In this applet, the domain of a complex function is\n"
                        +"graphed on the base plane. The range is graphed using\n"
                        +"polar coordinates. The modulus (magnitude) of the\n"
                        +"complex function is graphed on the vertical axis. The\n"
                        +"argument (angle) is graphed by using different colors:\n"
                        +"light blue for positive real, dark blue (shading to\n"
                        +"purple) for positive imaginary, red for negative real,\n"
                        +"and yellow-green for negative imaginary. This allows\n"
                        +"four dimensions to be represented in three spatial\n"
                        +"dimensions, which are then projected onto a two\n"
                        +"dimensional screen using a simple orthogonal\n"
                        +"projection.\n\n"
                        +"Enter complex functions into the f(z) = text box using\n"
                        +"standard calculator notation. You must use * for\n"
                        +"multiplication, i.e. 2*z not 2z. Functions must have\n"
                        +"parentheses, i.e. sin(z) not sin z. The parser\n"
                        +"understands the following operations, functions, and\n"
                        +"constants: +, -, *, /, ^, sqrt(), sin(), cos(), tan(),\n"
                        +"exp(),  ln(), log() (synonymous with ln), sinh(), cosh(),\n"
                        +"abs(), mod() (synonymous with abs), arg(), conj(),\n"
                        +"e, pi, i.\n\n"
                        +"To adjust the domain of the graph, right-click (or\n"
                        +"shift-click if you have a one-button mouse) on the\n"
                        +"graph. The domain editor only accepts numerical input,\n"
                        +"i.e. 3.14 not pi.");
        helpTextArea=new TextArea(helptextString,20,55,TextArea.SCROLLBARS_VERTICAL_ONLY);
        helpTextArea.setBounds(10,10,340,270);
        helpTextArea.setEditable(false);
        helpPanel.add(helpTextArea);
        helpcloseButton=new Button("Close");
        helpcloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                closeHelp();
            }
        });
        helpcloseButton.setBounds(145,285,70,20);
        helpPanel.add(helpcloseButton);

        // Edit Domain Panel
        dp=new Panel(null);
        dp.setBounds(125,85,250,190);
        dp.setBackground(Color.lightGray);
        dp.setVisible(false);
        add(dp);
        // Re(z) Values
        rLabel=new Label("Re(z):");
        rLabel.setBounds(5,10,40,20);
        dp.add(rLabel);
        rminLabel=new Label("min ",Label.RIGHT);
        rminLabel.setBounds(45,10,30,20);
        dp.add(rminLabel);
        rminTextField=new TextField("-2.0");
        rminTextField.setBounds(75,10,70,20);
        dp.add(rminTextField);
        rmaxLabel=new Label("max ",Label.RIGHT);
        rmaxLabel.setBounds(145,10,30,20);
        dp.add(rmaxLabel);
        rmaxTextField=new TextField("2.0");
        rmaxTextField.setBounds(175,10,70,20);
        dp.add(rmaxTextField);
        // Im(z) Values
        iLabel=new Label("Im(z):");
        iLabel.setBounds(5,50,40,20);
        dp.add(iLabel);
        iminLabel=new Label("min ",Label.RIGHT);
        iminLabel.setBounds(45,50,30,20);
        dp.add(iminLabel);
        iminTextField=new TextField("-2.0");
        iminTextField.setBounds(75,50,70,20);
        dp.add(iminTextField);
        imaxLabel=new Label("max ",Label.RIGHT);
        imaxLabel.setBounds(145,50,30,20);
        dp.add(imaxLabel);
        imaxTextField=new TextField("2.0");
        imaxTextField.setBounds(175,50,70,20);
        dp.add(imaxTextField);
        // |f(z)| Values
        fLabel=new Label("|f(z)|:");
        fLabel.setBounds(5,90,40,20);
        dp.add(fLabel);
        fCGroup=new CheckboxGroup();
        fautoCheckbox=new Checkbox("auto",true,fCGroup);
        fautoCheckbox.setBounds(65,90,80,20);
        fautoCheckbox.addItemListener(this);
        dp.add(fautoCheckbox);
        fmanCheckbox=new Checkbox("manual",false,fCGroup);
        fmanCheckbox.setBounds(65,120,80,20);
        fmanCheckbox.addItemListener(this);
        dp.add(fmanCheckbox);
        fminLabel=new Label("min ",Label.RIGHT);
        fminLabel.setForeground(Color.gray);
        fminLabel.setBounds(145,90,30,20);
        dp.add(fminLabel);
        fminTextField=new TextField("0.0");
        fminTextField.setForeground(Color.gray);
        fminTextField.setBounds(175,90,70,20);
        fminTextField.setEditable(false);
        // Prevent it gaining focus if fauto checked
        fminTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (fautoCheckbox.getState()) {
                    fminLabel.requestFocus();
                }
            }
        });
        dp.add(fminTextField);
        fmaxLabel=new Label("max ",Label.RIGHT);
        fmaxLabel.setForeground(Color.gray);
        fmaxLabel.setBounds(145,120,30,20);
        dp.add(fmaxLabel);
        fmaxTextField=new TextField("3.0");
        fmaxTextField.setForeground(Color.gray);
        fmaxTextField.setBounds(175,120,70,20);
        fmaxTextField.setEditable(false);
        // Prevent it gaining focus if fauto checked
        fmaxTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (fautoCheckbox.getState()) {
                    fmaxLabel.requestFocus();
                }
            }
        });
        dp.add(fmaxTextField);
        // Domain Buttons
        okButton=new Button("OK");
        okButton.setBounds(35,150,60,20);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                okdp();
            }
        });
        dp.add(okButton);
        resetButton=new Button("Defaults");
        resetButton.setBounds(95,150,60,20);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                resetdp();
            }
        });
        dp.add(resetButton);
        cancelButton=new Button("Cancel");
        cancelButton.setBounds(155,150,60,20);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                canceldp();
            }
        });
        dp.add(cancelButton);
        // Message Label (for errors)
        messLabel=new Label("",Label.CENTER);
        messLabel.setForeground(Color.red);
        messLabel.setBounds(5,170,240,20);
        dp.add(messLabel);

        // CompGraph to plot graph
        c3d=new CompGraph();
        c3d.setBackground(Color.black);
        c3d.setBounds(45,5,410,350);
        c3d.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if ((evt.getModifiers()&InputEvent.BUTTON3_MASK)>0
                        || evt.isShiftDown()) {
                    showdp();
                }
            }
        });
        add(c3d);
        // Label "f(z) ="
        fzLabel=new Label("f(z)  =",Label.CENTER);
        fzLabel.setBounds(10,360,50,20);
        add(fzLabel);
        // TextField to input f(z)
        fzTextField=new TextField("z");
        fzTextField.setBounds(60,360,265,20);
        fzTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!fzactive) return;
                emLabel.setText("");
                try {
                    c3d.setfct(fzTextField.getText());
                    c3d.repaint();
                } catch (Exception except) {
                    emLabel.setText("Unable to evaluate f(z)");
                }
            }
        });
        add(fzTextField);
        // Change view to "top" button
        tvButton=new Button("Top View");
        tvButton.setBounds(340,360,70,20);
        tvButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!fzactive) return;
                c3d.setView("top");
                emLabel.setText("");
                try {
                    c3d.setfct(fzTextField.getText());
                    c3d.repaint();
                } catch (Exception except) {
                    emLabel.setText("Unable to evaluate f(z)");
                }
            }
        });
        add(tvButton);
        // Change view to "side" button
        svButton=new Button("Side View");
        svButton.setBounds(410,360,70,20);
        svButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!fzactive) return;
                c3d.setView("side");
                emLabel.setText("");
                try {
                    c3d.setfct(fzTextField.getText());
                    c3d.repaint();
                } catch (Exception except) {
                    emLabel.setText("Unable to evaluate f(z)");
                }
            }
        });
        add(svButton);
        helpButton=new Button("Help");
        helpButton.setBounds(410,380,70,20);
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!helpactive) return;
                helpPanel.setVisible(true);
                fzTextField.setEditable(false);
                fzactive=false;
                dpactive=false;
            }
        });
        add(helpButton);

        // Error Message Label
        emLabel=new Label("",Label.LEFT);
        emLabel.setForeground(Color.red);
        emLabel.setBounds(60,380,265,20);
        add(emLabel);
    }

    /**
     Activate and Gray |f(z)| min & max
     @param evt Change in auto/manual |f(z)| limit checkboxes
     */
    public void itemStateChanged(ItemEvent evt) {
        if (fautoCheckbox.getState()) { // Gray |f(z)| min & max
            fminLabel.setForeground(Color.gray);
            fminTextField.setForeground(Color.gray);
            fminTextField.setEditable(false);
            fmaxLabel.setForeground(Color.gray);
            fmaxTextField.setForeground(Color.gray);
            fmaxTextField.setEditable(false);
        } else {    // Activate |f(z)| min & max
            fminLabel.setForeground(Color.black);
            fminTextField.setForeground(Color.black);
            fminTextField.setEditable(true);
            fmaxLabel.setForeground(Color.black);
            fmaxTextField.setForeground(Color.black);
            fmaxTextField.setEditable(true);
        }
    }

    // Show Domain Panel
    private void showdp() {
        if (!dpactive) return;
        rminTextField.setText(String.valueOf(c3d.getxmin()));
        rmaxTextField.setText(String.valueOf(c3d.getxmax()));
        iminTextField.setText(String.valueOf(c3d.getymin()));
        imaxTextField.setText(String.valueOf(c3d.getymax()));
        fminTextField.setText(String.valueOf(c3d.getzmin()));
        fmaxTextField.setText(String.valueOf(c3d.getzmax()));
        if (c3d.getzauto()) {
            fautoCheckbox.setState(true);
            fminLabel.setForeground(Color.gray);
            fminTextField.setForeground(Color.gray);
            fminTextField.setEditable(false);
            fmaxLabel.setForeground(Color.gray);
            fmaxTextField.setForeground(Color.gray);
            fmaxTextField.setEditable(false);
        } else {
            fmanCheckbox.setState(true);
            fminLabel.setForeground(Color.black);
            fminTextField.setForeground(Color.black);
            fminTextField.setEditable(true);
            fmaxLabel.setForeground(Color.black);
            fmaxTextField.setForeground(Color.black);
            fmaxTextField.setEditable(true);
        }
        messLabel.setText("");
        dp.setVisible(true);
        fzactive=false;
        helpactive=false;
        fzTextField.setEditable(false);
    }

    // Domain Panel Okayed
    private void okdp() {
        double rmin,rmax,imin,imax,fmin,fmax;
        // Validate Input
        try {
            rmin=Double.valueOf(rminTextField.getText()).doubleValue();
        } catch (NumberFormatException except) {
            messLabel.setText("Can't Parse Re(z) min");
            rminTextField.requestFocus();
            return;
        }
        try {
            rmax=Double.valueOf(rmaxTextField.getText()).doubleValue();
        } catch (NumberFormatException except) {
            messLabel.setText("Can't Parse Re(z) max");
            rmaxTextField.requestFocus();
            return;
        }
        try {
            imin=Double.valueOf(iminTextField.getText()).doubleValue();
        } catch (NumberFormatException except) {
            messLabel.setText("Can't Parse Im(z) min");
            iminTextField.requestFocus();
            return;
        }
        try {
            imax=Double.valueOf(imaxTextField.getText()).doubleValue();
        } catch (NumberFormatException except) {
            messLabel.setText("Can't Parse Im(z) max");
            imaxTextField.requestFocus();
            return;
        }
        fmin=0;fmax=3;
        if (fmanCheckbox.getState()) {
            try {
                fmin=Double.valueOf(fminTextField.getText()).doubleValue();
            } catch (NumberFormatException except) {
                messLabel.setText("Can't Parse |f(z)| min");
                fminTextField.requestFocus();
                return;
            }
            try {
                fmax=Double.valueOf(fmaxTextField.getText()).doubleValue();
            } catch (NumberFormatException except) {
                messLabel.setText("Can't Parse |f(z)| max");
                fmaxTextField.requestFocus();
                return;
            }
        }
        if (rmin>=rmax) {
            messLabel.setText("Re(z) min must be less than max");
            rminTextField.requestFocus();
            return;
        }
        if (imin>=imax) {
            messLabel.setText("Im(z) min must be less than max");
            iminTextField.requestFocus();
            return;
        }
        if (fmanCheckbox.getState() && (fmin>=fmax)) {
            messLabel.setText("|f(z)| min must be less than max");
            fminTextField.requestFocus();
            return;
        }
        // Valid input so update domain and graph
        c3d.setLimits(rmin,rmax,imin,imax);
        if (fautoCheckbox.getState()) {
            c3d.setzauto(true);
        } else {
            c3d.setzauto(false);
            c3d.setzLimits(fmin,fmax);
        }
        c3d.setfct(fzTextField.getText());
        dp.setVisible(false);
        c3d.repaint();
        fzTextField.setEditable(true);
        fzactive=true;
        helpactive=true;
    }

    // Domain Panel reset
    private void resetdp() {
        rminTextField.setText("-2.0");
        rmaxTextField.setText("2.0");
        iminTextField.setText("-2.0");
        imaxTextField.setText("2.0");
        fautoCheckbox.setState(true);
        fminLabel.setForeground(Color.gray);
        fminTextField.setForeground(Color.gray);
        fmaxLabel.setForeground(Color.gray);
        fmaxTextField.setForeground(Color.gray);
        fzactive=true;
        helpactive=true;
        fzTextField.setEditable(true);
    }

    // Domain Panel Cancelled
    private void canceldp() {
        dp.setVisible(false);
        fzactive=true;
        helpactive=true;
        fzTextField.setEditable(true);
    }

    // Close Help Panel
    private void closeHelp() {
        helpPanel.setVisible(false);
        fzactive=true;
        dpactive=true;
        fzTextField.setEditable(true);
        c3d.repaint();
    }

    //DECLARE CONTROLS
    CompGraph c3d;
    Panel dp,helpPanel;
    GridBagConstraints constraints;
    Label fzLabel,emLabel,rLabel,rminLabel,rmaxLabel,
            iLabel,iminLabel,imaxLabel,
            fLabel,fminLabel,fmaxLabel,messLabel;
    TextField fzTextField,rminTextField,rmaxTextField,
            iminTextField,imaxTextField,fminTextField,fmaxTextField;
    TextArea helpTextArea;
    String helptextString;
    Checkbox fautoCheckbox,fmanCheckbox;
    CheckboxGroup fCGroup;
    Button tvButton,svButton,helpcloseButton,helpButton,
            okButton,resetButton,cancelButton;
    boolean fzactive,dpactive,helpactive;
}
