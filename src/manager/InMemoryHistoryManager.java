package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    private LinkedList<Node<Task>> taskHistory;
    private HashMap<Integer, Node<Task>> receivedTasks;

    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;

    public InMemoryHistoryManager() {
        this.taskHistory = new LinkedList<>();
        this.receivedTasks = new HashMap<>();

    }

    public void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<Task>(oldTail, element, null);
        tail = newNode;
        receivedTasks.put(element.getId(), newNode);
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> currentNode = head;
        while (!(currentNode == null)) {
            tasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    public void removeNode(Node<Task> node) {
        if (!(node == null)) {
            final Task data = node.data;
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;
            node.data = null;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && !(tail == node)) {
                head = next;
                head.prev = null;
            } else if (!(head == node) && tail == node) {
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }

            node = null;
            size--;

        }
    }

    @Override
    public void add(Task task) {
        if (!(task == null)) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(receivedTasks.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}

class Node<Task> {

    public Task data;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
