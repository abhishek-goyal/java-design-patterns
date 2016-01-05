package com.iluwatar.poison.pill;

import org.junit.Test;
import org.mockito.InOrder;

import java.time.LocalDateTime;

import static org.mockito.Mockito.inOrder;

/**
 * Date: 12/27/15 - 9:45 PM
 *
 * @author Jeroen Meulemeester
 */
public class ConsumerTest extends StdOutTest {

  @Test
  public void testConsume() throws Exception {
    final Message[] messages = new Message[]{
        createMessage("you", "Hello!"),
        createMessage("me", "Hi!"),
        Message.POISON_PILL,
        createMessage("late_for_the_party", "Hello? Anyone here?"),
    };

    final MessageQueue queue = new SimpleMessageQueue(messages.length);
    for (final Message message : messages) {
      queue.put(message);
    }

    new Consumer("NSA", queue).consume();

    final InOrder inOrder = inOrder(getStdOutMock());
    inOrder.verify(getStdOutMock()).println("Message [Hello!] from [you] received by [NSA]");
    inOrder.verify(getStdOutMock()).println("Message [Hi!] from [me] received by [NSA]");
    inOrder.verify(getStdOutMock()).println("Consumer NSA receive request to terminate.");
    inOrder.verifyNoMoreInteractions();

  }

  /**
   * Create a new message from the given sender with the given message body
   *
   * @param sender  The sender's name
   * @param message The message body
   * @return The message instance
   */
  private static Message createMessage(final String sender, final String message) {
    final SimpleMessage msg = new SimpleMessage();
    msg.addHeader(Message.Headers.SENDER, sender);
    msg.addHeader(Message.Headers.DATE, LocalDateTime.now().toString());
    msg.setBody(message);
    return msg;
  }

}