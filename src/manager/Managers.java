package manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault(){
//        return new InMemoryTaskManager();
        return new FileBackedTasksManager(new File("./resources/data.csv"));
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
