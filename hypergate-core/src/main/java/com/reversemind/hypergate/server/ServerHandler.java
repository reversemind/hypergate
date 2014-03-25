package com.reversemind.hypergate.server;

import com.reversemind.hypergate.serialization.KryoDeserializer;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Copyright (c) 2013-2014 Eugene Kalinin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ServerHandler.class.getName());

    private IPayloadProcessor payloadProcessor;
    private boolean dropClientConnection = false;
    private Metrics metrics;
    // TODO #6 KryoSerializer
//    private KryoDeserializer kryoDeserializer;

    public ServerHandler(IPayloadProcessor payloadProcessor, Metrics metrics, boolean dropClientConnection) {
        this.payloadProcessor = payloadProcessor;
        this.dropClientConnection = dropClientConnection;
        this.metrics = metrics;
    }

    // TODO #6 KryoSerializer
//    public ServerHandler(IPayloadProcessor payloadProcessor, Metrics metrics, boolean dropClientConnection, KryoDeserializer kryoDeserializer) {
//        this.payloadProcessor = payloadProcessor;
//        this.dropClientConnection = dropClientConnection;
//        this.metrics = metrics;
//        this.kryoDeserializer = kryoDeserializer;
//    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent && ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
            LOG.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent messageEvent) {

        // TODO what about delay + very long messages???
        long beginTime = System.currentTimeMillis();

        // TODO #6 KryoSerializer
//        Object object = null;
//        try {
//            object = this.payloadProcessor.process(this.kryoDeserializer.deserialize((byte[]) messageEvent.getMessage()));
//        } catch (IOException e) {
//            LOG.error("KryoDeserializer needs for array", e);
//        }

        Object object = this.payloadProcessor.process(messageEvent.getMessage());

        if (this.metrics != null) {
            this.metrics.addRequest((System.currentTimeMillis() - beginTime));
        }

        ChannelFuture channelFuture = messageEvent.getChannel().write(object);

        if (!this.dropClientConnection) {
            // see here - http://netty.io/3.6/guide/
            // Close connection right after sending
            if(channelFuture != null){
                channelFuture.addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) {
                        Channel channel = future.getChannel();
                        channel.close();
                    }
                });
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        LOG.warn("Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }
}