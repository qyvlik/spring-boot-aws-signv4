package io.github.qyvlik.domain;

/**
 * 错误码
 */
public interface Code {
    long SUCCESS = 200L;

    long BAD_REQUEST = 400L;

    long UNAUTHORIZED = 401L;

    long FORBIDDEN = 403L;

    long NOT_FOUND = 404L;

    long CONFLICT = 409L;

    long TOO_MANY_REQUESTS = 429;

    long ERROR = 500L;
}
