package home.kali.admin.grpc

import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Default implementation of ChannelFactory for gRPC communication
 */
@Component
class DefaultChannelFactory(
    @Value("\${grpc.report-generator.host:localhost}") private val host: String,
    @Value("\${grpc.report-generator.port:9090}") private val port: Int
) : ChannelFactory {

    private var channel: ManagedChannel? = null

    override fun createChannel(): Channel {
        if (channel == null || channel!!.isShutdown) {
            channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build()
        }
        return channel!!
    }
}
