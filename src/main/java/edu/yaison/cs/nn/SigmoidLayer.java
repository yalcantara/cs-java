package edu.yaison.cs.nn;

import static java.lang.Math.exp;

import edu.yaison.cs.math.Matrix;

public class SigmoidLayer implements Layer {
	
	private Matrix x;
	private Matrix f;
	
	private Matrix dx;
	
	private double gx(double z) {
		return 1.0 / (1 + exp(-z));
	}
	
	private double gdx(double z) {
		return gx(z) * (1 - gx(z));
	}
	
	public Matrix foward(Matrix x) {
		
		int m = x.m();
		int n = x.n();
		this.x = x;
		f = new Matrix(m, n);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				double z = x.get(i, j);
				f.set(i, j, gx(z));
			}
		}
		
		return f;
	}
	
	public Matrix backward(Matrix dg) {
		
		int m = x.m();
		int n = x.n();
		dx = new Matrix(m, n);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				double fdx = dg.get(i, j) * gdx(x.get(i, j));
				
				dx.set(i, j, fdx);
			}
		}
		
		return dx;
	}
	
	public Matrix f() {
		return f;
	}
	
	public Matrix dx() {
		return dx;
	}
	
	public void update(float alpha) {
		
	}
}
