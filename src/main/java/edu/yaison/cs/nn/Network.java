package edu.yaison.cs.nn;

import edu.yaison.cs.math.Matrix;

public class Network {
	
	public final Layer[] layers;
	
	private Matrix x;
	private Matrix y;
	
	private Matrix fx;
	
	private float alpha = 0.1f;
	
	public Network(int deepness) {
		layers = new Layer[deepness];
	}
	
	public Matrix h() {
		return fx;
	}
	
	public Matrix x() {
		return x;
	}
	
	public void x(Matrix x) {
		this.x = x;
	}
	
	public Matrix y() {
		return y;
	}
	
	public void y(Matrix y) {
		this.y = y;
	}
	
	public void fx() {
		
		int L = layers.length;
		fx = layers[0].foward(x);
		for (int l = 1; l < L; l++) {
			fx = layers[l].foward(fx);
		}
	}
	
	public void dx() {
		
		int L = layers.length;
		Matrix dx = layers[L - 1].backward(fx.sub(y));
		for (int l = L - 2; l >= 0; l--) {
			dx = layers[l].backward(dx);
		}
	}
	
	public void update() {
		int L = layers.length;
		
		for (int l = 0; l < L; l++) {
			layers[l].update(alpha);
		}
	}
	
	public double cost() {
		
		int m = y.m();
		return fx.sub(y).pow(2).sum() / (2.0 * m);
	}
}
