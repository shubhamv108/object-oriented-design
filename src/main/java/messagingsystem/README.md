## Problem
    Design Messaging system for event driven architecture.

MessageRecord
- id: String
- key: String
- message: String
- timestamp: Date

Publisher
- publish(record: MessageRecord): boolean

Topic
- messages: List<MessageRecord>
+ isValidOffset(offset: int)
+ getMessage(offset: int): MessageRecord

TopicManager
- topics: Map<String, Topic>
+ <n>getInstance(): TopicManager</n>
- getOrCreateTopic(topicName: String): Topic
+ getMessage(topicName: String, offset: int): MessageRecord

TopicSubscriber
- subscriberId: String
- topicName: String
- offset: int
+ setOffset(offset: int): void

TopicSubscriberHandler
- subscribers: Map<String, TopicSubscriber>
+ subscribe(subscriberId: String, topicName: String): TopicSubscriber
+ poll(subscriberId: String, timeoutInMilliSeconds: long): MessageRecord
+ commit(subscriberId: String, offset: int): boolean
+ setOffset(offset: int): void
+ <n>getInstance(): TopicSubscriberHandler</n>

Subscriber
- subscribe(topicName: String): String
- poll(timeoutInMilliSeconds: long): MessageRecord
- acknowledge(offset: int)
