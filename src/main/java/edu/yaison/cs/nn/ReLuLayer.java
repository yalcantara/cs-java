package edu.yaison.cs.nn;

import static java.lang.Math.max;

import edu.yaison.cs.math.Matrix;

public class ReLuLayer implements Layer {
	
	private Matrix x;
	private Matrix f;
	
	private Matrix dx;
	
	private float func_fx(float x) {
		return max(0, x);
	}
	
	private float func_dx(float x) {
		return (x > 0) ? 1 : 0;
	}
	
	public Matrix foward(Matrix x) {
		
		int m = x.m();
		int n = x.n();
		this.x = x;
		f = new Matrix(m, n);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				float z = x.get(i, j);
				f.set(i, j, func_fx(z));
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
				float fdx = dg.get(i, j) * func_dx(x.get(i, j));
				
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
