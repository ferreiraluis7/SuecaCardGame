package org.academiadecodigo.bootcamp.client;

public class Client implements Playable {

    public static void main(String[] args) {
        throw new UnsupportedOperationException();
    }

    /**
     * Starts the client
     */
    private void start(){
        //client network methods here
        throw new UnsupportedOperationException();
    }

    /**
     * Decodes incoming message from server
     *
     * @param incoming incoming message from server
     *
     * @return decoded message
     */
    private String decodeReceivedString(String incoming) {
        throw new UnsupportedOperationException();
    }

    /**
     * Renders the decoded message to the terminal
     */
    private void renderToScreen(){
        throw new UnsupportedOperationException();
    }

    /**
     * @see Playable#play(String)
     */
    @Override
    public void play(String move) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Playable#checkKeyboardInput(String)
     *
     **/
    @Override
    public boolean checkKeyboardInput(String input) {
        throw new UnsupportedOperationException();
    }
}
