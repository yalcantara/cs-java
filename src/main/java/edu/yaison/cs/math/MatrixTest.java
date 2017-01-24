package edu.yaison.cs.math;

import java.util.concurrent.TimeUnit;

public class MatrixTest {
	
	public static void main(String[] args) {
		
		int d = 1300;
		Matrix a = Matrix.rand(d);
		Matrix b = Matrix.rand(d);
		
		System.out.println("get");
		for (int i = 0; i < 5; i++) {
			long now = System.nanoTime();
			Matrix c = dot(a, b);
			long time = System.nanoTime() - now;
			System.out.printf("Took: %,d\n", TimeUnit.NANOSECONDS.toMillis(time));
		}
		
		System.out.println();
		System.out.println("[]");
		for (int i = 0; i < 5; i++) {
			long now = System.nanoTime();
			Matrix c = dot2(a, b);
			long time = System.nanoTime() - now;
			System.out.printf("Took: %,d\n", TimeUnit.NANOSECONDS.toMillis(time));
		}
		
	}
	
	public static Matrix dot(Matrix a, Matrix b) {
		int m = a.m();
		int n = a.n();
		int bm = b.m();
		int bn = b.n();
		
		if (n != bm) {
			throw new IllegalArgumentException("Invalid matrix dimension for multiplication. This "
					+ m + "x" + n + ", other " + bm + "x" + bn + ".");
		}
		
		Matrix c = new Matrix(m, bn);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < bn; j++) {
				float sum = 0.0f;
				for (int k = 0; k < n; k++) {
					sum += a.get(i, k) * b.get(k, j);
				}
				c.set(i, j, sum);
			}
		}
		
		return c;
	}
	
	public static Matrix dot2(Matrix a, Matrix b) {
		int m = a.m();
		int n = a.n();
		
		int p = b.n();
		
		if (n != b.m()) {
			throw new IllegalArgumentException("Invalid matrix dimension for multiplication. This "
					+ m + "x" + n + ", other " + b.m() + "x" + p + ".");
		}
		
		Matrix c = new Matrix(m, p);
		final float[] A = a.ptr();
		final float[] B = b.ptr();
		final float[] C = c.ptr();
		
		for (int i = 0; i < m; i++) {
			for (int k = 0; k < p; k++) {
				float sum = 0.0f;
				for (int j = 0; j < n; j++) {
					sum += A[i * n + j] * B[j * p + k];
				}
				C[i * p + k] = sum;
			}
		}
		
		return c;
	}
	
}
