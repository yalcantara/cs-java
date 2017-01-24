package edu.yaison.cs.nn;

import static java.lang.Math.exp;

import edu.yaison.cs.math.Matrix;

public class SoftMaxLayer implements Layer {
	
	private Matrix fx;
	
	public Matrix foward(Matrix x) {
		
		int m = x.m();
		int n = x.n();
		fx = new Matrix(m, n);
		
		for (int i = 0; i < m; i++) {
			
			float max = x.rowMax(i);
			
			float sum = 0.0f;
			for (int j = 0; j < n; j++) {
				float val = (float) exp(x.get(i, j) - max);
				fx.set(i, j, val);
				sum += val;
			}
			
			for (int j = 0; j < n; j++) {
				float val = fx.get(i, j) / sum;
				fx.set(i, j, val);
			}
			
		}
		
		return fx;
	}
	
	public Matrix backward(Matrix dg) {
		
		// return fx.mult(-1).add(1.0).mult(fx).mult(dg);
		return dg;
	}
	
	public void update(float alpha) {
		// TODO Auto-generated method stub
		
	}
	
}
