package com.log.filelogger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.android.LogcatAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize
import com.google.gson.Gson
import com.log.filelogger.FileLogConstants.DEFAULT_FILE_INDEXING
import com.log.filelogger.FileLogConstants.DEFAULT_ROLLING_POLICY_HOUR
import com.log.filelogger.FileLogConstants.DEFAULT_ROLLING_POLICY_MB
import com.log.filelogger.FileLogConstants.DEFAULT_ROLLING_POLICY_OVERALL_GB
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object FileLogger {
    var pathToLog: String? = null
    private var logger: Logger? = null
    private var rollingPolicyHour: Int = DEFAULT_ROLLING_POLICY_HOUR
    private var rollingPolicyMB: Int = DEFAULT_ROLLING_POLICY_MB
    private var rollingPolicyOverallGB: Int = DEFAULT_ROLLING_POLICY_OVERALL_GB
    private var defaultFileIndexing: String = DEFAULT_FILE_INDEXING
    private var logBackName = "FileLog"

    fun d(message: String? = null, vararg args: Any?) {
        if (!TextUtils.isEmpty(pathToLog)) {
            log(priority = Logger.DEBUG_INT, message = concatVarArgsToMessage(message, args))
        }
    }

    fun i(message: String? = null, vararg args: Any?) {
        if (!TextUtils.isEmpty(pathToLog)) {
            log(priority = Logger.INFO_INT, message = concatVarArgsToMessage(message, args))
        }
    }

    fun w(message: String? = null, vararg args: Any?) {
        if (!TextUtils.isEmpty(pathToLog)) {
            log(priority = Logger.WARN_INT, message = concatVarArgsToMessage(message, args))
        }
    }

    fun e(message: String? = null, throwable: Throwable? = null, vararg args: Any?) {
        if (!TextUtils.isEmpty(pathToLog)) {
            log(priority = Logger.ERROR_INT, throwable = throwable, message = concatVarArgsToMessage(message, args))
        }
    }

    private fun concatVarArgsToMessage(message: String?, vararg args: Any?): String {
        var resultString = ""
        message?.let {
            resultString = message
            for (x in args) {
                resultString + "\n" + Gson().toJson(x)
            }
        } ?: let {
            for (x in args) {
                resultString + "\n" + Gson().toJson(x)
            }
        }
        return resultString
    }

    fun log(
        priority: Int,
        throwable: Throwable? = null,
        message: String
    ) {
        try {
            if (logger == null) {
                logger = createLogger()
            }
            logger?.log(null, Logger.FQCN, priority, message, null, throwable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun initFileLogger(
        pathToLog: String?,
        rollingPolicyHour: Int = DEFAULT_ROLLING_POLICY_HOUR,
        rollingPolicyMB: Int = DEFAULT_ROLLING_POLICY_MB,
        rollingPolicyOverallGB: Int = DEFAULT_ROLLING_POLICY_OVERALL_GB,
        defaultFileIndexing: String = DEFAULT_FILE_INDEXING,
    ) {
        this.pathToLog = pathToLog
        this.rollingPolicyHour = rollingPolicyHour
        this.rollingPolicyMB = rollingPolicyMB
        this.rollingPolicyOverallGB = rollingPolicyOverallGB
        this.defaultFileIndexing = defaultFileIndexing
        logger = createLogger()
    }

    private fun createLogger(): Logger? {
        if (TextUtils.isEmpty(pathToLog)) {
            return null
        }
        return confFileLogger()
    }

    private fun confFileLogger(): Logger {
        val context: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        context.reset()

        val rootLogger = LoggerFactory.getLogger(logBackName) as Logger
        rootLogger.level = ch.qos.logback.classic.Level.ALL

        val fileAppender: RollingFileAppender<ILoggingEvent> = createFileAppenderWithRollingPolicy(
            context,
            pathToLog
        )
        val logcatAppender = createLogcatAppender(context)
        rootLogger.addAppender(fileAppender)
        rootLogger.addAppender(logcatAppender)
        return rootLogger
    }

    private fun createFileAppenderWithRollingPolicy(_context: LoggerContext, _filePath: String?): RollingFileAppender<ILoggingEvent> {
        val rollingFileAppender = createRollingAppender(_context)
        val rollingFilePolicy = createRollingFilePolicy(_context, rollingFileAppender, _filePath)
        rollingFileAppender.rollingPolicy = rollingFilePolicy
        val encoder = PatternLayoutEncoder()
        encoder.pattern = "%date{ISO8601} %-5level %msg%n"
        encoder.context = _context
        encoder.start()
        rollingFileAppender.encoder = encoder
        rollingFileAppender.name = "rollingFileAppender"
        rollingFileAppender.start()
        return rollingFileAppender
    }
    private fun createRollingAppender(_context: LoggerContext): RollingFileAppender<ILoggingEvent> {
        val rollingFileAppender = RollingFileAppender<ILoggingEvent>()
        rollingFileAppender.isAppend = true
        rollingFileAppender.context = _context
        return rollingFileAppender
    }
    private fun createRollingFilePolicy(_context: LoggerContext, rollingFileAppender: RollingFileAppender<ILoggingEvent>, _filePath: String?): SizeAndTimeBasedRollingPolicy<ILoggingEvent> {
        val rollingPolicy = SizeAndTimeBasedRollingPolicy<ILoggingEvent>()
        rollingPolicy.fileNamePattern = "$_filePath/$defaultFileIndexing.txt"
        rollingPolicy.maxHistory = rollingPolicyHour
        rollingPolicy.setMaxFileSize(FileSize(FileSize.MB_COEFFICIENT * rollingPolicyMB))
        rollingPolicy.setTotalSizeCap(FileSize(FileSize.GB_COEFFICIENT * rollingPolicyOverallGB))
        rollingPolicy.setParent(rollingFileAppender)
        rollingPolicy.isCleanHistoryOnStart = true
        rollingPolicy.context = _context
        rollingPolicy.start()
        return rollingPolicy
    }
    private fun createLogcatAppender(_context: LoggerContext): LogcatAppender {
        val encoder = PatternLayoutEncoder()
        encoder.context = _context
        encoder.pattern = "%-5level %msg%n"
        encoder.start()
        val logcatAppender = LogcatAppender()
        logcatAppender.context = _context
        logcatAppender.encoder = encoder
        logcatAppender.start()
        return logcatAppender
    }

    fun getLogsList():ArrayList<String>{
        val fileList = ArrayList<String>()
        pathToLog?.let {
            val directory: File = File(pathToLog)
            val logListFiles = directory.listFiles()
            logListFiles?.let { listFiles ->
                for (file in listFiles){
                    fileList.add(file.name)
                }
            }
            Collections.sort(fileList,Collections.reverseOrder())
        }
        return fileList
    }

    fun sendMail(activity: Activity,uri:Uri,info:String?){
        try {
            val path = uri
            val emailIntent =  Intent(Intent.ACTION_SEND)

            // set the type to 'email'
            emailIntent.type = "vnd.android.cursor.dir/email"
            val to = arrayOf("bbasogluceng@gmail.com")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
            // the attachment
            emailIntent.putExtra(Intent.EXTRA_STREAM, path)
            // the mail subject
            emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Local Log : " + getTime()
            )
            // mail text
            emailIntent.putExtra(Intent.EXTRA_TEXT, info)
            activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun getTime(): String? {
        val sdf = SimpleDateFormat("yyyy.MM.dd - HH:mm:ss z", Locale.getDefault())
        return sdf.format(Date())
    }
}
