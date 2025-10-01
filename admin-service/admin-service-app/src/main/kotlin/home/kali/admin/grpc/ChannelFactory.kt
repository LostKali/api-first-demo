package home.kali.admin.grpc

import io.grpc.Channel

/**
 * Factory interface for creating gRPC channels
 */
interface ChannelFactory {
    fun createChannel(): Channel
}
