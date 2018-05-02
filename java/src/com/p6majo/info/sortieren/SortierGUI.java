
package com.p6majo.info.sortieren;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 24.01.2010
  * @version 2.0 vom 4.2.2012
  * @author
  */

public class SortierGUI extends JFrame {
  // Anfang Attribute
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JTextArea jTextArea1 = new JTextArea("");
  private JLabel Arraygroesse = new JLabel();
  private JButton Zufallsbelegung = new JButton();
  private JTextField SortBeispiel = new JTextField();
  
  
  private int[] SortArr;
  private int[] SortArrSich;
  private Sortierverfahren SortVerfahren = new Sortierverfahren(this);
  
  private JButton SelectionSortBt = new JButton();
  private JButton ClearBt = new JButton();
  private JButton InsertionSortBt = new JButton();
  private JButton BubbleSortBt = new JButton();
  private JButton MergeSortBt = new JButton();
  private JButton QuickSortBt = new JButton();
  private JButton HeapSortBt = new JButton();
  private JButton FSbt = new JButton();
  private JButton USbt = new JButton();
  private JSpinner jSpinner1 = new JSpinner();
  
  
  private JButton jButton1 = new JButton();
  // Ende Attribute
  
  public SortierGUI(String title) {
    // Frame-Initialisierung
    super(title);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    int frameWidth = 754; 
    int frameHeight = 470;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    Container cp = getContentPane();
    cp.setLayout(null);
    cp.setBackground(Color.ORANGE);
    // Anfang Komponenten
    
    jScrollPane1.setBounds(272, 24, 473, 417);
    cp.add(jScrollPane1);
    jTextArea1.setText("");
    jTextArea1.setBounds(-2, -2, 449, 409);
    jScrollPane1.setViewportView(jTextArea1);
    jTextArea1.setEditable(false);
    Arraygroesse.setBounds(16, 32, 90, 20);
    Arraygroesse.setText("Arraygr��e");
    Arraygroesse.setFont(new Font("MS Sans Serif", Font.BOLD, 15));
    cp.add(Arraygroesse);
    Zufallsbelegung.setBounds(16, 72, 169, 25);
    Zufallsbelegung.setText("Zufallsbelegung");
    Zufallsbelegung.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Zufallsbelegung_ActionPerformed(evt);
      }
    });
    Zufallsbelegung.setFont(new Font("Dialog", Font.BOLD, 13));
    cp.add(Zufallsbelegung);
    SortBeispiel.setBounds(16, 104, 249, 24);
    SortBeispiel.setText("SortBeispiel");
    SortBeispiel.setEditable(false);
    SortBeispiel.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    cp.add(SortBeispiel);
    SelectionSortBt.setBounds(16, 160, 169, 25);
    SelectionSortBt.setText("SelectionSort");
    SelectionSortBt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        SelectionSortBt_ActionPerformed(evt);
      }
    });
    SelectionSortBt.setFont(new Font("Dialog", Font.BOLD, 13));
    cp.add(SelectionSortBt);
    ClearBt.setBounds(616, 0, 65, 17);
    ClearBt.setText("clear");
    ClearBt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ClearBt_ActionPerformed(evt);
      }
    });
    ClearBt.setFont(new Font("Dialog", Font.BOLD, 11));
    cp.add(ClearBt);
    InsertionSortBt.setBounds(16, 224, 169, 25);
    InsertionSortBt.setText("InsertionSort");
    InsertionSortBt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        InsertionSortBt_ActionPerformed(evt);
      }
    });
    InsertionSortBt.setFont(new Font("Dialog", Font.BOLD, 13));
    cp.add(InsertionSortBt);
    BubbleSortBt.setBounds(16, 192, 169, 25);
    BubbleSortBt.setText("BubbleSort");
    BubbleSortBt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        BubbleSortBt_ActionPerformed(evt);
      }
    });
    BubbleSortBt.setFont(new Font("Dialog", Font.BOLD, 13));
    cp.add(BubbleSortBt);
    MergeSortBt.setBounds(16, 288, 169, 25);
    MergeSortBt.setText("MergeSort");
    MergeSortBt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MergeSortBt_ActionPerformed(evt);
      }
    });
    MergeSortBt.setFont(new Font("Dialog", Font.BOLD, 13));
    cp.add(MergeSortBt);
    QuickSortBt.setBounds(16, 320, 169, 25);
    QuickSortBt.setText("QuickSort");
    QuickSortBt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        QuickSortBt_ActionPerformed(evt);
      }
    });
    QuickSortBt.setFont(new Font("Dialog", Font.BOLD, 13));
    cp.add(QuickSortBt);
    FSbt.setBounds(192, 72, 25, 25);
    FSbt.setText("FS");
    FSbt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        FSbt_ActionPerformed(evt);
      }
    });
    FSbt.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
    cp.add(FSbt);
    USbt.setBounds(224, 72, 25, 25);
    USbt.setText("US");
    USbt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        USbt_ActionPerformed(evt);
      }
    });
    USbt.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
    cp.add(USbt);
    jSpinner1.setBounds(120, 32, 30, 24);
    
    jSpinner1.setModel(new SpinnerNumberModel(6, 4, 20, 1));
    cp.add(jSpinner1);
    USbt.setBounds(232, 72, 33, 25);
    FSbt.setBounds(192, 72, 33, 25);
    
    
    
    HeapSortBt.setBounds(16, 352, 169, 25);
    HeapSortBt.setText("HeapSort");
    HeapSortBt.setMargin(new Insets(2, 2, 2, 2));
    HeapSortBt.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent evt) { 
        HeapSortBt_ActionPerformed(evt);
      }
    });
    HeapSortBt.setFont(new Font("Arial", Font.ITALIC, 12));
    cp.add(HeapSortBt);
    jButton1.setBounds(192, 320, 25, 25);
    jButton1.setText("X");
    jButton1.setMargin(new Insets(2, 2, 2, 2));
    jButton1.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent evt) { 
        jButton1_ActionPerformed(evt);
      }
    });
    cp.add(jButton1);
    // Ende Komponenten
    //
    new_sortarray ();
    
    //
    setResizable(false);
    setVisible(true);
  }
  
  // Anfang Methoden
  public void Zufallsbelegung_ActionPerformed(ActionEvent evt) {
    new_sortarray();
  }
  
  public void FSbt_ActionPerformed(ActionEvent evt) {
    int groesse = (Integer) jSpinner1.getValue();
    SortArr = new int[groesse];
    for (int i = 0; i <  groesse; i++)
    SortArr[i] =  10 + i*9;
    int j = (int) ((Math.random() * groesse) -1);
    SortArr[j] = (int) ((Math.random() * 90) + 10);
    String str = "";
    for (int i = 0; i <  groesse; i++)
    str += ""+ SortArr[i] + " ";
    SortBeispiel.setText(str);
  }
  
  public void USbt_ActionPerformed(ActionEvent evt) {
    int groesse = (Integer) jSpinner1.getValue();
    SortArr = new int[groesse];
    for (int i = 0; i <  groesse; i++)
    SortArr[i] =  99 - i*9;
    int j = (int) ((Math.random() * groesse) -1);
    SortArr[j] = (int) ((Math.random() * 90) + 10);
    String str = "";
    for (int i = 0; i <  groesse; i++)
    str += ""+ SortArr[i] + " ";
    SortBeispiel.setText(str);
  }
  
  public void ClearBt_ActionPerformed(ActionEvent evt) {
    jTextArea1.setText("");
  }
  
  public void SelectionSortBt_ActionPerformed(ActionEvent evt) {
    copy_sortarray();
    jTextArea1.append("--------------------------------------Selection Sort\n");
    SortVerfahren.SelectionSort(SortArr);  
    SortArr = SortArrSich;
  }
  
  
  
  
  public void InsertionSortBt_ActionPerformed(ActionEvent evt) {
    copy_sortarray();
    jTextArea1.append("--------------------------------------Insertion Sort\n");
    SortVerfahren.InsertionSort(SortArr);
    SortArr = SortArrSich;
  }
  
  public void BubbleSortBt_ActionPerformed(ActionEvent evt) {
    copy_sortarray();
    jTextArea1.append("--------------------------------------Bubble Sort\n");
    SortVerfahren.BubbleSort(SortArr);
    SortArr = SortArrSich;
  }
  
  public void MergeSortBt_ActionPerformed(ActionEvent evt) {
    copy_sortarray();
    jTextArea1.append("--------------------------------------Merge Sort\n");
    SortVerfahren.MergeSort(SortArr);
    SortArr = SortArrSich;
  }
  
  public void QuickSortBt_ActionPerformed(ActionEvent evt) {
    copy_sortarray();
    jTextArea1.append("--------------------------------------Quick Sort\n");
  
    SortVerfahren.QuickSort(SortArr);
    SortArr = SortArrSich;
  }
  
  
  
  public void HeapSortBt_ActionPerformed(ActionEvent evt) {
    copy_sortarray();
    jTextArea1.append("--------------------------------------Heap Sort\n");
    SortVerfahren.HeapSort(SortArr);
    SortArr = SortArrSich;
  }
  
  public void jButton1_ActionPerformed(ActionEvent evt) {
    copy_sortarray();
    int[] a = { 2, 3, 3, 1, 1, 1, 1 };
    SortArr = a;
    jTextArea1.append("--------------------------------------Quick Sort\n");
    SortVerfahren.QuickSort(a);
    SortArr = SortArrSich;
    
  }
  
  // Ende Methoden
  
  private void new_sortarray ()   {
    int groesse = (Integer) jSpinner1.getValue();
    SortArr = new int[groesse];
    for (int i = 0; i <  groesse; i++)
    SortArr[i] = (int) ((Math.random() * 90) + 10);     // 10 ... 100
    String str = "";
    for (int i = 0; i <  groesse; i++)
    str += ""+ SortArr[i] + " ";
    SortBeispiel.setText(str);
  }
  
  private void copy_sortarray() {
    SortArrSich = new int[SortArr.length];
    for (int i = 0; i <  SortArr.length; i++)
    SortArrSich[i] = SortArr[i];
  }
  
  public void ausdrucken(int lauf1, int lauf2) {
    String str = "";
    for (int i = 0; i < SortArr.length; i++)
    str += SortArr[i]+" ";
    str += "         ("+lauf1 + ","+lauf2+")\n";
    jTextArea1.append(str);
  }
  
  public void textDrucken(String str) {
    jTextArea1.append(str+"\n");
  }
  
  public void MergeSortausdrucken(int links, int rechts) {
    String str = "";
    for (int i = links; i <= rechts; i++)
    str += SortArr[i]+" ";
    str += "\t==> Links: "+ links+ "  Rechts:"+ rechts +"\n";
    jTextArea1.append(str);
  }
  
  
  // MAIN --------------------------------------------------------------
  public static void main(String[] args) {
    new SortierGUI("SortierGUI");
  }
}
