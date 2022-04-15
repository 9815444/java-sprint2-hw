package Tests;

import manager.InMemoryTaskManager;
import manager.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    @BeforeEach
    public void initializeManager() {
        manager = new InMemoryTaskManager();
    }

}
