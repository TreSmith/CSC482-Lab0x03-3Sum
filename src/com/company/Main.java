package com.company;

import java.util.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Main {

    public static void main(String[] args) {
	// write your code here
        int N=20;
        int[] list = generateList(N);

        List<List<Integer>> threeSumBrute = threeSumBrute(list);
        printList(threeSumBrute);

        List<List<Integer>> threeSumFast = threeSumFaster(list);
        printList(threeSumFast);

        List<List<Integer>> threeSumFastest = threeSumFastest(list);
        printList(threeSumFastest);

        verificationTest(2, 4096);
    }
    //=============================
    //3 Sum Problems
    //=============================

    static public List<List<Integer>> threeSumBrute(int[] list) {
        Arrays.sort(list);
        List<List<Integer>> threeSums = new LinkedList<>();

        int a,b,c;

        for(int i=0; i<list.length-2; i++) {
            a = list[i];
            if(i == 0 || list[i] != list[i-1]) {
                for (int j = i + 1; j < list.length - 1; j++) {
                    b = list[j];
                    if(list[j] != list[j-1]) {
                        for (int x = j + 1; x < list.length; x++) {
                            c = list[x];
                            if ((list[x] != list[x - 1])) {
                                if (a + b + c == 0) {
                                    threeSums.add(Arrays.asList(a, b, c));
                                }

                            }
                        }
                    }
                }
            }
        }

        return threeSums;
    }

    static public List<List<Integer>> threeSumFaster(int[] list) {
        Arrays.sort(list);
        List<List<Integer>> threeSums = new LinkedList<>();

        for(int i=0; i<list.length-2; i++) {
            if(i == 0 || list[i] != list[i - 1]) {
                int start = i+1, end = list.length-1, sum= -list[i];
                while(start<end)
                {
                    if(list[start] + list[end] == sum) {
                        threeSums.add(Arrays.asList(list[i], list[start], list[end]));
                        while (start < end && list[start] == list[start + 1])
                            start++;
                        while (start < end && list[end] == list[end - 1])
                            end--;
                        start++;
                        end--;
                    }
                    else if(list[start] + list[end] > sum)
                        end--;
                    else
                        start++;
                }
            }

        }
        return threeSums;
    }

    static public List<List<Integer>> threeSumFastest(int[] list) {
        Arrays.sort(list);
        List<List<Integer>> threeSums = new LinkedList<>();
        int start, end;

        for(int i=0; i<list.length-2; i++) {
            start = i + 1;
            end = list.length - 1;
            while(start < end) {
                if((list[i] + list[start] + list[end]) == 0)
                {
                    threeSums.add(Arrays.asList(list[i], list[start], list[end]));
                    start++;
                    end--;
                }
                else if((list[i] + list[start] + list[end]) > 0) //list[i] list[start] list[end]
                    end--;
                else
                    start++;
            }
        }
        return threeSums;
    }

    //=============================
    //Test Methods
    //=============================

    static public void verificationTest(int minN, int maxN) {
        long beforeTime, afterTime;
        long bruteTime, fastTime, fastestTime;
        long beforeBruteTime, beforeFastTime, beforeFastestTime;
        String brutePrefix, fastPrefix, fastestPrefix;
        String doRa = "Doubling Ratios", exDoRa = "Expected Doubling Ratios", time = "Time";
        double bruteRatio, fastRatio, fastestRatio;
        double cbruteTime, cfastTime, cfastestTime;

        beforeBruteTime = -1;
        beforeFastTime =  -1;
        beforeFastestTime = -1;

        System.out.printf("%50s %65s %65s\n", "Brute 3Sum", "Fast 3Sum", "Fastest 3Sum");
        System.out.printf("%-5s %13s %25s %25s %13s %25s %25s %13s %25s %25s\n", "N", time, doRa, exDoRa, time, doRa, exDoRa, time, doRa, exDoRa);

        for(int N = minN; N <= maxN; N=N*2) {
            List<List<Integer>> bruteList;
            List<List<Integer>> fastList;
            List<List<Integer>> fastestList;

            int[] list = generateList(N);


            //==========Brute==============

            beforeTime = getCpuTime();
            bruteList = threeSumBrute(list);
            afterTime =getCpuTime();

            bruteTime = afterTime - beforeTime;

            brutePrefix = getSecondType(bruteTime);
            cbruteTime = convertNanoSeconds(bruteTime);

            bruteRatio =  (double)(bruteTime / beforeBruteTime);

            //===========Fast================


            beforeTime = getCpuTime();
            fastList = threeSumFaster(list);
            afterTime =getCpuTime();

            fastTime = afterTime - beforeTime;

            fastPrefix = getSecondType(fastTime);
            cfastTime = convertNanoSeconds(fastTime);

            fastRatio = (double)(fastTime/beforeFastTime);

            //=============Fastest============

            beforeTime = getCpuTime();
            fastestList = threeSumFastest(list);
            afterTime = getCpuTime();

            fastestTime = afterTime - beforeTime;

            fastestPrefix = getSecondType(fastestTime);
            cfastestTime = convertNanoSeconds(fastestTime);

            fastestRatio = (double)(fastestTime/beforeFastestTime);

            if(beforeBruteTime != -1 || beforeFastTime != -1 || beforeFastestTime != -1) {
                double expexB, expexFs, expexFst; //expected doubling rates
                expexB = (double)(N*N*N)/(double)((N/2)*(N/2)*(N/2));
                expexFs = (double)(N*N*Math.log(N))/(double)((N/2)*(N/2)*Math.log((double)N/2));
                expexFst = (double)(N*N)/(double)((N/2)*(N/2));

                System.out.printf("%-5d %10.0f %-3s %25.3f %25.3f %10.0f %-3s %25.3f %15.3f %20.0f %-3s %25.3f %25.3f\n", N, cbruteTime, brutePrefix, bruteRatio, expexB, cfastTime, fastPrefix, fastRatio, expexFs, cfastestTime, fastestPrefix, fastestRatio, expexFst);


            }

            beforeBruteTime = bruteTime;
            beforeFastTime = fastTime;
            beforeFastestTime = fastestTime;

        }
    }

    //=============================
    //Utility Methods
    //=============================

    static public int[] generateList(int N) {
        int[] list = new int[N];

        for(int x = 0; x < N; x++)
        {
            list[x] = (int) (Math.random() * (2 * N + 2 * N)) -2*N;
        }
        return list;
    }

    static public void printList(List<List<Integer>> printArray) {
        int x;
        System.out.print("List Print\n");
        for(x=0; x<printArray.size(); x++)
             System.out.print(printArray.get(x)+"\n");
        System.out.print("\n");
    }

    public static String getSecondType (long time) {
        if((time/1000000000) > 60)
            return "m ";
        if(time >= 1000000000)
            return "s ";
        else if(time >= 1000000)
            return "µs";
        else if(time >= 1000)
            return "ms";
        else
            return "ns";
    }

    public static long convertNanoSeconds (long time) {
        long convertedTime=0;

        if(time >= 1000000000) {
            convertedTime = time / 1000000000;
            if(convertedTime>=60)   //Check if its a minute or more
                convertedTime = convertedTime/60;
        }
        else if(time >= 1000000)
            convertedTime = time / 1000000;
        else if(time >= 1000)
            convertedTime = time / 1000;
        else
            convertedTime = time;

        return convertedTime;
    }

    /* Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime( ) {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadCpuTime( ) : 0L;

    }

}
