## Problem
    Design kafka like system.

MessageRecord
- id: String
- key: String
- message: String
- timestamp: Date

Publisher
- Publisher()
+ <n>getInstance(): Publisher</n>
+ publish(topicName: String, record: MessageRecord): boolean

TopicManager
- topics: Map<String, Topic>
- TopicManager()
+ <n>getInstance(): TopicManager</n>
- getOrCreateTopic(topicName: String): Topic
+ addMessage(topicName: String, message: MessageRecord): void
+ getMessageAtOffset(topicName: String, offset: int): MessageRecord

Topic
- messages: List<MessageRecord>
+ isValidMessageOffset(offset: int): boolean
+ addMessage(message: MessageRecord): void
- getRandomMessageId(): String
+ getMessage(offset: int): MessageRecord

Subscriber
- Subscriber()
+ <n>getInstance(): Subscriber</n>
+ subscribe(topicName: String, subscriberId: String): void
+ poll(timeoutInMilliSeconds: long): MessageRecord
+ acknowledge(offset: int): boolean

TopicSubscriberHandler
- subscribers: Map<String, TopicSubscriber>
- TopicSubscriberHandler()
+ <n>getInstance(): TopicSubscriberHandler</n>
+ subscribe(subscriberId: String, topicName: String): void
+ poll(subscriberId: String, timeoutInMilliSeconds: long): MessageRecord
+ commit(subscriberId: String, offset: int): boolean
+ setOffset(offset: int): boolean

TopicSubscriber
- subscriberId: String
- topicName: String
- offset: int
+ setOffset(offset: int): boolean
+ poll(): MessageRecord

MessagePollerStrategyFactory
- messagePoller: Map<MessagePollerStrategyType, MessagePoller>
- MessagePollerStrategyFactory()
+ <n>getInstance(): MessagePollerStrategyFactory</n>
+ getMessagePoller(type: MessagePollerStrategyType): MessagePoller

MessagePollerStrategyType
+ DEFAULT
+ AUTO_ACKNOWLEDGE

<<MessagePoller>>
+ poll(timeoutInMilliSeconds: long): MessageRecord
+ acknowledge(offset: int): boolean

DefaultMessagePoller(MessagePoller)</br>
MessagePollerAutoAcknowledgeFacade(MessagePoller)
