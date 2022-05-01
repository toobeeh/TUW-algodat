package main.java.exercise;

import main.java.framework.StudentInformation;
import main.java.framework.StudentSolution;

import java.util.ArrayList;
import java.util.Random;

public class StudentSolutionImplementation implements StudentSolution {
    @Override
    public StudentInformation provideStudentInformation() {
        return new StudentInformation(
                "Tobias", // Vorname
                "Scharsching", // Nachname
                "12123692" // Matrikelnummer
        );
    }

    // Implementieren Sie hier Ihre Lösung für Insertion Sort
    public void insertionSort(Point[] points, int a, int b, boolean sortX) {

        for(int takeIndex = a; takeIndex < b; takeIndex++){

            // temp store number at index
            Point take = points[takeIndex];

            // find index to put temp in and shift all above
            int insertIndex = takeIndex - 1;
            while(insertIndex >= a && (sortX ? points[insertIndex].getX() > take.getX() : points[insertIndex].getY() > take.getY()) ) {
                points[insertIndex+1] = points[insertIndex];
                insertIndex--;
            }
            points[insertIndex+1] = take;
        }
    }

    // Implementieren Sie hier Ihre Lösung für Brute Force
    public PointPair bruteForce(Point[] points, int a, int b) {

        if (b-a <= 1) return null;

        double minDistance = Double.MAX_VALUE;
        int selectedFirstIndex = 0, selectedSecondIndex = 0;

        // loop for first point of a pair
        for(int firstIndex = a; firstIndex < b; firstIndex++){

            // loop for second point of a pair
            for(int secondIndex = a; secondIndex < b; secondIndex++){

                // dont mind same points
                if(firstIndex == secondIndex) continue;;

                // get distance and check for new min
                double distance = points[firstIndex].getDistance(points[secondIndex]);
                if(distance < minDistance){
                    minDistance = distance;
                    selectedFirstIndex = firstIndex;
                    selectedSecondIndex = secondIndex;
                }
            }
        }

        PointPair result = new PointPair(points[selectedFirstIndex], points[selectedSecondIndex]);
        return result;
    }

    // Implementieren Sie hier Ihre Lösung für die Bestimmung des Pivotelements
    public double getPivotValue(Point[] points, int a, int b, String method, Random random) {

        if(b-a <= 1) return -1;

        // random
        else if(method.equals("Random")){
            return points[random.nextInt(b-a) + a].getX();
        }

        // first
        else if(method.equals("First")){
            return points[a].getX();
        }

        // median of three
        else {
            double xleft = points[a].getX();
            double xmiddle = points[(b-a-1) / 2 + a].getX();
            double xright = points[b-1].getX();

            double med = xleft <= xmiddle && xmiddle <= xright || xright <= xmiddle && xmiddle <= xleft ? xmiddle // left mid right || right mid left
                    : xmiddle <= xright && xright <= xleft || xleft <= xright && xright <= xmiddle ? xright // mid right left || left right mid
                    : xleft; // mid left right || right left mid
            return med;
        }
    }

    // Implementieren Sie hier Ihre Lösung für das Aufteilen des Arrays
    public int split(Point[] points, int a, int b, double pivot) {

        // invalid
        if(b-a <= 1) return -1;

        // create array with sections for smaller, equal and bigger elements
        Point[] divided = new Point[3 * b-a];
        int leftHead = 0, midHead = b-a, rightHead = 2 * (b-a);

        // sort elements in interval to either right, middle or left side in divided array
        for(int i = a; i < b; i++){

            double x = points[i].getX();
            if(x < pivot) divided[leftHead++] = points[i];
            else if(x == pivot) divided[midHead++] = points[i];
            else divided[rightHead++] = points[i];
        }

        // overwrite elements in original to divided order
        int insertIndex = a;
        for(int i = 0; i < divided.length; i++){
            if(divided[i] != null){
                points[insertIndex++] = divided[i];
            }
        }

        // return left head (smaller than pivot) which is 1 bigger than index of last left element (or add 1 if no elems were added left)
        return a + (leftHead == 0 ? leftHead + 1 : leftHead);
    }


    // Implementieren Sie hier Ihre Lösung für die Kombination zweier Teilprobleme
    public PointPair combination(Point[] points, int a, int b, double delta, int t, double L) {

        // init range with found index
        int minLeft = t;
        int maxRight = t;

        // find most left index in 2d range
        while(minLeft > a && points[minLeft - 1].getX() >= L - delta) minLeft--;

        // find first index bigger than 2d range
        while(++maxRight < b && points[maxRight].getX() <= L + delta);

        // there are less than two points in 2delta space
        if(maxRight - minLeft <= 1) return null;

        // sort points in 2delta space by y
        insertionSort(points, minLeft, maxRight, false);

        // loop through sorted y points and find nearest pair
        PointPair nearest = null;
        double minDistance = Double.MAX_VALUE;

        for(int i = minLeft; i < maxRight - 1; i++){
            double distance = points[i].getDistance(points[i+1]);
            if(distance < minDistance) {
                minDistance = distance;
                nearest = new PointPair(points[i], points[i+1], distance);
            }
        }

        return nearest;
    }

}
