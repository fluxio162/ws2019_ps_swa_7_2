scan("35 seconds") // Scan for file changes every 35 seconds
def LOG_PATH = "logs"
def LOG_ARCHIVE = "${LOG_PATH}/archive"

appender("Console-Appender", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%cyan(%date{yyyy-MM-dd HH:mm:ss.SSS}) %green(%-5level) %message in %yellow(%F:%L %n)"
    }
}

appender("RollingFile-Appender", RollingFileAppender) {
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${LOG_ARCHIVE}/log_%d{yyyy-MM-dd}.log"
        maxHistory = 30
        totalSizeCap = "50MB"
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%date{HH:mm:ss.SSS} %-5level [%t] %message in %F:%L %n"
        outputPatternAsHeader = true
    }
}

root(INFO, ["Console-Appender", "RollingFile-Appender"])