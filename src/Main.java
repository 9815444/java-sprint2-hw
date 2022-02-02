public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        //epics
        Epic epic1 = new Epic("Epic №1", "");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", "DONE", epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", "IN_PROGRESS", epic1);
        manager.addSubtask(subtask12);

        Epic epic2 = new Epic("Epic №2", "");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Epic2 Subtask1", "", "DONE", epic2);
        manager.addSubtask(subtask21);

        System.out.println("epics = " + manager.getEpics());
        System.out.println("subtasks = " + manager.getSubtasks());

        Subtask subtask12New = new Subtask("Epic1 Subtask2 modified", "", "NEW", epic1);
        subtask12New.setId(3);
        manager.updateSubtask(subtask12New);

        System.out.println("epics = " + manager.getEpics());
        System.out.println("subtasks = " + manager.getSubtasks());

        manager.deleteEpic(4);
        System.out.println("epics = " + manager.getEpics());
        System.out.println("subtasks = " + manager.getSubtasks());

        manager.deleteSubtask(2);
        System.out.println("epics = " + manager.getEpics());
        System.out.println("subtasks = " + manager.getSubtasks());

    }
}
