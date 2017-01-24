package edu.yaison.cs.math;

import static java.lang.Math.max;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class Matrix {
	
	private static final int MAX_PRINT_ROWS = 500;
	private static final int MAX_PRINT_COLS = 500;
	
	private static final Random r = new Random();
	
	private final int m;
	private final int n;
	private final float[] grid;
	
	public Matrix(int m, int n) {
		this.m = m;
		this.n = n;
		grid = new float[m * n];
		
	}
	
	public Matrix(float[] arr, int m, int n) {
		this.m = m;
		this.n = n;
		grid = new float[m * n];
		
		int l = m * n;
		for (int i = 0; i < l; i++) {
			grid[i] = arr[i];
		}
	}
	
	public Matrix(double[] arr, int m, int n) {
		this.m = m;
		this.n = n;
		grid = new float[m * n];
		
		int l = m * n;
		for (int i = 0; i < l; i++) {
			grid[i] = (float) arr[i];
		}
	}
	
	public static Matrix rand(int d) {
		return rand(d, d);
	}
	
	public static Matrix rand(int m, int n) {
		
		Matrix mtr = new Matrix(m, n);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(i, j, r.nextGaussian());
			}
		}
		return mtr;
	}
	
	public void set(int row, int col, double val) {
		set(row, col, (float) val);
	}
	
	public void set(int row, int col, float val) {
		if (row >= m) {
			throw new IndexOutOfBoundsException("The row parameter is " + "out of bounds. Rows " + m
					+ ", row parameter: " + row + ".");
		}
		
		if (col >= n) {
			throw new IndexOutOfBoundsException("The col parameter is " + "out of bounds. Columns "
					+ n + ", col parameter: " + col + ".");
		}
		grid[row * n + col] = val;
	}
	
	public void inc(int row, int col, double val) {
		if (row >= m) {
			throw new IndexOutOfBoundsException("The row parameter is " + "out of bounds. Rows " + m
					+ ", row parameter: " + row + ".");
		}
		
		if (col >= n) {
			throw new IndexOutOfBoundsException("The col parameter is " + "out of bounds. Columns "
					+ n + ", col parameter: " + col + ".");
		}
		grid[row * n + col] += val;
	}
	
	public int n() {
		return n;
	}
	
	public int m() {
		return m;
	}
	
	public float get(int row, int col) {
		if (row < 0 || row >= m) {
			throw new IllegalArgumentException("The row param must be between [0, " + (m - 1)
					+ "], but instead got: " + row + ".");
		}
		
		if (col < 0 || col >= n) {
			throw new IllegalArgumentException("The col param must be between [0, " + (n - 1)
					+ "], but instead got: " + col + ".");
		}
		return grid[row * n + col];
	}
	
	public int length() {
		return m() * n();
	}
	
	public int cols() {
		return n();
	}
	
	public Vector col(int idx) {
		int m = m();
		Vector v = new Vector(m);
		
		for (int i = 0; i < m; i++) {
			v.set(i, get(i, idx));
		}
		
		return v;
	}
	
	public int rows() {
		return m();
	}
	
	public Vector row(int idx) {
		int n = n();
		Vector v = new Vector(n);
		
		for (int j = 0; j < n; j++) {
			v.set(j, get(idx, j));
		}
		
		return v;
	}
	
	public double[][] toArray() {
		double[][] arr = new double[rows()][cols()];
		
		final int m = m();
		final int n = n();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				arr[i][j] = get(i, j);
			}
		}
		
		return arr;
	}
	
	public void copyRow(int srcRow, Matrix dest, int destRow) {
		int n = n();
		for (int j = 0; j < n; j++) {
			dest.set(destRow, j, get(srcRow, j));
		}
	}
	
	public Matrix selectRows(int start, int end) {
		int m = m();
		int n = n();
		int l = end - start;
		
		if (end <= start) {
			throw new IllegalArgumentException(
					"The 'end' parameter must be higher than the 'start' parameter. Expected >  "
							+ start + ", but got " + end + " instead.");
		}
		
		if (l > m) {
			throw new IllegalArgumentException(
					"For roll=false the length of the selection can not be higher than this matrix number of rows. Number of rows: "
							+ m + ", selection length: " + l + ".");
		}
		
		if (end > m) {
			throw new IllegalArgumentException(
					"For roll=false the end parameter must be less or equals than the number of rows. Number of rows: "
							+ m + ", end parameter: " + m + ".");
		}
		
		Matrix mtr = new Matrix(l, n);
		for (int i = 0; i < l; i++) {
			copyRow(start + i, mtr, i);
		}
		
		return mtr;
	}
	
	public double colMin(int col) {
		final int rows = rows();
		double min = get(0, col);
		for (int i = 1; i < rows; i++) {
			min = Math.min(min, get(i, col));
		}
		
		return min;
	}
	
	public double colMax(int col) {
		final int rows = rows();
		double max = get(0, col);
		for (int i = 1; i < rows; i++) {
			max = Math.max(max, get(i, col));
		}
		
		return max;
	}
	
	public float rowMax(int row) {
		
		float max = get(row, 0);
		for (int j = 1; j < n; j++) {
			max = Math.max(max, get(row, j));
		}
		
		return max;
	}
	
	public Matrix add(Matrix b) {
		
		int bm = b.m();
		int bn = b.n();
		if (m != bm || n != bn) {
			throw new IllegalArgumentException("Matrix dimensions must be the same. This " + m + "x"
					+ n + ", other " + bm + "x" + bn);
		}
		
		Matrix c = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c.set(i, j, get(i, j) + b.get(i, j));
			}
		}
		
		return c;
	}
	
	public Matrix add(double val) {
		return add((float) val);
	}
	
	public Matrix add(float val) {
		
		int m = m();
		int n = n();
		
		Matrix c = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c.set(i, j, get(i, j) + val);
			}
		}
		
		return c;
	}
	
	public void copyTo(Matrix dest) {
		
		int bm = dest.m();
		int bn = dest.n();
		if (m != bm || n != bn) {
			throw new IllegalArgumentException("Matrix dimensions must be the same. This " + m + "x"
					+ n + ", other " + bm + "x" + bn);
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				dest.set(i, j, get(i, j));
			}
		}
		
	}
	
	public Matrix sub(float val) {
		
		int m = m();
		int n = n();
		
		Matrix c = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c.set(i, j, get(i, j) - val);
			}
		}
		
		return c;
	}
	
	public void setAll(float val) {
		
		int m = m();
		int n = n();
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, val);
			}
		}
		
	}
	
	public Matrix pow(float exp) {
		
		int m = m();
		int n = n();
		
		Matrix c = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c.set(i, j, (float) Math.pow(get(i, j), exp));
			}
		}
		
		return c;
	}
	
	public Matrix sub(Matrix b) {
		int m = m();
		int n = n();
		int bm = b.m();
		int bn = b.n();
		if (m != bm || n != bn) {
			throw new IllegalArgumentException("Matrix dimensions must be the same. This " + m + "x"
					+ n + ", other " + bm + "x" + bn);
		}
		
		Matrix c = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c.set(i, j, get(i, j) - b.get(i, j));
			}
		}
		
		return c;
	}
	
	public Matrix dot(Matrix b) {
		int m = m();
		int n = n();
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
					sum += get(i, k) * b.get(k, j);
				}
				c.set(i, j, sum);
			}
		}
		
		return c;
	}
	
	public Matrix dot2(Matrix b) {
		int m = m();
		int n = n();
		
		int p = b.n();
		
		if (n != b.m) {
			throw new IllegalArgumentException("Invalid matrix dimension for multiplication. This "
					+ m + "x" + n + ", other " + b.m + "x" + p + ".");
		}
		
		Matrix c = new Matrix(m, p);
		final float[] A = this.grid;
		final float[] B = b.grid;
		final float[] C = c.grid;
		
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
	
	public Matrix affine(Matrix w, Vector b) {
		int m = m();
		int n = n();
		int wm = w.m();
		int wn = w.n();
		int bl = b.length();
		
		if (n != wm) {
			throw new IllegalArgumentException("Invalid matrix dimension for multiplication. This "
					+ m + "x" + n + ", other " + wm + "x" + wn + ".");
		}
		
		if (wn != bl) {
			throw new IllegalArgumentException(
					"Invalid vector dimension for addition broadcast. Expected " + n + ", but got "
							+ bl + " instead.");
		}
		
		Matrix c = new Matrix(m, wn);
		for (int i = 0; i < m; i++) {
			for (int k = 0; k < wn; k++) {
				float sum = 0.0f;
				for (int j = 0; j < n; j++) {
					sum += get(i, j) * w.get(j, k);
				}
				
				sum += b.get(k);
				c.set(i, k, sum);
			}
		}
		
		return c;
	}
	
	public Matrix mult(Matrix b) {
		int m = m();
		int n = n();
		int om = b.m();
		int on = b.n();
		
		if (n != on || m != om) {
			throw new IllegalArgumentException(
					"Invalid matrix dimension for element-wise multiplication. Expected "
							+ "the same dimension for both matrices, but instead go: this " + m
							+ "x" + n + ", other " + om + "x" + on + ".");
		}
		
		Matrix c = new Matrix(m, on);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < on; j++) {
				c.set(i, j, get(i, j) * b.get(i, j));
			}
		}
		
		return c;
	}
	
	public Matrix mult(double scalar) {
		return mult((float) scalar);
	}
	
	public Matrix mult(float scalar) {
		int m = m();
		int n = n();
		Matrix mtr = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(i, j, get(i, j) * scalar);
			}
		}
		return mtr;
	}
	
	public Matrix div(double scalar) {
		return div((float) scalar);
	}
	
	public Matrix div(float scalar) {
		int m = m();
		int n = n();
		Matrix c = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c.set(i, j, get(i, j) / scalar);
			}
		}
		return c;
	}
	
	public Vector dot(Vector x) {
		int m = m();
		int n = n();
		int l = x.length();
		
		if (n != l) {
			throw new IllegalArgumentException("Invalid matrix dimension for multiplication. This "
					+ m + "x" + n + ", vector length " + l + ".");
		}
		
		Vector b = new Vector(m);
		for (int i = 0; i < m; i++) {
			double sum = 0.0;
			for (int j = 0; j < n; j++) {
				sum += get(i, j) * x.get(j);
			}
			b.set(i, sum);
		}
		
		return b;
	}
	
	public Matrix trans() {
		int m = m();
		int n = n();
		Matrix mtr = new Matrix(n, m);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(j, i, get(i, j));
			}
		}
		return mtr;
	}
	
	public double sum() {
		int m = m();
		int n = n();
		
		double sum = 0.0;
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				sum += get(i, j);
			}
		}
		
		return sum;
	}
	
	public Matrix insertCol(int colIdx, float value) {
		int m = m();
		int n = n();
		
		Matrix mtr = new Matrix(m, n + 1);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (j >= colIdx) {
					mtr.set(i, j + 1, get(i, j));
				} else {
					mtr.set(i, j, get(i, j));
				}
			}
		}
		
		for (int i = 0; i < m; i++) {
			mtr.set(i, colIdx, value);
		}
		
		return mtr;
	}
	
	public double colSum(int col) {
		double sum = 0.0;
		
		final int rows = rows();
		for (int i = 0; i < rows; i++) {
			sum += get(i, col);
		}
		
		return sum;
	}
	
	public double colVar(int col) {
		return colVar(col, colMean(col));
	}
	
	public double colVar(int col, double mean) {
		
		// See org.apache.commons.math3.stat.descriptive.moment.Variance
		final int rows = rows();
		double var = Double.NaN;
		if (rows == 1) {
			var = 0.0;
		} else if (rows > 1) {
			double accum = 0.0;
			double dev = 0.0;
			double accum2 = 0.0;
			for (int i = 0; i < rows; i++) {
				dev = get(i, col) - mean;
				accum += dev * dev;
				accum2 += dev;
			}
			
			double N = rows;
			double n = N - 1; // correct the bias
			var = (accum - (accum2 * accum2 / N)) / n;
		}
		
		return var;
	}
	
	public double colMean(int col) {
		
		final int rows = rows();
		double sum = colSum(col);
		
		double xbar = sum / rows;
		
		// Compute correction factor in second pass
		double correction = 0;
		for (int i = 0; i < rows; i++) {
			correction += get(i, col) - xbar;
		}
		
		final double n = rows;
		return xbar + (correction / n);
	}
	
	public float[] ptr() {
		return grid;
	}
	
	@Override
	public Matrix clone() {
		int m = m();
		int n = n();
		Matrix mtr = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(i, j, get(i, j));
			}
		}
		return mtr;
	}
	
	public void print() {
		print(System.out, MAX_PRINT_ROWS, MAX_PRINT_COLS);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(1024);
		print(sb, MAX_PRINT_ROWS, MAX_PRINT_COLS);
		return sb.toString();
	}
	
	public void print(Appendable out, int maxRows, int maxCols) {
		
		maxRows = (maxRows < m) ? maxRows : m;
		maxCols = (maxCols < n) ? maxCols : n;
		
		try {
			if (maxRows < m || maxCols < n) {
				out.append("Matrix " + m + "x" + n + "  (truncated)\n");
			} else {
				out.append("Matrix " + m + "x" + n + "\n");
			}
			
			NumberFormat f = DecimalFormat.getNumberInstance();
			f.setMaximumFractionDigits(4);
			f.setMinimumFractionDigits(4);
			f.setGroupingUsed(true);
			
			int[] maxLength = new int[maxCols];
			
			for (int j = 0; j < maxCols; j++) {
				for (int i = 0; i < maxRows; i++) {
					
					String str = f.format(get(i, j));
					
					maxLength[j] = max(maxLength[j], str.length());
				}
			}
			
			for (int i = 0; i < maxRows; i++) {
				for (int j = 0; j < maxCols; j++) {
					if (j > 0) {
						out.append("  ");
					}
					String str = f.format(get(i, j));
					int leading = maxLength[j] - str.length();
					for (int s = 0; s < leading; s++) {
						out.append(" ");
					}
					
					out.append(str);
				}
				out.append("\n");
			}
			
			out.append("\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
