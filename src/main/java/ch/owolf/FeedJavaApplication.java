package ch.owolf;

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;


@SpringBootApplication
public class FeedJavaApplication {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "rss-inbound.xml");
        try {
            PollableChannel feedChannel = context.getBean("articleChannel", PollableChannel.class);
            for (int i = 0; i < 10; i++) {
                Message<SyndEntry> message = (Message) feedChannel.receive(10000);
                if (message != null) {
                    SyndEntry entry = message.getPayload();
                    System.out.println("New Title"+entry.getPublishedDate() + " - " + entry.getTitle());
                } else {
                    break;
                }
            }
        } finally {
            context.close();
        }
    }

}