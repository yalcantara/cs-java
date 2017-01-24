package edu.yaison.cs.math;

import com.yaison.cerebro.Loader;
import com.yaison.cerebro.structs.Data;

import edu.yaison.cs.nn.AffineLayer;
import edu.yaison.cs.nn.Network;
import edu.yaison.cs.nn.SigmoidLayer;
import edu.yaison.cs.nn.SoftMaxLayer;

public class Main {
	
	public static void main(String[] args) {
		
		Data data = Loader.fromFile("files/iris.data");
		
		int m = data.rows();
		com.yaison.cerebro.math.Matrix raw = data.matrix();
		double[] xraw = raw.selectColumns(0, 4).toSingleArray();
		double[] yraw = raw.selectColumns(4, 7).toSingleArray();
		
		Matrix x = new Matrix(xraw, m, 4);
		Matrix y = new Matrix(yraw, m, 3);
		
		Network n = new Network(4);
		n.x(x);
		n.y(y);
		
		n.layers[0] = new AffineLayer(x.n(), x.n());
		n.layers[1] = new SigmoidLayer();
		n.layers[2] = new AffineLayer(x.n(), 3);
		n.layers[3] = new SoftMaxLayer();
		
		int iter = 1000;
		System.out.println();
		
		for (int i = 0; i <= iter; i++) {
			
			n.fx();
			n.dx();
			n.update();
			if (i % (iter / 10) == 0) {
				double j = n.cost();
				System.out.printf("iter: %,10d       J:  %12.8f\n", i, j);
			}
			
		}
		
	}
	
	public void test2() {
		
		double[] arr1 = { 0, 0, 0, 1, 1, 0, 1, 1 };
		
		double[] arr3 = { 1, 0, 0, 1 };
		Matrix x = new Matrix(arr1, 4, 2);
		Matrix y = new Matrix(arr3, 4, 1);
		
		System.out.println("X:");
		x.print();
		
		AffineLayer l1 = new AffineLayer(x.n(), x.n());
		SigmoidLayer l2 = new SigmoidLayer();
		AffineLayer l3 = new AffineLayer(x.n(), 1);
		SigmoidLayer l4 = new SigmoidLayer();
		
		float alpha = 0.1f;
		int iter = 10000;
		for (int i = 0; i <= iter; i++) {
			
			l1.foward(x);
			l2.foward(l1.f());
			l3.foward(l2.f());
			l4.foward(l3.f());
			
			Matrix f = l4.f();
			Matrix dg = f.sub(y);
			
			l4.backward(dg);
			l3.backward(l4.dx());
			l2.backward(l3.dx());
			l1.backward(l2.dx());
			
			l1.update(alpha);
			l3.update(alpha);
			
			if (i % (iter / 10) == 0) {
				System.out.printf("iter: %6d\n", i);
				System.out.println("==============================");
				
				l4.f().print();
				System.out.println();
			}
		}
	}
	
	public void test1(Matrix x, Matrix w) {
		System.out.println("Numerical Gradients");
		System.out.println("====================================================");
		System.out.println("dFX");
		numericalGradX(x, w).print();
		System.out.println("dFW");
		numericalGradW(x, w).print();
		
		System.out.println("Analitical Gradients");
		System.out.println("====================================================");
		System.out.println("dx");
		analiticalGradX(x, w).print();
		System.out.println("dw");
		analiticalGradW(x, w).print();
	}
	
	public static Matrix numericalGradX(Matrix x, Matrix w) {
		
		float delta = 0.0001f;
		
		Matrix dxr = x.add(delta);
		Matrix dxl = x.sub(delta);
		
		Matrix fxr = fx(dxr, w);
		Matrix fxl = fx(dxl, w);
		
		Matrix dif = fxr.sub(fxl);
		
		return dif.div(2 * delta);
		
	}
	
	public static Matrix numericalGradW(Matrix x, Matrix w) {
		
		float delta = 0.0001f;
		
		Matrix wr = w.add(delta);
		Matrix wl = w.sub(delta);
		
		Matrix fr = fx(x, wr);
		Matrix fl = fx(x, wl);
		
		Matrix dif = fr.sub(fl);
		
		return dif.div(2 * delta);
		
	}
	
	public static Matrix analiticalGradX(Matrix x, Matrix w) {
		
		int m = x.rows();
		int n = x.cols();
		int p = w.cols();
		AffineLayer layer = new AffineLayer(n, p);
		layer.w(w);
		layer.foward(x);
		
		Matrix dg = new Matrix(m, p);
		dg.setAll(1.0f);
		layer.backward(dg);
		
		return layer.dx();
	}
	
	public static Matrix analiticalGradW(Matrix x, Matrix w) {
		
		int m = x.rows();
		int n = x.cols();
		int p = w.cols();
		AffineLayer layer = new AffineLayer(n, p);
		layer.w(w);
		layer.foward(x);
		
		Matrix dg = new Matrix(m, p);
		dg.setAll(1.0f);
		layer.backward(dg);
		
		return layer.dw();
	}
	
	public static double error(Matrix a, Matrix b) {
		
		return a.sub(b).pow(2).mult(1.0 / a.m()).sum();
	}
	
	public static Matrix fx(Matrix x, Matrix w) {
		
		int m = x.m();
		
		int p = w.n();
		Matrix fx = new Matrix(m, p);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < p; j++) {
				Vector row = x.row(i);
				Vector col = w.col(j);
				fx.set(i, j, fx(row, col));
			}
		}
		
		return fx;
	}
	
	public static double fx(Vector x, Vector w) {
		
		double sum = 0.0;
		for (int i = 0; i < x.length(); i++) {
			sum += x.get(i) * w.get(i);
		}
		
		return sum;
	}
	
}
