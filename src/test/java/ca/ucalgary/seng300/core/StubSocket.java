package ca.ucalgary.seng300.core;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// Stub connection so we don't need to rely on a real connection
public class StubSocket extends Socket {

    // fake output to the server
    private final ByteArrayOutputStream fakeOutputStream;

    // fake input from the server back to the user
    private final ByteArrayInputStream fakeInputStream;

    // Here we pass in the fake response we want to simulate
    public StubSocket(byte[] simulatedServerResponse) {
        this.fakeOutputStream = new ByteArrayOutputStream();
        this.fakeInputStream = new ByteArrayInputStream(simulatedServerResponse);
    }

    // override the talking pipe
    @Override
    public OutputStream getOutputStream() {
        return fakeOutputStream;
    }

    // override the listening pipe
    @Override
    public InputStream getInputStream() {
        return fakeInputStream;
    }

    // helper method so our test can easily read what the Network class sent
    public byte[] getCapturedOutput() {
        return fakeOutputStream.toByteArray();
    }
}
