package edu.yaison.cs.nn;

import edu.yaison.cs.math.Matrix;

public interface Layer {
	
	public Matrix foward(Matrix x);
	
	public Matrix backward(Matrix dg);
	
	public void update(float alpha);
}
