package tests;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    @BeforeEach
    public void initializeManager() {
        manager = new InMemoryTaskManager();
    }

}
