package Manager;

import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public Map<Integer, Node> nodeInfo;
    public Node<Task> head;
    public Node<Task> tail;

    public InMemoryHistoryManager() {
        nodeInfo = new HashMap<>();
    }

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node<>(tail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        nodeInfo.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasksArray = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            tasksArray.add(node.data);
            node = node.next;
        }
        return tasksArray;
    }

    public void removeNode(Node node) {
        while (nodeInfo.values().remove(node)) ;
        if (node == tail) {
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


    @Override
    public void addTaskToHistory(Task task) {
        if (nodeInfo.containsKey(task.getId())) {
            removeNode(nodeInfo.get(task.getId()));
        }
        linkLast(task);
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
}
