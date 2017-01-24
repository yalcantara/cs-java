package edu.yaison.cs.math;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.Random;

public class Vector {

	private static final Random r = new Random();

	private final double[] arr;

	public static Vector rand(int d) {
		Vector v = new Vector(d);

		for (int i = 0; i < d; i++) {
			v.set(i, r.nextGaussian());
		}

		return v;
	}

	public Vector(double[] arr) {
		int d = arr.length;

		this.arr = new double[d];

		for (int i = 0; i < d; i++) {
			this.arr[i] = arr[i];
		}
	}

	public Vector(int d) {
		this.arr = new double[d];
	}

	public Vector add(Vector b) {

		int d = length();

		int bd = b.length();
		if (d != bd) {
			throw new IllegalArgumentException("Vector dimensions must be the same. This " + b + ", other " + bd + ".");
		}

		Vector c = new Vector(d);
		for (int i = 0; i < d; i++) {
			c.set(i, get(i) + b.get(i));
		}

		return c;
	}

	public double dot(Vector v) {
		int l = length();
		int vl = v.length();
		if (l != vl) {
			throw new IllegalArgumentException("The vector length must be " + l + ", but instead got: " + vl + ".");
		}

		double sum = 0;
		for (int i = 0; i < l; i++) {
			sum += get(i) * v.get(i);
		}

		return sum;
	}

	public double get(int idx) {
		return arr[idx];
	}

	public void inc(int idx, double val) {
		arr[idx] += val;
	}

	public int length() {
		return arr.length;
	}

	public double norm() {
		double sum = 0.0;
		int l = length();
		for (int i = 0; i < l; i++) {
			sum += pow(get(i), 2);
		}

		return sqrt(sum);
	}

	public Vector proj(Vector v) {
		return scale(dot(v) / dot(this));
	}

	public Vector scale(double scalar) {
		int d = length();
		Vector v = new Vector(d);
		for (int i = 0; i < d; i++) {
			v.set(i, get(i) * scalar);

		}
		return v;
	}

	public void set(int idx, double val) {
		arr[idx] = val;
	}

	public Vector sub(Vector b) {

		int d = length();

		int bd = b.length();
		if (d != bd) {
			throw new IllegalArgumentException("Vector dimensions must be the same. This " + b + ", other " + bd + ".");
		}

		Vector c = new Vector(d);
		for (int i = 0; i < d; i++) {
			c.set(i, get(i) - b.get(i));
		}

		return c;
	}

	public double[] toArray() {

		final int d = length();
		double[] arr = new double[d];
		for (int j = 0; j < d; j++) {
			arr[j] = get(j);
		}

		return arr;
	}

	public Vector unit() {
		int s = length();
		Vector v = new Vector(s);
		double norm = norm();

		for (int i = 0; i < s; i++) {
			v.set(i, get(i) / norm);
		}

		return v;
	}
}
