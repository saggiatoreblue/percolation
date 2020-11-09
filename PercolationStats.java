/* *****************************************************************************
 *  Name:              Michael Botelho
 *  Coursera User ID:
 *  Last modified:     11/9/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private int T;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Argument out of bounds");
        }
        this.T = T;
        double[] results = runMonteCarlo(N);
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);

    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return mean - confidenceInterval();
    }

    public double confidenceHi() {
        return mean + confidenceInterval();
    }

    private double confidenceInterval() {
        return 1.96 * stddev / Math.sqrt(this.T);
    }

    private double[] runMonteCarlo(int N) {
        double[] results = new double[T];
        for (int i = 0; i < T; i++) {
            results[i] = runMonteCaroloInstance(N);
        }
        return results;
    }

    private double runMonteCaroloInstance(int N) {
        Percolation perc = new Percolation(N);
        double openSites = 0.0;
        while(!perc.percolates()) {
            int i = StdRandom.uniform(1, N + 1);
            int j = StdRandom.uniform(1, N + 1);
            if (!perc.isOpen(i, j)) {
                perc.open( i, j);
                openSites++;
            }
        }
        return openSites / (N * N);
    }

    public static void main(String[] args) {
        int N = StdIn.readInt();
        int T = StdIn.readInt();

        PercolationStats stats = new PercolationStats(N, T);
        StdOut.println("Mean = " + stats.mean);
        StdOut.println("Stddev = " + stats.stddev);
        StdOut.println("95% Confidence Interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
