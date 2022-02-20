package com.log.filelogger

object FileLogConstants {
    const val DEFAULT_ROLLING_POLICY_HOUR = 24
    const val DEFAULT_ROLLING_POLICY_MB = 24
    const val DEFAULT_ROLLING_POLICY_OVERALL_GB = 2
    const val DEFAULT_FILE_INDEXING = "log-time-%d{yyyy-MM-dd_HH}-index-%i"
}
