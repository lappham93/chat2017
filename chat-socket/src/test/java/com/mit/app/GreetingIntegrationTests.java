package com.mit.app;

import com.mit.http.ApiResponse;
import com.mit.http.context.ApplicationContextProvider;
import com.mit.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Hung Le on 2/28/17.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingIntegrationTests {
    @LocalServerPort
    private int port;

    private SockJsClient sockJsClient;

    private WebSocketStompClient stompClient;

    private WebSocketClient webSocketClient;

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void setup() {
        ApplicationContextProvider appCtx = new ApplicationContextProvider();
        appCtx.setApplicationContext(applicationContext);

        this.webSocketClient = new StandardWebSocketClient();

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(this.webSocketClient));
        this.sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(this.webSocketClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.stompClient.setDefaultHeartbeat(new long[] {25000,60000});

        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        this.stompClient.setTaskScheduler(taskScheduler);

//        this.stompClient.setReceiptTimeLimit(1);

        this.headers.set("Session", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkIjp7InVpZCI6IjE5NCIsImV4cGlyZVRpbWUiOjE0OTQ5MDQ3NDc0NDEsInByb2ZpbGVUeXBlIjozLCJ0b2tlblR5cGUiOjF9LCJ2IjowLCJpYXQiOjE0OTQ4MTgzNDd9.1qXZcxgXvkWk2qq3MAUmpQp4D8RrH0VTPxDGg8NUQFI");
    }

    @Test
    public void getGreeting() throws Exception {

        final CountDownLatch latch = new CountDownLatch(100);
        final AtomicReference<Throwable> failure = new AtomicReference<>();

        StompSessionHandler handler = new TestSessionHandler(failure) {

            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
                session.setAutoReceipt(true);

                session.send("/app/test", DefaultStompSession.EMPTY_PAYLOAD);

                StompHeaders subcribeHeaders = new StompHeaders();
                subcribeHeaders.setId("0");
                subcribeHeaders.setDestination("/user/queue/test");
                for (int i = 0; i < 10; i++) {
                    session.subscribe(subcribeHeaders, new StompFrameHandler() {
                            @Override
                            public Type getPayloadType(StompHeaders headers) {
                                return ApiResponse.class;
                            }

                            @Override
                            public void handleFrame(StompHeaders headers, Object payload) {

                            }
                        });
                    try {
                        Thread.sleep(100);
                    } catch (Exception e){

                    }
                }

//                StompHeaders subcribeHeaders1 = new StompHeaders();
//                subcribeHeaders1.setDestination("/user/queue/ack");
////                subcribeHeaders1.set("receipt", "message-103");
////                subcribeHeaders1.setReceipt("message-123");
//                session.subscribe(subcribeHeaders1, new StompFrameHandler() {
//                    @Override
//                    public Type getPayloadType(StompHeaders headers) {
//                        return null;
//                    }
//
//                    @Override
//                    public void handleFrame(StompHeaders headers, Object payload) {
//                        try {
//                            System.out.println("receive ACK for request-id " + headers.get("request-id").get(0));
////                            assertEquals("Hello, Spring!", greeting.getContent());
//                        } catch (Throwable t) {
//                            failure.set(t);
//                        } finally {
////                            if (session.isConnected()) {
////                                session.disconnect();
////                            }
//                            latch.countDown();
//                        }
//                    }
//                });
                StompHeaders subcribeHeaders2 = new StompHeaders();
                subcribeHeaders2.setDestination("/user/queue/demand-client");
//                subcribeHeaders2.set("request-id", "104");
//                subcribeHeaders2.setReceipt("message-126");
                session.subscribe(subcribeHeaders2, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return ApiResponse.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        ApiResponse location = (ApiResponse) payload;
                        try {
                            System.out.println(JsonUtils.Instance.toJson(location.getData()));
//                            assertEquals("Hello, Spring!", greeting.getContent());
                        } catch (Throwable t) {
                            failure.set(t);
                        } finally {
//                            if (session.isConnected()) {
//                                session.disconnect();
//                            }
                            latch.countDown();
                        }
                    }
                });

//                StompHeaders subcribeHeaders = new StompHeaders();
//                subcribeHeaders.setDestination("/user/queue/demand-tracking");
//                subcribeHeaders.set("request-id", "102");
//                subcribeHeaders.setAck("client");
////                subcribeHeaders.set("receipt", "102");
////                subcribeHeaders.setReceipt("message-127");
//                session.subscribe(subcribeHeaders, new StompFrameHandler() {
//                    @Override
//                    public Type getPayloadType(StompHeaders headers) {
//                        return ApiResponse.class;
//                    }
//
//                    @Override
//                    public void handleFrame(StompHeaders headers, Object payload) {
//                        ApiResponse<TrackingLocation> location = (ApiResponse) payload;
//                        try {
//                            System.out.println(JsonUtils.Instance.toJson(location.getData()));
////                            assertEquals("Hello, Spring!", greeting.getContent());
//                        } catch (Throwable t) {
//                            failure.set(t);
//                        } finally {
////                            if (session.isConnected()) {
////                                session.disconnect();
////                            }
//                            latch.countDown();
//                        }
//                    }
//                });
                try {
//                    try {
//                        Thread.sleep(120 * 1000);
//                    } catch (Exception e) {
//
//                    }
                    StompHeaders headers = new StompHeaders();
                    headers.setDestination("/app/demand-client-status");
//                    headers.set("request-id", "101");
//                    headers.setReceipt("message-124");
//                    headers.setContentType(new MimeType("application/json;charset=UTF-8"));
//                    headers.setContentLength(JsonUtils.Instance.toJson(new LandscapingDemand()).length() + 1);
//                    session.send(headers, new LandscapingDemand()).addReceiptTask(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("received SEND receipt");
//                        }
//                    });
//                    String testStr = "abc123";
//                    BinaryMessage binaryMessage = new BinaryMessage(testStr.getBytes());
//                    session.send("/app/upload", testStr.getBytes());

//                    session.send("/app/demand-provider-accept", new LandscapingDemandAcceptance()).addReceiptTask(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("received SEND receipt");
//                        }
//                    });
//                    try {
//                        Thread.sleep(10 * 1000);
//                    } catch (Exception e) {
//
//                    }
//                    session.send("/app/demand-cancel", DefaultStompSession.EMPTY_PAYLOAD);
                } catch (Throwable t) {
                    failure.set(t);
                    latch.countDown();
                }
            }
        };
        this.stompClient.connect("ws://localhost:{port}/api/websocket", this.headers, handler, this.port);

        if (latch.await(300, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        }
        else {
//            fail("Greeting not received");
        }

    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;


        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }
    }
}
