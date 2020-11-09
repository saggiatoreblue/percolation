/* *****************************************************************************
 *  Name:              Michael Botelho
 *  Coursera User ID:
 *  Last modified:     11/9/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] data;
    private int lastIdx;
    private int N;
    private WeightedQuickUnionUF puf;
    private WeightedQuickUnionUF ifuf;
    private int openSites = 0;

    public Percolation(int N) {
        if (N <= 0) {
            StringBuilder str = new StringBuilder("Input out of bounds");
            str.append(N).append(". Value must be greater than 0");
            throw new IllegalArgumentException(str.toString());
        }

        data = new boolean[N][N];

        this.N = N;
        lastIdx = N * N + 1;
        puf = new WeightedQuickUnionUF(lastIdx + 1);
        ifuf = new WeightedQuickUnionUF(lastIdx + 1);
    }

    public void open(int row, int column) {
        validate(row, column);
        if (isOpen(row, column)) {
            return;
        }
        data[row - 1][column - 1] = true;

        if (row > 1) {
            if (isOpen(row - 1, column)) {
                puf.union(flatten(row, column), flatten(row - 1, column));
                ifuf.union(flatten(row, column), flatten(row - 1, column));
            }
        } else {
            puf.union(0, flatten(row, column));
            ifuf.union(0, flatten(row, column));
        }

        if (row < N) {
            if (isOpen(row + 1, column)) {
                puf.union(flatten(row, column), flatten(row + 1, column));
                ifuf.union(flatten(row, column), flatten(row + 1, column));
            }
        } else {
            puf.union(lastIdx, flatten(row, column));
        }

        if (column > 1 && isOpen(row, column - 1)) {
            puf.union(flatten(row, column), flatten(row, column -1));
            ifuf.union(flatten(row, column), flatten(row, column - 1));
        }

        if (column < N && isOpen(row, column + 1)) {
            puf.union(flatten(row, column), flatten(row, column + 1));
            ifuf.union(flatten(row, column), flatten(row, column + 1));
        }
        openSites++;
    }

    public boolean isOpen(int row, int column) {
        validate(row, column);
        return data[row - 1][column - 1];
    }

    public boolean isFull(int row, int column) {
        validate(row, column);
        return ifuf.find(0) == ifuf.find(flatten(row, column));
    }

    public boolean percolates() {
        return puf.find(0) == puf.find(lastIdx);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    private void validate(int row, int column) {
        if (row < 1 || row > N) {
            throw new IllegalArgumentException("Row value is out of bounds: " + row);
        }
        if (column < 1 || column > N) {
            throw new IllegalArgumentException("Column value is out of bounds: " + column);
        }
    }

    private int flatten(int row, int column) {
        validate(row, column);
        return (row - 1) * N + column;
    }
}
