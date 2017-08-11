import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelHandlerContext, ChannelInitializer, SimpleChannelInboundHandler}
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler
import io.netty.handler.codec.http.websocketx.{TextWebSocketFrame, WebSocketFrame, WebSocketServerProtocolHandler}
import io.netty.handler.codec.http.{HttpObjectAggregator, HttpServerCodec}
import io.netty.handler.logging.{LogLevel, LoggingHandler}

/**
  * Created by wxji on 2017-08-11.
  */
object Startup {
  def main(args: Array[String]): Unit = {
    val bossGroup = new NioEventLoopGroup(1)
    val workerGroup = new NioEventLoopGroup()
    try {
      val b = new ServerBootstrap()
      b.group(bossGroup, workerGroup)
        .channel(classOf[NioServerSocketChannel])
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new WebSocketServerInitializer())
      val ch = b.bind(8270).sync().channel()
      ch.closeFuture().sync()
    } finally {
      bossGroup.shutdownGracefully()
      workerGroup.shutdownGracefully()
    }
  }
}

class WebSocketServerInitializer extends ChannelInitializer[SocketChannel] {

  private val WEB_SOCKET_PATH = "/jianghu"

  override def initChannel(ch: SocketChannel): Unit = {
    val pipeline = ch.pipeline()
    pipeline.addLast(new HttpServerCodec())
    pipeline.addLast(new HttpObjectAggregator(65536))
    pipeline.addLast(new WebSocketServerCompressionHandler())
    pipeline.addLast(new WebSocketServerProtocolHandler(WEB_SOCKET_PATH, null, true))
    pipeline.addLast(new WebSocketDataHandler())
  }
}

class WebSocketDataHandler extends SimpleChannelInboundHandler[WebSocketFrame] {
  override def channelRead0(ctx: ChannelHandlerContext, webSocketFrame: WebSocketFrame): Unit = {
    ctx.writeAndFlush(new TextWebSocketFrame("ok"))
  }
}