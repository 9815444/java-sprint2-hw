package manager;

import java.io.File;
import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() {
//        return new InMemoryTaskManager();
//        return new FileBackedTasksManager(new File("./resources/data.csv"));
        return new HTTPTaskManager("http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
