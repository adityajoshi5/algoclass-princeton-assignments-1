public class PercolationStats {

	private double[] thresholds;

	public PercolationStats(int N, int T) {
		// perform T independent computational experiments on an N-by-N grid
		if (T <= 0 || N <= 0) {
			throw new IllegalArgumentException("N and T should be positive!");
		}
		thresholds = new double[T];
		for (int i = 0; i < T; i++) {
			Percolation percolation = new Percolation(N);
			double openSitesCount = 0d;
			while (!percolation.percolates()) {
				int x = StdRandom.uniform(1, N + 1);
				int y = StdRandom.uniform(1, N + 1);
				if (!percolation.isOpen(x, y)) {
					percolation.open(x, y);
					openSitesCount++;
				}
			}
			thresholds[i] = openSitesCount / (N*N);
		}
	}

	public double mean() {
		// sample mean of percolation threshold
		return StdStats.mean(thresholds);
	}

	public double stddev() {
		// sample standard deviation of percolation threshold
		if (thresholds.length > 1) {
			return StdStats.stddev(thresholds);
		} else {
			return Double.NaN;
		}
	}

	public static void main(String[] args) {
		int T = Integer.parseInt(args[1]);
		PercolationStats stats = new PercolationStats(
				Integer.parseInt(args[0]), T);
		double mean = stats.mean();
		StdOut.println("mean = " + mean);
		double stddev = stats.stddev();
		StdOut.println("stddev = " + stddev);
		double d = (1.96 * stddev) / Math.sqrt(T);
		StdOut.println("95% confidence interval = " + (mean - d) + ", "
				+ (mean + d));
	}

}
