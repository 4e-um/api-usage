package com.project.rdb.kafka.consumer;

// @Profile("!batch")
// @Slf4j
// @Component
// @ConditionalOnProperty(
//        name = "kafka.consumer.enabled",
//        havingValue = "true"
// )
// @RequiredArgsConstructor
// public class UsageEventConsumer {
//
//    private final ObjectMapper objectMapper;
//    private final UsageLogRepository usageLogRepository;
//
//    @KafkaListener(
//            id = "usage-log-consumer",
//            topics = "usage-data",
//            groupId = "usage-log-consumer",
//            containerFactory = "batchKafkaListenerContainerFactory"
//    )
//    public void consume(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
//        if (records == null || records.isEmpty()) {
//            ack.acknowledge();
//            return;
//        }
//
//        try {
//            for (ConsumerRecord<String, String> rec : records) {
//                UsageEventSchema e = objectMapper.readValue(rec.value(), UsageEventSchema.class);
//
//                UsageLog row = UsageLog.builder()
//                        .eventId(e.eventId())
//                        .subId(e.subscriptionId())
//                        .usedBytes(e.usageBytes())
//                        .eventTime(LocalDateTime.parse(e.timeStamp()))
//                        .build();
//
//                try {
//                    usageLogRepository.save(row);
//                } catch (DataIntegrityViolationException dup) {
//                    log.error("Duplicate record: {}", dup);
//                }
//            }
//
//            ack.acknowledge();
//        } catch (Exception ex) {
//            log.error("usage-log batch failed", ex);
//            throw new ApplicationException(GlobalErrorCode.USAGE_LOG_BATCH_FAILED);
//        }
//    }
// }
