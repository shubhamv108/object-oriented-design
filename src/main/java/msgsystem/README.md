Design messaging system for event driven architecture

Message
- content: String

Publisher
- subscribers: List<Subscriber>
+ publish(message: Message)

Subscriber
+ subscribe(publisher: Publisher): List<Message>
+ notify(message: Message)