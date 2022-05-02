package exceptions;

public class TaskStartTimeException extends RuntimeException{
    public TaskStartTimeException(String message) {
        super(message);
        System.out.println(message);
    }
}
