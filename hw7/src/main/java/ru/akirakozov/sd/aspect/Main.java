package ru.akirakozov.sd.aspect;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Random random = new Random(System.currentTimeMillis());
        AVLTreeSet<Integer> set = new AVLTreeSet<>(
                IntStream.range(0, 50)
                        .mapToObj(x -> random.nextInt(Integer.MAX_VALUE))
                        .collect(Collectors.toList()));
        System.out.println(set.contains(2));
        System.out.println(set.contains(3));
        System.out.println(set.contains(4));
        System.out.println(set.contains(5));
        System.out.println(set.first());
        System.out.println(set.last());
    }
}