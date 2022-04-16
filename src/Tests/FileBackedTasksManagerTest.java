package Tests;

import exceptions.ManagerSaveException;
import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    File file;

    @Override
    @BeforeEach
    public void initializeManager() {
        file = new File("./resources/data.csv");
        manager = new FileBackedTasksManager(file);
    }

    @Test
    void SaveAndRestoreEmpty() {
        manager.deleteAllTasks();
        manager.deleteAllSubtask();
        manager.deleteAllEpics();
        String data = "";
        try {
            data = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        assertEquals("id,type,name,status,description,epic,starttime,duration,endtime", data.trim(), "Неверное содержимое файла");
    }

    @Test
    void SaveAndRestoreEpic() {
        manager.deleteAllTasks();
        manager.deleteAllSubtask();
        manager.deleteAllEpics();

        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.getEpic(1);

        String data = "";
        try {
            data = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        String[] lines = data.split("\n");

        assertTrue(
                lines[0].equals("id,type,name,status,description,epic,starttime,duration,endtime")
                        && lines[1].equals("1,EPIC,Epic1,NEW,,,null,null,null")
                        && lines[2].equals("")
                        && lines[3].equals("1")
                , "Неверное содержимое файла.");

    }

    @Test
    void SaveAndRestoreHistoryIsEmpty() {
        manager.deleteAllTasks();
        manager.deleteAllSubtask();
        manager.deleteAllEpics();

        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);

        String data = "";
        try {
            data = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        String[] lines = data.split("\n");

        assertTrue(
                lines[0].equals("id,type,name,status,description,epic,starttime,duration,endtime")
                        && lines[1].equals("1,EPIC,Epic1,NEW,,,null,null,null")
                , "Неверное содержимое файла.");

    }
}
