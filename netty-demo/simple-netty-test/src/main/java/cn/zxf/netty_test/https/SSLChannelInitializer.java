package cn.zxf.netty_test.https;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;

import cn.zxf.netty_test.http.MyHttpHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

public class SSLChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;

    public SSLChannelInitializer() {
        String keyStoreFilePath = "my-https.keystore";
        String keyStorePassword = "123456";

        try {
            InputStream is = SSLChannelInitializer.class.getResourceAsStream( keyStoreFilePath );

            KeyStore keyStore = KeyStore.getInstance( "PKCS12" );
            keyStore.load( is, keyStorePassword.toCharArray() );

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );
            keyManagerFactory.init( keyStore, keyStorePassword.toCharArray() );

            sslContext = SslContextBuilder.forServer( keyManagerFactory )
                    .build();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    protected void initChannel( SocketChannel ch ) {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine sslEngine = sslContext.newEngine( ch.alloc() );
        pipeline.addLast( new SslHandler( sslEngine ) )
                .addLast( "decoder", new HttpRequestDecoder() )
                .addLast( "encoder", new HttpResponseEncoder() )
                .addLast( "aggregator", new HttpObjectAggregator( 512 * 1024 ) )
                .addLast( "handler", new MyHttpHandler() );
    }

}
