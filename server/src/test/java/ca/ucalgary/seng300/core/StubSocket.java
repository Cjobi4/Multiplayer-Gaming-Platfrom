package ca.ucalgary.seng300.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Stub socket for server-side unit tests, allowing test code to create
 * Session objects without a real network connection.
 */
public class StubSocket extends Socket {

    private final ByteArrayOutputStream fakeOutputStream;
    private final ByteArrayInputStream fakeInputStream;

    /**
     * Creates a StubSocket with a simulated server response.
     *
     * @param simulatedServerResponse the bytes to return from getInputStream()
     */
    public StubSocket(byte[] simulatedServerResponse) {
        this.fakeOutputStream = new ByteArrayOutputStream();
        this.fakeInputStream = new ByteArrayInputStream(simulatedServerResponse);
    }

    @Override
    public OutputStream getOutputStream() {
        return fakeOutputStream;
    }

    @Override
    public InputStream getInputStream() {
        return fakeInputStream;
    }

    /**
     * Returns the bytes written to this socket's output stream.
     *
     * @return captured output bytes
     */
    public byte[] getCapturedOutput() {
        return fakeOutputStream.toByteArray();
    }
}
