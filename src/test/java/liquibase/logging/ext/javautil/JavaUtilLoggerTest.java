package liquibase.logging.ext.javautil;

import org.junit.Test;

import static org.junit.Assert.*;

public class JavaUtilLoggerTest {

    @Test
    public void getPriority() {
        assertTrue(new JavaUtilLogger().getPriority() > 1);
    }

}