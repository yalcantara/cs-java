package edu.yaison.cs.nn;

import edu.yaison.cs.math.Matrix;
import edu.yaison.cs.math.Vector;

public class AffineLayer implements Layer {
	
	// m = inputs
	// n = outputs
	private final Matrix w;
	private final Vector b;
	
	private Matrix x;
	private Matrix f;
	
	private Matrix dx;
	private Matrix dw;
	private Vector db;
	
	public AffineLayer(int in, int out) {
		w = Matrix.rand(in, out);
		b = Vector.rand(out);
	}
	
	public void w(Matrix w) {
		w.copyTo(this.w);
	}
	
	public Matrix foward(Matrix x) {
		this.x = x;
		f = x.affine(w, b);
		return f;
	}
	
	public Matrix backward(Matrix dg) {
		
		int m = x.m();
		int n = x.n();
		int p = w.n();
		
		dx = new Matrix(m, n);
		dw = new Matrix(n, p);
		db = new Vector(p);
		
		for (int i = 0; i < m; i++) {
			for (int k = 0; k < p; k++) {
				
				double fdg = dg.get(i, k);
				for (int j = 0; j < n; j++) {
					double dfw = fdg * w.get(j, k);
					double dfx = fdg * x.get(i, j);
					
					dx.inc(i, j, dfw);
					dw.inc(j, k, dfx);
				}
				
				db.inc(k, fdg);
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
	
	public Matrix dw() {
		return dw;
	}
	
	public int out() {
		return w.n();
	}
	
	public int in() {
		return w.m();
	}
	
	public void update(float alpha) {
		
		int in = w.m();
		int out = w.n();
		
		for (int i = 0; i < in; i++) {
			for (int j = 0; j < out; j++) {
				w.set(i, j, w.get(i, j) - dw.get(i, j) * alpha);
			}
		}
		
		for (int j = 0; j < out; j++) {
			b.set(j, b.get(j) - db.get(j) * alpha);
		}
	}
}
