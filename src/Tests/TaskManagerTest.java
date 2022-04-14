package Tests;

import manager.Managers;
import manager.Status;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    TaskManager manager;

    @BeforeEach
    public abstract void initializeManager();



}
