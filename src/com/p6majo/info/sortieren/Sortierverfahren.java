package com.p6majo.info.sortieren;
/**
 * Sortierverfahren
 *    insertion und selection sort
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Sortierverfahren
{
  // Instanzvariablen 
  private SortierGUI gui;  // Sortier-Array
  
  //  private boolean kein_neues_sortarray = false; // Nicht der beste Programmierstil
  
  
  // Konstruktor
  public Sortierverfahren (SortierGUI g) {
    gui = g;
  }
  
  
  /**
  * Selection Sort
  *
  */
  public void SelectionSort(int[] a)
  {gui.ausdrucken(0,0);
    int min = 0;
    int hilfsvar;
    for (int i=0; i<a.length; i++)        // Gehe n-mal (n = L?nge des Arrays) durch das Array
    { min = i;
      for (int j=(i+1); j<a.length; j++) // Suche im unsortierten Bereich (i+1 bis n-1)
      { if (a[j]<a[min]) min = j;       }   // nach dem kleinsten Element
      if (i != min)  {                 // Ist das kleinste Element nicht das erste Element im unsortierten Bereich
        swap(i, min, a);              // dann tausche die Position der Elemente.
      }
    }
    gui.ausdrucken(0,0);
  }
  
  
  /**
  * Bubble Sort
  */
  public void BubbleSort(int[] a)
  {gui.ausdrucken(0,0);
    int hilfsvar;
    for (int i=0; i<a.length; i++)           // Gehe n-mal (n = L?nge des Arrays) durch das Array
    { for (int k=(a.length-1); k > i; k--)   // Laufe von rechts nach links und lasse die
      { if (a[k]<a[k-1])                 // kleineren Elemente (Zahlen) schrittweise aufsteigen.
        {
          hilfsvar = a[k];
          a[k] = a[k-1];
          a[k-1] = hilfsvar;
          gui.ausdrucken(k-1,k);
        }
      } // for k
    } // for i
    gui.ausdrucken(0,0);
  }
  
  /**
  * Insertion Sort
  */
  public void InsertionSort(int[] a)
  {gui.ausdrucken(0,0);
    // HIER MUSS DIE METHODE EINGEF?GT WERDEN...
    
    
    // BEIM TAUSCHVORGANG:
    // gui.ausdrucken(...,...);
    // einf?gen.
  }
  
  
  
  /**
  * MergeSort
  */
  public void MergeSort(int[] a)
  {gui.ausdrucken(0,0);
    
    MergeSortRek(0, a.length-1, a);
    
    gui.ausdrucken(0,0);
    
  }
  public void MergeSortRek(int lR, int rR, int[] a)
  {gui.ausdrucken(0,0);
    
    // HIER MUSS DIE METHODE EINGEF?GT WERDEN...
    
    
    
  } 
  
  
  // ********************************************   QUICKSORT
  
  public void QuickSort(int[] a) {
    gui.ausdrucken(0,0);
    quicksortREK(a,  0, a.length-1);
    gui.ausdrucken(0,0);
  }
  
  public void quicksortREK (int[] a, int links, int rechts) {
    gui.textDrucken("QuickSort-Aufruf: "+links+" "+ rechts);
    int nachlinks = rechts;                     // Laufindex, der vom rechten Ende nach links l?uft
    int nachrechts = links;                     // Laufindex, der vom linken Ende nach rechts l?uft
    if (nachrechts < nachlinks) {           // Pivotelement bestimmen
      int pivot = a[ ( nachrechts + nachlinks )/2 ];
      while (nachrechts <= nachlinks) {
        // Links erstes Element suchen, das gr??er oder gleich dem Pivotelement ist
        while ((nachrechts < rechts) && (a[nachrechts] < pivot))
        nachrechts++;
        // Rechts erstes Element suchen, das kleiner oder gleich dem Pivotelement ist
        while ((nachlinks > links) && (a[nachlinks] > pivot))
        nachlinks--;
        // Wenn nicht aneinander vorbei gelaufen, Inhalte vertauschen
        if (nachrechts <= nachlinks)
        { swap(nachrechts, nachlinks, a);   // vertausche Zelleninhalte
          nachrechts++;
          nachlinks--;
        }
      } // end while
      // Linken Teil sortieren
      if (nachlinks > links)
      quicksortREK (a, links, nachlinks);
      // Rechten Teil sortieren
      if (nachrechts < rechts)
      quicksortREK (a, nachrechts, rechts);
    } // end if
  } // quicksort
  
  
  
  // ************************************************ HEAPSORT
  
  public void HeapSort(int[] a)
  {
    gui.ausdrucken(0,0);
    int end=a.length;
    
    int border=0;
    if((a.length%2)==1)
    border=(a.length-1)/2;
    else
    border=a.length/2;
    for(int i=border-1;i>=0;i--)  seep(i,a, a.length);
    gui.ausdrucken(0,0);
    
    for(int i=1;i<a.length;i++)
    {
      swap(0,end-1,a);
      seep(0,a,end-1);
      end--;
    }
  }
  
  
  public void seep(int index, int a[], int end)
  {
    
    int border=0;
    if((end%2)==1)
    border=(end-1)/2;
    else border=end/2;
    if(index<=border && end>1) {
      int pointto=0;
      pointto=2*(index+1);
      if(pointto<end)
      {
        if( !((a[index]>a[pointto-1]) && (a[index]>a[pointto])) )
        {
          if(a[pointto]>a[pointto-1])
          { swap(index,pointto,a);
            if(pointto<=(border-1))
          seep(pointto,a,end); }
          else
          { swap(index,pointto-1,a);
            if((pointto-1)<=(border-1))
          seep(pointto-1,a,end); } } }
      else
      {
        if(a[index]<a[pointto-1])
      swap(index,pointto-1,a); } }
  }
  
  public  void swap(int indexA,int indexB,int a[])
  { if (indexA != indexB)
    {
      int temp=a[indexA];
      a[indexA]=a[indexB];
      a[indexB]=temp;
      gui.ausdrucken(indexA,indexB);
    }
  }
  
  
}