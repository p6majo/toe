package com.p6majo.mnist;

import static java.lang.Math.min;
import static org.junit.Assert.*;

import java.util.List;

import com.p6majo.mnist.MNISTDataProvider;
import org.junit.Test;


public class TestMnistReader {

	@Test
	public void test() {
		String LABEL_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/t10k-labels-idx1-ubyte";
		String IMAGE_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/t10k-images-idx3-ubyte";

		int[] labels = MNISTDataProvider.getLabels(LABEL_FILE);
		List<int[][]> images = MNISTDataProvider.getImages(IMAGE_FILE);
		
		assertEquals(labels.length, images.size());
		assertEquals(28, images.get(0).length);
		assertEquals(28, images.get(0)[0].length);

		for (int i = 0; i < min(10, labels.length); i++) {
			printf("================= LABEL %d\n", labels[i]);
			printf("%s", MNISTDataProvider.renderImage(images.get(i)));
		}


		printf("There are %d images in the test data set.\n\n",images.size());

        LABEL_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/train-labels-idx1-ubyte";
        IMAGE_FILE = "/home/p6majo/workbase/toe/src/com/p6majo/mnist/train-images-idx3-ubyte";

        long start = System.currentTimeMillis();

        labels = MNISTDataProvider.getLabels(LABEL_FILE);
        images = MNISTDataProvider.getImages(IMAGE_FILE);

        printf("Parsing of the training data took %d ms.\n",(System.currentTimeMillis()-start));

        assertEquals(labels.length, images.size());
        assertEquals(28, images.get(0).length);
        assertEquals(28, images.get(0)[0].length);

        printf("There are %d images in the training data set.\n",images.size());


    }

	public static void printf(String format, Object... args) {
		System.out.printf(format, args);
	}


}
