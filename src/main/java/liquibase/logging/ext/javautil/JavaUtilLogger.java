package liquibase.logging.ext.javautil;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.logging.core.AbstractLogger;
import liquibase.logging.LogLevel;
import liquibase.logging.LogType;
import liquibase.util.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.*;

public class JavaUtilLogger extends AbstractLogger {

    private java.util.logging.Logger logger;

    private String changeLogName = null;
    private String changeSetName = null;

    public void setChangeLog(DatabaseChangeLog databaseChangeLog) {
        if (databaseChangeLog == null) {
            this.changeLogName = null;
        } else {
            this.changeLogName = databaseChangeLog.getFilePath();
        }
    }

    public void setChangeSet(ChangeSet changeSet) {
        if (changeSet == null) {
            this.changeSetName = null;
        } else {
            this.changeSetName = changeSet.toString(false);
        }
    }

    public void setName(String name) {
        logger = java.util.logging.Logger.getLogger(name);
    }

    public void setLogLevel(LogLevel logLevel) {
        if (logLevel == LogLevel.DEBUG) {
            logger.setLevel(Level.ALL);
        } else if (logLevel == LogLevel.INFO) {
            logger.setLevel(Level.INFO);
        } else if (logLevel == LogLevel.WARNING) {
            logger.setLevel(Level.WARNING);
        } else if (logLevel == LogLevel.SEVERE) {
            logger.setLevel(Level.SEVERE);
        } else if (logLevel == LogLevel.OFF) {
            logger.setLevel(Level.OFF);
        } else {
            throw new UnexpectedLiquibaseException("Unknown log level: " + logLevel);
        }
    }


    public void setLogLevel(String logLevel, String logFile) {
        Handler fH;

        try {
            fH = new FileHandler(logFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot open log file " + logFile + ". Reason: " + e.getMessage());
        }

        fH.setFormatter(new Formatter(){
            public String format(LogRecord record) {
                return record.getLoggerName()+":"+record.getLevel().getName()+": "+record.getMessage();
            }
        });
        logger.addHandler(fH);
        logger.setUseParentHandlers(false);
        setLogLevel(logLevel);

    }

    private void setLogLevel(String logLevel) {

    	logger.setUseParentHandlers(false);
        setLogLevel(logLevel);

	}

	public void severe(LogType target, String message) {
        logger.severe(message);
    }

    public void severe(LogType target, String message, Throwable e) {
        logger.log(Level.SEVERE, message, e);
    }

    public void warning(LogType target, String message) {
        logger.warning(message);
    }

    public void warning(LogType target, String message, Throwable e) {
        logger.log(Level.WARNING, message, e);
    }

    public void info(LogType target, String message) {
        logger.info(message);
    }

    public void info(LogType target, String message, Throwable e) {
        logger.log(Level.INFO, message, e);
    }

    public void debug(LogType target, String message) {
        logger.finest(message);

    }

    public void debug(LogType target, String message, Throwable e) {
        logger.log(Level.FINEST, message, e);
    }

    public Handler[] getHandlers() {
        return logger.getHandlers();
    }

    public void removeHandler(Handler handler) {
        logger.removeHandler(handler);
    }

    public void addHandler(Handler handler) {
        logger.addHandler(handler);
    }

    public void setUseParentHandlers(boolean b) {
        logger.setUseParentHandlers(b);
    }

    protected String formatMessage(String message) {
        if (StringUtils.trimToNull(message) == null) {
            return null;
        }

        String outMessage = "";
        if (changeLogName != null) {
            outMessage += changeLogName+"::";
        }
        if (changeSetName != null) {
            outMessage += changeSetName.replace(changeLogName+"::", "")+"::";
        }
        outMessage += message;

        return message;
    }
}
