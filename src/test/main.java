package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;


public class main {

    public static Vector<Process> sortedP(Vector<Process> v) {
        Vector<Process> tmp = new Vector<>();
        v.sort(new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                if (o1.startTime > o2.startTime) {
                    return 1;
                } else if (o1.startTime < o2.startTime) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        for (Process p : v) {
            tmp.add(new Process(p.startTime, p.runTime));
        }
        return tmp;
    }

    public static double meanTurnAroundFCFS(Vector<Process> v) {
        Vector<Process> sortedPriority = sortedP(v);
        double sum = 0;
        int time = 0;
        int currTime = 0;
        for (Process p : sortedPriority) {
            if (p.startTime < time) { // wait
                sum += time - p.startTime + p.runTime + 1;
            } else { // not waiting
                sum += p.runTime;
            }
            time = currTime + p.runTime;
            currTime = time;
        }
        return sum / v.size();
    }

    public static int maxRunTime(Vector<Process> v, int time) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i).startTime > max && v.get(i).startTime <= time) {
                max = v.get(i).startTime;
                index = i;
            }
        }
        return index;
    }

    public static double meanTurnAroundLCFSNP(Vector<Process> v) {
        Vector<Process> sortedPriority = sortedP(v);
        int currTime = sortedPriority.get(0).runTime + sortedPriority.get(0).startTime - 1;
        double sum = sortedPriority.get(0).runTime;
        sortedPriority.remove(0);
        while (!sortedPriority.isEmpty()) {
            int index = maxRunTime(sortedPriority, currTime);
            if (sortedPriority.get(index).startTime < currTime) {
                sum += currTime - sortedPriority.get(index).startTime + sortedPriority.get(index).runTime + 1;
            } else {
                sum += sortedPriority.get(index).runTime;
            }
            currTime += sortedPriority.get(index).runTime;
            sortedPriority.remove(index);
        }
        return sum / v.size();
    }

//    public static double meanTurnAroundLCFS(Vector<Process> v) {
//        Vector<Process> sortedPriority = sortedP(v);
//
//    }

    public static double meanTurnAroundRR(Vector<Process> v) {
        int size = v.size();
        Vector<Process> sortedPriority = sortedP(v);
        int timeCounter = 0;
        double sum = 0;
        while (!sortedPriority.isEmpty()) {
            for (int i = 0; i < sortedPriority.size(); i++) {
                timeCounter++;
                Process p = sortedPriority.get(i);
                if (p.startTime <= timeCounter) {
                    p.runTime--;
                    if (p.runTime == 0) {
                        sortedPriority.remove(i);
                        sum += timeCounter - p.startTime;
                    } else {
                        p.runTime--;
                        timeCounter++;
                        if (p.runTime == 0) {
                            sortedPriority.remove(i);
                            sum += timeCounter - p.startTime;
                        }
                    }
                }
            }
        }
        return sum / size;
    }

    public static Vector<Process> sortedPbyRunTime(Vector<Process> v) {
        Vector<Process> tmp = new Vector<>();
        v.sort(new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                if (o1.runTime > o2.runTime) {
                    return 1;
                } else if (o1.runTime < o2.runTime) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        for (Process p : v) {
            tmp.add(new Process(p.startTime, p.runTime));
        }
        return tmp;
    }

    public static int minRunTime(Vector<Process> v, int time) {
        int min = 0;
        int index = 0;
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i).runTime < min && v.get(i).startTime <= time) {
                min = v.get(i).runTime;
                index = i;
            }
        }
        return index;
    }

    public static double meanTurnAroundSJF(Vector<Process> v) {
        Vector<Process> sortedPriority = sortedP(v);
        int currTime = sortedPriority.get(0).startTime;
        int prev = 0;
        double sum = 0;
        while (!sortedPriority.isEmpty()) {
            int index = minRunTime(sortedPriority, currTime);
            if (index != prev) {
                prev = index;
            }
            sortedPriority.get(prev).runTime--;
            currTime++;
            if (sortedPriority.get(prev).runTime == 0) {
                sortedPriority.remove(prev);
            }
        }
        return sum / v.size();
    }

    public static void main(String[] args) {
        Vector<Process> processVector = new Vector<>();
        try {
            File myObj = new File("C:\\Users\\roey\\IdeaProjects\\OSmatala3\\src\\test\\input1.txt");
            Scanner myReader = new Scanner(myObj);
            String data = myReader.nextLine();
            int number = Integer.parseInt(data);
            System.out.println("Number of processes is = " + number);
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();
                String[] tokens = row.split(",");
                int runTime = Integer.parseInt(tokens[0]);
                int startTime = Integer.parseInt(tokens[1]);
                Process tmp = new Process(runTime, startTime);
                processVector.add(tmp);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("FCFS = " + meanTurnAroundFCFS(processVector));
        System.out.println("LCFS = " + meanTurnAroundLCFSNP(processVector));
        System.out.println("RR = " + meanTurnAroundRR(processVector));
    }
}


