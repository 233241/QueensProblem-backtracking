package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// para liczb typu int
final class IntPair implements Cloneable {
    public int x, y;

    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

final class QueensProblem {
    private final int numberOfQueens;
    private final boolean[] rows;
    private final boolean[] columns;
    private final boolean[] positiveDiagonals;
    private final boolean[] negativeDiagonals;
    private final ArrayList<IntPair[]> solutions;

    public QueensProblem(int n) {
        numberOfQueens = n;
        rows = new boolean[n];
        columns = new boolean[n];
        positiveDiagonals = new boolean[2*n-1];
        negativeDiagonals = new boolean[2*n-1];
        solutions = new ArrayList<>();
    }

    public List<IntPair[]> solve() {
        solveAux(0, new IntPair[numberOfQueens]);
        return solutions;
    }

    private void solveAux(int n, IntPair[] solution) {
        IntPair lastQueenPosition = null;
        if (n > 0 && n < numberOfQueens)
            lastQueenPosition = solution[n-1];

        // pobierz mozliwych kandydatow
        List<IntPair> candidates = getCandidates(lastQueenPosition);
        // sprawdzamy wszystkich kandydatow
        for (IntPair candidate : candidates) {
            insert(candidate, n, solution);
            solveAux(n+1, solution);
            remove(solution, n);
        }

        // nie ma wiecej kandydatow

        // wstawilismy juz n hetmanow, wiec znalezlismy rozwiazanie
        // dopisujemy rozwiazanie do listy rozwiazan
        if (n == numberOfQueens)
            addSolution(solution);

        // nie mozemy wstawic hetmana wiec wracamy
        // jesli n == 0, to koniec calego przeszukiwania
    }

    private List<IntPair> getCandidates(IntPair lastQueenPosition) {
        ArrayList<IntPair> result = new ArrayList<>();

        if (lastQueenPosition == null)
            lastQueenPosition = new IntPair(-1, -1);

        for (int y = lastQueenPosition.y + 1; y < numberOfQueens; y++)
            for (int x = 0; x < numberOfQueens; x++)
                if (canInsert(x, y))
                    result.add(new IntPair(x, y));

        return result;
    }

    // sprawdza dana pozycja nie koliduje z zadnym z hetmanow
    private boolean canInsert(int x, int y) {
        return ! (rows[y] || columns[x] || positiveDiagonals[x+y] || negativeDiagonals[x - y + numberOfQueens - 1]);
    }

    private void insert(IntPair position, int n, IntPair[] solution) {
        solution[n] = position;
        tick(position);
//        rows[position.y] = true;
//        columns[position.x] = true;
//        positiveDiagonals[position.x + position.y] = true;
//        negativeDiagonals[position.x - position.y + numberOfQueens - 1] = true;
    }

    // napisac change -> x[i] = not x[i]

    private void remove(IntPair[] solution, int n) {
        IntPair position = solution[n];
        tick(position);
//        rows[position.y] = false;
//        columns[position.x] = false;
//        positiveDiagonals[position.x + position.y] = false;
//        negativeDiagonals[position.x - position.y + numberOfQueens - 1] = false;
    }

    private void tick(IntPair position) {
        rows[position.y] = ! rows[position.y];
        columns[position.x] = ! columns[position.x];
        positiveDiagonals[position.x + position.y] = ! positiveDiagonals[position.x + position.y];
        negativeDiagonals[position.x - position.y + numberOfQueens - 1] = ! negativeDiagonals[position.x - position.y + numberOfQueens - 1];
    }

    private void addSolution(IntPair[] solution) {
        solutions.add(Arrays.copyOf(solution, solution.length));
    }
}

public class Main {

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        QueensProblem queensProblem = new QueensProblem(n);
        List<IntPair[]> solutions = queensProblem.solve();

        solutions.forEach(Main::print);
        System.out.println("Number of solutions: " + solutions.size());
    }

    // wypisuje tablice
    private static void print(Object[] solution) {
        System.out.print("[ ");
        // wypisz pierwszy element
        if (solution.length > 0)
            System.out.print(solution[0]);
        // wypisz reszte elementow
        for (int i = 1; i < solution.length; i++)
            System.out.print(", " + solution[i]);
        System.out.println(" ]");
    }
}
