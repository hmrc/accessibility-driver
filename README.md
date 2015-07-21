Accessibility Driver
====================

A selenium webdriver implementation which will produce an HTML accessibility report using
[google accessibility developer tools](https://github.com/GoogleChrome/accessibility-developer-tools)

Requirements
------------

PhantomJS must be installed, e.g.

    brew install phantomjs
    
    (add linux instructions)
    
Usage
-----

    val driver = new AccessibilityDriver
    
    (run tests)
    
    driver.quit //The quit command triggers the production of the report, make sure you quit the driver after testing

Reports
-------

The reports produced will end up in

    target/accessibility-reports/[timestamp]/index.html

Packaging
---------

To package for scala versions 2.10 and 2.11 (as defined in build.sbt), use a +!

    sbt
    > + package
    > + publish
    > + publishLocal
    
Examples of possible commands prefixed with +

Logging
-------

To enable logging of the underlying proxy server, use the following example config in 'resources/log4j.xml'

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
    <log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
        <appender class="org.apache.log4j.RollingFileAppender" name="RollingTextFile">
            <param value="log.txt" name="File"/>
            <param value="5" name="MaxBackupIndex"/>
            <param value="50MB" name="MaxFileSize"/>
            <layout class="org.apache.log4j.PatternLayout"/>
        </appender>
        <appender class="org.apache.log4j.ConsoleAppender" name="stdout">
            <layout class="org.apache.log4j.PatternLayout">
                <param value="%-6r %d{ISO8601} %-5p [%t] %c{2} (%F:%L).%M() - %m%n" name="ConversionPattern"/>
            </layout>
        </appender>
        <appender class="org.apache.log4j.FileAppender" name="TextFile">
            <param value="false" name="Append"/>
            <param value="log.txt" name="File"/>
            <layout class="org.apache.log4j.PatternLayout">
                <param value="%-6r %d{ISO8601} %-5p [%t] %c{2} (%F:%L).%M() - %m%n" name="ConversionPattern"/>
            </layout>
        </appender>
        <appender name="ASYNC" class="org.apache.log4j.AsyncAppender">
            <param name="BufferSize" value="500"/>
            <appender-ref ref="TextFile"/>
            <appender-ref ref="stdout"/>
        </appender>
        <logger name="org.eclipse.jetty">
            <level value="off"/>
        </logger>
        <logger name="org.littleshoot.proxy">
            <level value="info"/>
        </logger>
        <root>
            <level value="info"/>
            <appender-ref ref="ASYNC"/>
        </root>
    </log4j:configuration>
    
Suggested Future Improvements
-----------------------------


 * Support for other delegate drivers (currently hardcoded to use FirefoxDriver)
 * Some tests
 * Parsing of reports so we can get some metrics e.g. number of warnings
 * Dont run reports on the same page twice (check the page md5 hash)
 * Support for compressed responses
