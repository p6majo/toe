package com.p6majo.mnist;

import static java.lang.Math.min;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.p6majo.info.aachen.Kon;
import com.p6majo.mnist.MNISTDataProvider;
import com.princeton.ExtendedDraw;
import org.junit.Test;


public class TestMnistReader {


    private static int[] trainLabels ;
    private static List<int[][]> trainImages;
    private static ExtendedDraw draw;


    @Test
	public void test() {
		String LABEL_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/t10k-labels-idx1-ubyte";
		String IMAGE_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/t10k-images-idx3-ubyte";

		int[] testLabels = MNISTDataProvider.getLabels(LABEL_FILE);
		List<int[][]> testImages = MNISTDataProvider.getImages(IMAGE_FILE);
		
		assertEquals(testLabels.length, testImages.size());
		assertEquals(28, testImages.get(0).length);
		assertEquals(28, testImages.get(0)[0].length);

		for (int i = 0; i < min(10, testLabels.length); i++) {
			printf("================= LABEL %d\n", testLabels[i]);
			printf("%s", MNISTDataProvider.renderImage(testImages.get(i)));
		}


		printf("There are %d images in the test data set.\n\n",testImages.size());

        LABEL_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/train-labels-idx1-ubyte";
        IMAGE_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/train-images-idx3-ubyte";

        long start = System.currentTimeMillis();

        trainLabels = MNISTDataProvider.getLabels(LABEL_FILE);
        trainImages = MNISTDataProvider.getImages(IMAGE_FILE);

        printf("Parsing of the training data took %d ms.\n",(System.currentTimeMillis()-start));

        assertEquals(trainLabels.length, trainImages.size());
        assertEquals(28, trainImages.get(0).length);
        assertEquals(28, trainImages.get(0)[0].length);

        printf("There are %d images in the training data set.\n",trainImages.size());

		draw = new ExtendedDraw("MNIST Database visualizer");
		draw.setCanvasSize(1400,56);
		draw.setXscale(0.,1399.);
		draw.setYscale(0.,55.);

		draw.setButton1Label("New");
		draw.setActionListenerForButton1(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.clear();
                oneRowOfNumbers();
            }
        });

        draw.setButton2Label("Exit");
        draw.setActionListenerForButton2(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        draw.setButton3Visible(false);


        draw.enableDoubleBuffering();

		for (int pos = 0;pos<50;pos++){
		    int imageIndex = (int) Math.round(Math.random()*trainImages.size());
		    int[][] image = trainImages.get(imageIndex);
            drawPictureAtPosition(image,pos,trainLabels[imageIndex]);
        }

        Kon.readInt();
    }


    private static void oneRowOfNumbers(){
        for (int pos = 0;pos<50;pos++){
            int imageIndex = (int) Math.round(Math.random()*trainImages.size());
            int[][] image = trainImages.get(imageIndex);
            drawPictureAtPosition(image,pos,trainLabels[imageIndex]);
        }
    }

    private static void drawPictureAtPosition( int[][] image,int pos, int value){
	    for (int width=0;width<28;width++)
	        for(int height=0;height<28;height++){
	            int grey=255-image[height][width];
                draw.setPenColor(grey,grey,grey);
                draw.point(width+pos*28,55-height);
	    }
	    draw.setPenColor(Color.BLUE);
        draw.text(14+pos*28,27-14,value+"");
        draw.show();
    }

	public static void printf(String format, Object... args) {
		System.out.printf(format, args);
	}


}
