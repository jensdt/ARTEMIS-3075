### Setup

- 2 live servers, configured to scale down to each other (static cluster)

### Set up testcase

1. Run both servers. On both broker instances, you should see the address 'activemq.notifications' has a multicast queue
notif.<uid>.ActiveMQServerImpl_serverUUID=<other-server-id>.

2. Run the "DebugCase" main method. This will create 50 consumers on server 2 (61617)

### Execute the testcase

3. Shutdown server 2. The server will shutdown, causing the 50 consumers to disconnect. This causes messages on "activemq.notifications", which is normal

4. Because of a timing issue (???), the consumer on this "notif." queue is closed, but the queue still exists. So any "activemq.notifications" posted on it, are still added to it.
In one test run, I had 13 messages still posted on this queue.

5. The scale down hanlder now comes into play and scales down these messages (e.g. "AMQ221078: Scaled down 13 messages total") to the other broker instance.
This causes the "notif." queue to be created on the first broker instance (local1, 61616)

### So now the problem is....

6. Any message arriving on 'activemq.notifications' on the first broker instance (local1, 61616) is multicast to this "notif." queue.
But there are no consumers on this queue.