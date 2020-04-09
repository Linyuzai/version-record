package com.github.linyuzai.versionrecord.core;

public class VersionRecordException extends RuntimeException {

    public VersionRecordException() {
    }

    public VersionRecordException(String message) {
        super(message);
    }

    public VersionRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionRecordException(Throwable cause) {
        super(cause);
    }

    public VersionRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
