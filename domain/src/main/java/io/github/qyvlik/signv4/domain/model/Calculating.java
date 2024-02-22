package io.github.qyvlik.signv4.domain.model;

/**
 * @param kDate    hash("AWS4" + Key, Date)
 * @param kRegion  hash(kDate, Region)
 * @param kService hash(kRegion, Service)
 * @param kSigning hash(kService, "aws4_request")
 */
public record Calculating(String kDate,
                          String kRegion,
                          String kService,
                          String kSigning) {
}
