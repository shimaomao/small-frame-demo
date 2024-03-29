package cn.zxf.netty_test.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class TestClient implements Runnable {

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group( group );
            b.channel( NioSocketChannel.class )
                    .option( ChannelOption.TCP_NODELAY, true );
            b.handler( new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel( SocketChannel ch ) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast( "frameDecoder", new LengthFieldBasedFrameDecoder( Integer.MAX_VALUE, 0, 4, 0, 4 ) );
                    pipeline.addLast( "frameEncoder", new LengthFieldPrepender( 4 ) );
                    pipeline.addLast( "decoder", new StringDecoder( CharsetUtil.UTF_8 ) );
                    pipeline.addLast( "encoder", new StringEncoder( CharsetUtil.UTF_8 ) );

                    pipeline.addLast( "handler", new TcpClientHandler() );
                }
            } );

            ChannelFuture f = b.connect( "127.0.0.1", 5656 )
                    .sync();
            f.channel()
                    .writeAndFlush( "hello Service!" + Thread.currentThread()
                            .getName() + ":--->:" + 10 );
            f.channel()
                    .closeFuture()
                    .sync();

        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main( String[] args ) throws Exception {
        new Thread( new TestClient(), ">>>this thread " + 10 ).start();
    }
}