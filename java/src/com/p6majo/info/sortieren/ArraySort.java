package com.p6majo.info.sortieren;

public class ArraySort<C extends Comparable<C>> {
	private C[] array;
	private long exchanges;
	private long arrayaccesses;
	private long comparisons;
	private String methodNameString;
	
	public ArraySort(C[] array){
		this.array=array;
	}
	
	public void selectionSort(){
		this.methodNameString="Selection sort";
		int left = 0;
		int n = this.array.length;
		while (left<n){
			int min = left;
			for (int i=left+1;i<n;i++)if (this.array[i].compareTo( this.array[min])<0) min=i;
			this.exchange(min,left);
			left++;
		}
	}

	public void insertionSort(){
		this.methodNameString="Insertion sort";
		for (int i=1;i<this.array.length;i++){
			C tmp = this.array[i];
			int j=i;
			while ((j>0) && (this.array[j-1].compareTo(tmp)>0)){
				this.array[j]=this.array[j-1];
				j--;
			}
			this.array[j]=tmp;
		}
	}

	public void bubbleSort(){
		this.methodNameString="Bubble sort";
		int n = this.array.length;
		while (n>1){
			for (int i=0;i<n-1;i++){
				if (this.array[i].compareTo(this.array[i+1])>0) this.exchange(i, i+1);
			}
			n--;
		}
	}
	
	public void verboseSelectionSort(){
		this.methodNameString="Selection sort";
		long start=System.currentTimeMillis();
		this.exchanges=0;
		this.arrayaccesses=0;
		this.comparisons =0;
		int left = 0;
		int n = this.array.length;
		while (left<n){
			int min = left;
			for (int i=left+1;i<n;i++){
				if (this.array[i].compareTo( this.array[min])<0) min=i;
				this.arrayaccesses+=2;
				this.comparisons++;
			}
			if (min!=left) {
				this.exchange(min,left);
				this.exchanges++;
				this.arrayaccesses+=4;//for accesses during exchange
			}
			left++;
		}
		long time =System.currentTimeMillis()-start;
		
		System.out.println("****************************************************************************");
		System.out.println("Analysis of "+this.toString()+" for "+this.array.length+" elements!");
		System.out.println("Time: "+time);
		System.out.println("Comparisons: "+this.comparisons);
		System.out.println("Array accesses: "+this.arrayaccesses);
		System.out.println("Exchanges: "+this.exchanges);
		System.out.println("****************************************************************************");
	}

	public void verboseInsertionSort(){
		this.methodNameString="Insertion sort";
		long start=System.currentTimeMillis();
		this.exchanges=0;
		this.arrayaccesses=0;
		this.comparisons =0;
		for (int i=1;i<this.array.length;i++){
			C tmp = this.array[i];
			this.arrayaccesses++;
			int j=i;
			while ((j>0) && (this.array[j-1].compareTo(tmp)>0)){
				this.arrayaccesses+=3;
				this.comparisons++;
				this.array[j]=this.array[j-1];
				j--;
			}
			this.array[j]=tmp;
			this.arrayaccesses++;
		}
		long time = System.currentTimeMillis()-start;
		System.out.println("****************************************************************************");
		System.out.println("Analysis of "+this.toString()+" for "+this.array.length+" elements!");
		System.out.println("Time: "+time);
		System.out.println("Comparisons: "+this.comparisons);
		System.out.println("Array accesses: "+this.arrayaccesses);
		System.out.println("Exchanges: "+this.exchanges);
		System.out.println("****************************************************************************");

	}

	public void verboseBubbleSort(){
		this.methodNameString="Bubble sort";
		long start=System.currentTimeMillis();
		this.exchanges=0;
		this.arrayaccesses=0;
		this.comparisons =0;
		int n = this.array.length;
		while (n>1){
			for (int i=0;i<n-1;i++){
				if (this.array[i].compareTo(this.array[i+1])>0) this.exchange(i, i+1);
				this.exchanges++;
				this.comparisons++;
				this.arrayaccesses+=5;
			}
			n--;
		}
		long time = System.currentTimeMillis()-start;
		System.out.println("****************************************************************************");
		System.out.println("Analysis of "+this.toString()+" for "+this.array.length+" elements!");
		System.out.println("Time: "+time);
		System.out.println("Comparisons: "+this.comparisons);
		System.out.println("Array accesses: "+this.arrayaccesses);
		System.out.println("Exchanges: "+this.exchanges);
		System.out.println("****************************************************************************");
	}
	
	private void exchange(int i, int j){
		C tmp = this.array[i];
		this.array[i]=this.array[j];
		this.array[j]=tmp;
	}

	public String toString(){
		return methodNameString;
	}
	
}
