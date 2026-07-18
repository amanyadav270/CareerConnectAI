package com.placement.model;

/**
 * Lifecycle status of a placement drive. Required by section 4.2 of the
 * problem statement ("use finite types such as ApplicationStatus and
 * DriveStatus rather than arbitrary strings") and used by FR-05 to filter
 * drive listings by status.
 */
public enum DriveStatus {
    OPEN,
    CLOSED
}
