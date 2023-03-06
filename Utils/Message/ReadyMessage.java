package Utils.Message;

/*
 * Message de type
 */
public class ReadyMessage extends Message {

    @Override
    public MessageType getType() {
        return MessageType.READY;
    }
}
