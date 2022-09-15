package Manager;

import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node<Task>> nodeInfo;
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        nodeInfo = new HashMap<>();
    }

    public static class Node<Task> {

        private Task data;
        private Node<Task> next;
        private Node<Task> prev;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

    }

    @Override
    public void addTaskToHistory(Task task) {
        try {
            //Проверка на перезапись единственной одинаковой задачи
            if (nodeInfo.size() == 1 && nodeInfo.containsKey(task.getId())) {
                return;
            } else if (nodeInfo.containsKey(task.getId())) {
                removeNode(nodeInfo.get(task.getId()));
            } else {
                linkLast(task);
            }
        } catch (NullPointerException e){
            System.out.println("Не удалось получить номер задачи для добавления в историю");
        }

    }

    @Override
    public void removeTaskFromHistory(int id) {
        removeNode(nodeInfo.get(id));
        nodeInfo.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        nodeInfo.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> tasksArray = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            tasksArray.add(node.data);
            node = node.next;
        }
        return tasksArray;
    }

    private void removeNode(Node<Task> node) {
        if (node != null){
            nodeInfo.values().remove(node);
            if (node.prev == null && node.next == null) {
                head = null;
            } else if (node == tail) {
                tail = node.prev;
                tail.next = null;
            } else if (node == head) {
                head = node.next;
                head.prev = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        }
    }

}
