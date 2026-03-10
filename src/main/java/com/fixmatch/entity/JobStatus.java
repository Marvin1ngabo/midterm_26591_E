package com.fixmatch.entity;

/**
 * JobStatus Enum - Defines the status of a job
 * 
 * - OPEN: Job is posted and waiting for providers
 * - IN_PROGRESS: Provider has accepted and is working on the job
 * - COMPLETED: Job is finished successfully
 * - CANCELLED: Job was cancelled by client or provider
 * - PAUSED: Job is temporarily paused
 * - DISPUTED: Job has a dispute between client and provider
 * - PENDING_PAYMENT: Job completed but payment is pending
 * - EXPIRED: Job posting has expired without being accepted
 */
public enum JobStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    PAUSED,
    DISPUTED,
    PENDING_PAYMENT,
    EXPIRED
}
