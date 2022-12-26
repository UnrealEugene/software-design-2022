package ru.akirakozov.sd.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public aspect ProfilerAspect {
    private final Map<String, Long> methodSpentTime = new HashMap<>();

    private final Map<String, Integer> methodCallCount = new HashMap<>();

    private Node currentParent = new Node(null);

    pointcut privateMethodExecuted():
            execution(* *(..)) && within(@ru.akirakozov.sd.aspect.Profile *);

    private String getMethodString(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        return methodSignature.toShortString();
    }

    private boolean getClassShowTree(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getStaticPart()
                .getSignature()
                .getDeclaringType();
        Profile profile = clazz.getAnnotation(Profile.class);
        return profile.showTree();
    }

    Object around(): privateMethodExecuted() {
        String methodName = getMethodString(thisJoinPoint);
        boolean showTree = getClassShowTree(thisJoinPoint);
        Node oldParent = currentParent;
        Node node = new Node(methodName);
        if (showTree) {
            currentParent.addChild(node);
            currentParent = node;
        }

        long timeNs = System.nanoTime();
        Object result = proceed();
        long timeElapsedNs = System.nanoTime() - timeNs;

        methodSpentTime.merge(methodName, timeElapsedNs, Long::sum);
        methodCallCount.merge(methodName, 1, Integer::sum);
        if (showTree) {
            node.setTimeElapsed(timeElapsedNs);
            currentParent = oldParent;
        }
        return result;
    }

    pointcut mainMethod(): execution(public static void main(..));

    private void printMethodCallTree(Node node, int depth, List<Boolean> isLastAtDepthList) {
        for (int i = 0; i < isLastAtDepthList.size() - 1; i++) {
            System.out.print(isLastAtDepthList.get(i) ? "   " : "|  ");
        }
        if (depth > 0) {
            System.out.printf("+- %s: %d ns%n", node.getMethodName(), node.getTimeElapsed());
        }

        List<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            isLastAtDepthList.add(i == children.size() - 1);
            printMethodCallTree(child, depth + 1, isLastAtDepthList);
            isLastAtDepthList.remove(isLastAtDepthList.size() - 1);
        }
    }

    after(): mainMethod() {
        if (!currentParent.getChildren().isEmpty()) {
            System.out.println();
            System.out.println("Method call tree:");
            printMethodCallTree(currentParent, 0, new ArrayList<>());
        }

        if (!methodCallCount.isEmpty()) {
            System.out.println("Method profile summary: ");
            List<String> methodOrder = methodCallCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            for (String methodName : methodOrder) {
                int callCount = methodCallCount.get(methodName);
                long timeElapsed = methodSpentTime.get(methodName);
                System.out.printf("* %s: %d call(s), sum %d ns, avg %d ns%n",
                        methodName, callCount, timeElapsed, timeElapsed / callCount);
            }
        }
    }

    private static class Node {
        final String methodName;
        long timeElapsed;
        final List<Node> children;

        private Node(String methodName) {
            this.methodName = methodName;
            this.children = new ArrayList<>();
        }

        void setTimeElapsed(long timeElapsed) {
            this.timeElapsed = timeElapsed;
        }

        void addChild(Node node) {
            children.add(node);
        }

        String getMethodName() {
            return methodName;
        }

        long getTimeElapsed() {
            return timeElapsed;
        }

        List<Node> getChildren() {
            return children;
        }
    }
}